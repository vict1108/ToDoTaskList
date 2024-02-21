package com.zoho.todo.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToDoResponseModel(
    @SerialName("limit")
    val limit: Int? = null,
    @SerialName("skip")
    val skip: String? = null,
    @SerialName("todos")
    val todos: List<Todo>? = null,
    @SerialName("total")
    val total: Int? = null
)