package com.zoho.todo.appdatabase.databaseentity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_service_remote_keys")
data class ToDoTaskRemoteKeys(
    @PrimaryKey
    val repoId: Long = 0L,
    val prevKey: Int?,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
