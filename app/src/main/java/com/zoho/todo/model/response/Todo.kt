package com.zoho.todo.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    @SerialName("completed")
    val completed: Boolean? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("todo")
    val todo: String? = null,
    @SerialName("userId")
    val userId: Int? = null
)