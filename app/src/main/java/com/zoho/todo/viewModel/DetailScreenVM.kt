package com.zoho.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import androidx.room.withTransaction
import arrow.core.Either
import com.zoho.todo.appdatabase.AppDatabase
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.appdatabase.dao.TodoTaskRemoteKeysDao
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.repo.detailscreenrepo.DetailScreenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DetailScreenUIState(

    val todaTaskEntity: ToDoTaskEntity? = null,
    val errorMsg: String? = null,
    val isErrorDialogToShow: Boolean? = null,
    val toShowLoading:Boolean? = null,
    val moveToPreviousScreen:Boolean? = false

)


@HiltViewModel
class DetailScreenVM @Inject constructor(
    private val toDoTaskDao: ToDoTaskDao,
    private val detailScreenRepo: DetailScreenRepo,
    private val appDatabase: AppDatabase,
    private val remoteKeysDao: TodoTaskRemoteKeysDao
) : ViewModel() {

    private var _detailScreenUIState = MutableStateFlow(DetailScreenUIState())

    val detailScreenUIState: StateFlow<DetailScreenUIState> = _detailScreenUIState.asStateFlow()



    fun deleteTask(id:Int,taskId: Long){
        viewModelScope.launch {
            launch {
                _detailScreenUIState.update {
                    it.copy(
                       toShowLoading = true
                    )
                }
            }


            launch {
                detailScreenRepo.deleteTask(id).collect{
                    when(it){
                        is Either.Left -> {
                            _detailScreenUIState.value.todaTaskEntity?.let {taskEntity->
                                toDoTaskDao.updateTask(
                                    taskEntity.copy(
                                        isDeleted = true,
                                        isUploaded = false,
                                        toDoTaskID = taskId
                                    )
                                )
                            }
                        }
                        is Either.Right -> {
                            appDatabase.withTransaction {
                                toDoTaskDao.deleteTask(id)
                                remoteKeysDao.deleteParticularRemoteKey(id.toLong())
                            }
                        }
                    }

                    _detailScreenUIState.update {
                        it.copy(
                            toShowLoading = false,
                            moveToPreviousScreen = true
                        )
                    }
                }
            }
        }
    }



    fun dismissDialog(toDismiss:Boolean){
        viewModelScope.launch {
            _detailScreenUIState.update {
                it.copy(
                    toShowLoading = toDismiss
                )
            }
        }
    }


    fun getTaskBasedOnID(taskId: Int) {
        viewModelScope.launch {
            toDoTaskDao.getToDoTask(taskID = taskId).collect { taskEntity ->
                _detailScreenUIState.update {
                    it.copy(
                        todaTaskEntity = taskEntity
                    )
                }
            }
        }
    }

    fun updateTaskEntity(updatedDescription: String, isTaskCompleted: Boolean, taskId: Int) {
        viewModelScope.launch {

            launch {
                _detailScreenUIState.update {
                    it.copy(
                        toShowLoading = true
                    )
                }
            }


            launch {
                _detailScreenUIState.update {
                    it.copy(
                        todaTaskEntity = _detailScreenUIState.value.todaTaskEntity?.copy(
                            todo = updatedDescription,
                            completed = isTaskCompleted
                        )
                    )
                }
            }

            launch {
                detailScreenRepo.updateTask(taskId, updatedDescription,isTaskCompleted).collect { response ->
                    when (response) {
                        is Either.Left -> {
                            _detailScreenUIState.update {
                                it.copy(
                                    isErrorDialogToShow = true,
                                    errorMsg = response.value,
                                    toShowLoading = false,
                                    todaTaskEntity = _detailScreenUIState.value.todaTaskEntity?.copy(
                                        isUploaded = false,
                                        isToUpdate = true
                                    )
                                )
                            }

                        }

                        is Either.Right -> {
                            _detailScreenUIState.update {
                                it.copy(
                                    toShowLoading = false
                                )
                            }

                        }
                    }
                }
            }


            launch {
                _detailScreenUIState.value.todaTaskEntity?.let {
                    toDoTaskDao.updateTask(it)
                }
            }


        }
    }


}