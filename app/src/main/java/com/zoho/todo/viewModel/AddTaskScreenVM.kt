package com.zoho.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.model.request.AddTaskRequestModel
import com.zoho.todo.repo.detailscreenrepo.DetailScreenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


data class AddScreenUIState(
    val todaTaskEntity: ToDoTaskEntity? = null,
    val errorMsg: String? = null,
    val isErrorDialogToShow: Boolean? = null,
    val toShowLoading: Boolean? = null,
    val toMovePreviousScreen:Boolean? = null

)

@HiltViewModel
class AddTaskScreenVM @Inject constructor(
    private val detailScreenRepo: DetailScreenRepo,
    private val toDoTaskDao: ToDoTaskDao
) : ViewModel() {

    private var _addScreenUIState = MutableStateFlow(AddScreenUIState())

    val addScreenUIState: StateFlow<AddScreenUIState> = _addScreenUIState.asStateFlow()
    val generateRandomID = List(1) { Random.nextInt(151, 200) }

    fun dismissDialog(toDismiss:Boolean){
        viewModelScope.launch {
            _addScreenUIState.update {
                it.copy(
                    toShowLoading = toDismiss
                )
            }
        }
    }

    fun addTask(toDoDescription: String, userID: Int) {
        viewModelScope.launch {
            launch {
                _addScreenUIState.update {
                    it.copy(
                        toShowLoading = true
                    )
                }
            }

            detailScreenRepo.addTask(
                AddTaskRequestModel(
                    completed = false,
                    userID = userID,
                    todo = toDoDescription
                )
            ).collect { response ->
                _addScreenUIState.update {
                    it.copy(
                        errorMsg = (response as? Either.Left)?.value ?: "",
                        toShowLoading = false,
                        toMovePreviousScreen = true
                    )
                }
                toDoTaskDao.insertTask(
                    ToDoTaskEntity(
                        completed = false,
                        id = (response as? Either.Right)?.value?.id ?: generateRandomID.first(),
                        todo = toDoDescription,
                        userId = userID,
                        isUploaded = !response.isLeft(),
                        isToAdd = response.isLeft()
                    )
                )
            }
        }
    }
}