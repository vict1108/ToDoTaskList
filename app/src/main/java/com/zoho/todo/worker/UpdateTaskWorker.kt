package com.zoho.todo.worker

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import arrow.core.Either
import com.zoho.todo.R
import com.zoho.todo.appdatabase.AppDatabase
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.appdatabase.dao.TodoTaskRemoteKeysDao
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.di.Dispatcher
import com.zoho.todo.di.ToDoTaskDispatchers
import com.zoho.todo.model.request.AddTaskRequestModel
import com.zoho.todo.repo.detailscreenrepo.DetailScreenRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlin.math.log

//  @Dispatcher(ToDoTaskDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
@HiltWorker
class UpdateTaskWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val appDatabase: AppDatabase,
    @Dispatcher(ToDoTaskDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    private val toDoTaskDao: ToDoTaskDao,
    private val detailScreenRepo: DetailScreenRepo
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(coroutineDispatcher) {
        try {
            var listOfTaskToUpload = toDoTaskDao.getAllTaskNotUploaded()
            Log.e("uploadWorker",listOfTaskToUpload.toString())
            while (listOfTaskToUpload.isNotEmpty()) {
                listOfTaskToUpload = toDoTaskDao.getAllTaskNotUploaded()
                val toDoTaskEntity = listOfTaskToUpload.first()

                when {
                    toDoTaskEntity.isToUpdate == true -> {
                        updateTask(toDoTaskEntity)
                    }

                    toDoTaskEntity.isDeleted == true -> {
                        deleteTask(toDoTaskEntity)
                    }

                    toDoTaskEntity.isToAdd == true -> {
                        addTask(toDoTaskEntity)
                    }
                }
            }
        } catch (e:Exception){
            Result.retry()
        }
        Result.success()
    }


    private suspend fun updateTask(toDoTaskEntity: ToDoTaskEntity) {
        toDoTaskEntity.id?.let {
            detailScreenRepo.updateTask(
                id = it,
                toDoDescription = toDoTaskEntity.todo ?: "",
                isCompleted = toDoTaskEntity.completed ?: false
            ).collect{
                when(it){
                    is Either.Left -> {}
                    is Either.Right -> {
                      toDoTaskDao.updateTask(toDoTaskEntity.copy(
                          isUploaded = true,
                          isToUpdate = false
                      ))
                    }
                }
            }
        }
    }


    private suspend fun deleteTask(toDoTaskEntity: ToDoTaskEntity){
        toDoTaskEntity.id?.let {id ->
            detailScreenRepo.deleteTask(
                id = id
            ).collect{
                when(it){
                    is Either.Left -> {}
                    is Either.Right -> {
                        toDoTaskDao.deleteTask(id)
                    }
                }
            }
        }
    }

    private suspend fun addTask(toDoTaskEntity: ToDoTaskEntity){
        toDoTaskEntity.id?.let {id ->
            detailScreenRepo.addTask(
                AddTaskRequestModel(
                    completed = toDoTaskEntity.completed,

                    todo = toDoTaskEntity.todo,

                    userID = toDoTaskEntity.userId
                )
            ).collect{
                when(it){
                    is Either.Left -> {}
                    is Either.Right -> {
                        toDoTaskDao.insertTask(
                            toDoTaskEntity.copy(
                                isToAdd = false,
                                isUploaded = true
                            )
                        )
                    }
                }
            }
        }
    }

}