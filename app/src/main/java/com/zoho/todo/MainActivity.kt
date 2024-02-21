package com.zoho.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.zoho.todo.ui.composeui.AddTaskScreenComponent
import com.zoho.todo.ui.composeui.DetailScreenComponent
import com.zoho.todo.ui.composeui.ErrorItem
import com.zoho.todo.ui.composeui.LoadingItem
import com.zoho.todo.ui.composeui.LoadingView
import com.zoho.todo.ui.composeui.TaskComponentItem
import com.zoho.todo.ui.theme.ToDoTheme
import com.zoho.todo.viewModel.AddTaskScreenVM
import com.zoho.todo.viewModel.DetailScreenVM
import com.zoho.todo.viewModel.HomeScreenVM
import com.zoho.todo.worker.UpdateTaskWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


const val WORKMANAGERTAG = "uploadTask"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            ToDoTheme {
                Scaffold(content = { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = ToDoScreens.Home.route,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(route = ToDoScreens.Home.route) {
                            val homeScreenVm = hiltViewModel<HomeScreenVM>()
                            val listOfTask =
                                homeScreenVm.pagedToDoTaskDatasource.collectAsLazyPagingItems()
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            ) {

                                items(listOfTask.itemCount) {
                                    listOfTask[it]?.let { task ->
                                        TaskComponentItem(
                                            modifier = Modifier,
                                            task,
                                            onClick = { id ->
                                                navController.navigate("${ToDoScreens.Detail.route}/${id}")
                                            })
                                    }
                                }

                                listOfTask.apply {
                                    when {
                                        loadState.refresh is LoadState.Loading -> {
                                            item {
                                                LoadingView(modifier = Modifier.fillParentMaxSize())
                                            }
                                        }

                                        loadState.append is LoadState.Loading -> {
                                            item { LoadingItem() }
                                        }

                                        loadState.refresh is LoadState.Error -> {
                                            val e = listOfTask.loadState.refresh as LoadState.Error
                                            item {
                                                ErrorItem(message = "No Service",
                                                    modifier = Modifier.fillParentMaxSize(),
                                                    onClickRetry = { retry() })
                                            }
                                        }

                                        loadState.append is LoadState.Error -> {
                                            val e = listOfTask.loadState.append as LoadState.Error
                                            item {
                                                ErrorItem(message = e.error.localizedMessage ?: "",
                                                    onClickRetry = { retry() })
                                            }
                                        }
                                    }
                                }


                            }


                        }


                        composable(
                            "${ToDoScreens.Detail.route}/{taskID}",
                            arguments = listOf(navArgument("taskID") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val detailScreenVm = hiltViewModel<DetailScreenVM>()
                            var editDescription by remember { mutableStateOf("") }
                            var taskStatus by remember { mutableStateOf(false) }

                            val taskId by remember {
                                mutableIntStateOf(backStackEntry.arguments?.getInt("taskID") ?: 0)
                            }
                            LaunchedEffect(Unit) {
                                detailScreenVm.getTaskBasedOnID(
                                    taskId
                                )
                            }


                            val detailScreenUIState by detailScreenVm.detailScreenUIState.collectAsStateWithLifecycle()


                            LaunchedEffect(key1 = detailScreenUIState.moveToPreviousScreen){
                                if (detailScreenUIState.moveToPreviousScreen == true) {
                                    navController.navigateUp()
                                }
                            }

                            DetailScreenComponent(
                                modifier = Modifier,
                                onBackClicked = {
                                    navController.navigateUp()
                                },
                                descritpion = editDescription,
                                onDescriptionChange = {
                                    if (it.length <= 100) {
                                        editDescription = it
                                    }
                                },
                                onUpdateClicked = {
                                    detailScreenVm.updateTaskEntity(
                                        editDescription,
                                        taskStatus,
                                        taskId
                                    )
                                },
                                onStatus = {
                                    taskStatus = it
                                },
                                toDoTaskEntity = detailScreenUIState.todaTaskEntity,
                                taskStatus = taskStatus,
                                toShowErrorDialog = detailScreenUIState.toShowLoading
                                    ?: false, onDismissDialog = detailScreenVm::dismissDialog,
                                onAddClick = {
                                    navController.navigate("${ToDoScreens.AddTask.route}/${detailScreenUIState.todaTaskEntity?.userId}")
                                }, onDelete = {
                                    detailScreenVm.deleteTask(taskId,detailScreenUIState.todaTaskEntity?.toDoTaskID ?: 0L)
                                }
                            )
                        }



                        composable(
                            "${ToDoScreens.AddTask.route}/{userID}",
                            arguments = listOf(navArgument("userID") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val addScreenVm = hiltViewModel<AddTaskScreenVM>()
                            var editDescription by remember { mutableStateOf("") }
                            val userID by remember {
                                mutableIntStateOf(backStackEntry.arguments?.getInt("userID") ?: 0)
                            }
                            val addScreenUIState by addScreenVm.addScreenUIState.collectAsStateWithLifecycle()
                            LaunchedEffect(key1 = addScreenUIState.toMovePreviousScreen) {
                                if (addScreenUIState.toMovePreviousScreen == true) {
                                    val result = snackbarHostState.showSnackbar(
                                        "Added Task  SuccessFully",
                                        "Back",
                                        duration = SnackbarDuration.Indefinite,
                                    )
                                    when(result){
                                        SnackbarResult.Dismissed -> {

                                        }
                                        SnackbarResult.ActionPerformed -> {
                                            navController.navigateUp()
                                        }
                                    }
                                }
                            }
                            AddTaskScreenComponent(
                                modifier = Modifier,
                                onBackClicked = { navController.navigateUp() },
                                onAddClicked = {
                                    addScreenVm.addTask(
                                        toDoDescription = editDescription,
                                        userID = userID
                                    )
                                },
                                onDescriptionChange = {
                                    editDescription = it
                                },
                                descritpion = editDescription,
                                toShowErrorDialog = addScreenUIState.toShowLoading ?: false,
                                onDismissDialog = addScreenVm::dismissDialog
                            )
                        }


                    }
                }, snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                })

            }
        }
      val workRequest =  OneTimeWorkRequestBuilder<UpdateTaskWorker>()
            .setConstraints(Constraints(
                requiredNetworkType = NetworkType.CONNECTED
            ))
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30,
                TimeUnit.MILLISECONDS)
            .addTag(WORKMANAGERTAG)
            .build()
        WorkManager
            .getInstance(this)
            .enqueueUniqueWork(WORKMANAGERTAG,ExistingWorkPolicy.REPLACE,workRequest)
    }




    override fun onDestroy() {
        super.onDestroy()
       WorkManager.getInstance(this).cancelAllWorkByTag(WORKMANAGERTAG)
    }
}


sealed class ToDoScreens(val route: String) {
    data object Home : ToDoScreens("home")
    data object Detail : ToDoScreens("detail")
    data object AddTask : ToDoScreens("addTask")
}





