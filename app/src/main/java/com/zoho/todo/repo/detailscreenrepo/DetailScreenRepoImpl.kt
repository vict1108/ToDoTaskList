package com.zoho.todo.repo.detailscreenrepo

import arrow.core.Either
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.di.Dispatcher
import com.zoho.todo.di.ToDoTaskDispatchers
import com.zoho.todo.model.request.AddTaskRequestModel
import com.zoho.todo.model.request.ToDoUpdateRequestModel
import com.zoho.todo.model.response.ToDoAddTaskResponseModel
import com.zoho.todo.model.response.ToDoDeleteResponseModel
import com.zoho.todo.model.response.Todo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailScreenRepoImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val toDoTaskDao: ToDoTaskDao,
    @Dispatcher(ToDoTaskDispatchers.IO) val coroutineDispatcher: CoroutineDispatcher
) : DetailScreenRepo {


    override suspend fun updateTask(
        id: Int,
        toDoDescription: String,
        isCompleted: Boolean
    ): Flow<Either<String, Todo>> = flow {
        runCatching {
            httpClient.put("https://dummyjson.com/todos/${id}") {
                setBody(
                    ToDoUpdateRequestModel(
                        completed = isCompleted,
                        todo = toDoDescription
                    )
                )
            }.body<Todo>()
        }.onSuccess {
            emit(Either.Right(it))
        }.onFailure {
            emit(Either.Left(it.message ?: "Something went wrong"))
        }
    }.flowOn(coroutineDispatcher)

    override suspend fun deleteTask(id: Int): Flow<Either<String, ToDoDeleteResponseModel>> =
        flow{
            runCatching {
                httpClient.delete("https://dummyjson.com/todos/${id}") {
                }.body<ToDoDeleteResponseModel>()
            }.onSuccess {
                emit(Either.Right(it))
            }.onFailure {
                emit(Either.Left(it.message ?: "Something went wrong"))
            }


        }.flowOn(coroutineDispatcher)

    override suspend fun addTask(addTaskRequestModel: AddTaskRequestModel): Flow<Either<String, ToDoAddTaskResponseModel>> =
        flow<Either<String, ToDoAddTaskResponseModel>> {
            runCatching {
                httpClient.post("https://dummyjson.com/todos/add") {
                    setBody(addTaskRequestModel)
                }.body<ToDoAddTaskResponseModel>()
            }.onSuccess {
                emit(Either.Right(it))
            }.onFailure {
                emit(Either.Left(it.message ?: "Something went wrong"))
            }
        }.flowOn(coroutineDispatcher)
}