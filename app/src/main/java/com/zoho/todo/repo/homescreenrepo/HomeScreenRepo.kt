package com.zoho.todo.repo.homescreenrepo

import androidx.paging.PagingData
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {

    fun getAllToDoTask(): Flow<PagingData<ToDoTaskEntity>>
}