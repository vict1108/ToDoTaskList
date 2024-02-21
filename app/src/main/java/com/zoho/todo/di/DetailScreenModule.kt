package com.zoho.todo.di

import com.zoho.todo.repo.detailscreenrepo.DetailScreenRepo
import com.zoho.todo.repo.detailscreenrepo.DetailScreenRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DetailScreenModule {

    @Binds
    abstract fun bindDetailScreenRepo(detailScreenRepoImpl: DetailScreenRepoImpl): DetailScreenRepo
}