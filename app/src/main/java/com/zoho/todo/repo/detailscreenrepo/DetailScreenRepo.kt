package com.zoho.todo.repo.detailscreenrepo

import arrow.core.Either
import com.zoho.todo.model.request.AddTaskRequestModel
import com.zoho.todo.model.response.ToDoAddTaskResponseModel
import com.zoho.todo.model.response.ToDoDeleteResponseModel
import com.zoho.todo.model.response.Todo
import kotlinx.coroutines.flow.Flow

interface DetailScreenRepo {

    suspend fun updateTask(id:Int,toDoDescription:String,isCompleted:Boolean):Flow<Either<String,Todo>>


    suspend fun deleteTask(id:Int):Flow<Either<String, ToDoDeleteResponseModel>>


    suspend fun addTask(addTaskRequestModel: AddTaskRequestModel):Flow<Either<String,ToDoAddTaskResponseModel>>
}