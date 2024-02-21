package com.zoho.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.repo.homescreenrepo.HomeScreenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class HomeScreenVM  @Inject constructor(
    private val homeScreenRepo: HomeScreenRepo
): ViewModel() {

    @ExperimentalPagingApi
    val pagedToDoTaskDatasource: Flow<PagingData<ToDoTaskEntity>> =
        homeScreenRepo.getAllToDoTask().cachedIn(viewModelScope)

}