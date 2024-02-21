
package com.zoho.todo.appdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskRemoteKeys

@Dao
interface TodoTaskRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: ToDoTaskRemoteKeys)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ToDoTaskRemoteKeys>)

    @Query("SELECT * FROM todo_service_remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Long): ToDoTaskRemoteKeys?

    @Query("DELETE FROM todo_service_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From todo_service_remote_keys Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

    @Query("DELETE FROM todo_service_remote_keys WHERE repoId = :id")
    suspend fun deleteParticularRemoteKey(id:Long)
}

