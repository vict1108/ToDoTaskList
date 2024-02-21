package com.zoho.todo.paged

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.zoho.todo.appdatabase.AppDatabase
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskRemoteKeys
import com.zoho.todo.model.response.ToDoResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.post
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class ToDoTaskPagingSource(
    private val httpClient: HttpClient,
    private val appDatabase: AppDatabase,
) : RemoteMediator<Int, ToDoTaskEntity>() {


    private val PAGE_INDEX = 0


    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - (appDatabase.toDoRemoteKeys().getCreationTime() ?: 0) <= cacheTimeout)
        {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ToDoTaskEntity>
    ): MediatorResult {

        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(10) ?: PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                Log.e("actionAppeb", "$nextKey")
                nextKey
            }
        }

        try {

            val response =
                httpClient
                    .get("https://dummyjson.com/todos?limit=10&skip=${loadKey}").body<ToDoResponseModel>()

            val listOfToDoTask = response.todos?.mapIndexed { index, todo ->
                ToDoTaskEntity(
                    isUploaded = true,
                    todo = todo.todo,
                    userId = todo.userId,
                    id = todo.id,
                    completed = todo.completed
                )
            } ?: emptyList()

            Log.e("ListOfItem",listOfToDoTask.toString())

            val endOfItem = listOfToDoTask.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.toDoTaskDao().clearRepos()
                    appDatabase.toDoRemoteKeys().clearRemoteKeys()
                }

                val prevKey = if (loadKey == PAGE_INDEX) null else loadKey - 10
                val nextKey = if (endOfItem) null else loadKey + 10
                val keys = listOfToDoTask.map { index ->
                    ToDoTaskRemoteKeys(
                        repoId = index.id?.toLong() ?: 0L,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                appDatabase.toDoRemoteKeys().insertAll(keys)
                appDatabase.toDoTaskDao().insert(listOfToDoTask)
            }


            return    MediatorResult.Success(
                endOfPaginationReached = response.todos.isNullOrEmpty()
            )

        } catch (io: IOException) {
            return MediatorResult.Error(io)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: ClientRequestException) {
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ToDoTaskEntity>): ToDoTaskRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                repo.id?.let {
                    appDatabase.toDoRemoteKeys().remoteKeysRepoId(it.toLong())
                }

            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ToDoTaskEntity>): ToDoTaskRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.let { repo ->
            Log.e(
                "nextKeyInDatabase",
                "${repo},${
                    appDatabase.toDoRemoteKeys().remoteKeysRepoId(repo.toLong())?.nextKey
                }"
            )
            appDatabase.toDoRemoteKeys().remoteKeysRepoId(repo.toLong())
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ToDoTaskEntity>
    ): ToDoTaskRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.toDoRemoteKeys().remoteKeysRepoId(repoId = repoId.toLong())
            }
        }
    }
}