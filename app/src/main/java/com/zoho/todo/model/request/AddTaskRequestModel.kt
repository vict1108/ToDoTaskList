package com.zoho.todo.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AddTaskRequestModel(
    @SerialName("completed")
    val completed: Boolean? = null,
    @SerialName("todo")
    val todo: String? = null,
    @SerialName("userId")
    val userID:Int? = null
)
