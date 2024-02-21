package com.zoho.todo.appdatabase.databaseentity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "todo_task")
data class ToDoTaskEntity(
    @ColumnInfo("completed")
    val completed: Boolean? = null,
    @ColumnInfo("id")
    val id: Int? = null,
    @ColumnInfo("todo")
    val todo: String? = null,
    @ColumnInfo("userId")
    val userId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val toDoTaskID:Long = 0L,
    @ColumnInfo("isUploaded")
    val isUploaded:Boolean? = null,
    @ColumnInfo("isDeleted")
    val isDeleted:Boolean? = null,
    @ColumnInfo("isToUpdate")
    val isToUpdate:Boolean? = null,
    @ColumnInfo("isToAdd")
    val isToAdd:Boolean? = false
)
