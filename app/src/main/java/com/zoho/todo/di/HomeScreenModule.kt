package com.zoho.todo.di

import com.zoho.todo.repo.homescreenrepo.HomeScreenImpl
import com.zoho.todo.repo.homescreenrepo.HomeScreenRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract  class HomeScreenModule {

    @Binds
    abstract fun bindHomeScreenRepo(homeScreenImpl: HomeScreenImpl):HomeScreenRepo
}