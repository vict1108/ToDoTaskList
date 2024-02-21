package com.zoho.todo.appdatabase.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repos: List<ToDoTaskEntity>)

    @Query("SELECT * FROM todo_task")
    fun getAllToDoTask(): PagingSource<Int, ToDoTaskEntity>

    @Query("SELECT * FROM todo_task WHERE isUploaded = 0")
    fun getAllTaskNotUploaded(): List<ToDoTaskEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(toDoTaskEntity: ToDoTaskEntity)


    @Query("SELECT * FROM todo_task WHERE id = :taskID")
    fun getToDoTask(taskID: Int): Flow<ToDoTaskEntity>


    @Update
    suspend fun updateTask(toDoTaskEntity: ToDoTaskEntity)

    @Query("DELETE FROM todo_task")
    suspend fun clearRepos()

    @Query("DELETE FROM todo_task WHERE id = :userId")
    suspend fun deleteTask(userId: Int)
}