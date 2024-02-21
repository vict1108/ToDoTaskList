package com.zoho.todo.di

import com.zoho.todo.appdatabase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun toDoTaskDao(appDatabase: AppDatabase) = appDatabase.toDoTaskDao()

    @Provides
    fun toDoTaskRemoteDao(appDatabase: AppDatabase) = appDatabase.toDoRemoteKeys()
}