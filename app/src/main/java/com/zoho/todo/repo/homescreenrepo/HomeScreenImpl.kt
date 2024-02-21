package com.zoho.todo.repo.homescreenrepo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zoho.todo.appdatabase.AppDatabase
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.di.Dispatcher
import com.zoho.todo.di.ToDoTaskDispatchers
import com.zoho.todo.paged.ToDoTaskPagingSource
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeScreenImpl @Inject constructor(
    @Dispatcher(ToDoTaskDispatchers.IO)  val coroutineDispatcher: CoroutineDispatcher,
    private val toDoTaskDao: ToDoTaskDao,
    private val httpClient: HttpClient,
    private val appDatabase: AppDatabase
): HomeScreenRepo {


    @OptIn(ExperimentalPagingApi::class)
    override fun getAllToDoTask(): Flow<PagingData<ToDoTaskEntity>>   = Pager(
    config = PagingConfig(pageSize = 10, enablePlaceholders = false),
    remoteMediator = ToDoTaskPagingSource(httpClient, appDatabase)){
        toDoTaskDao.getAllToDoTask()
    }.flow.flowOn(coroutineDispatcher)
}