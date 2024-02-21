package com.zoho.todo.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToDoDeleteResponseModel(
    @SerialName("completed")
    val completed: Boolean? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("isDeleted")
    val isDeleted: Boolean? = null,
    @SerialName("todo")
    val todo: String? = null,
    @SerialName("userId")
    val userId: Int? = null
)