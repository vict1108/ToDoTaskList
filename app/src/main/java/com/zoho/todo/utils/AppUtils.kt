package com.zoho.todo.utils

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppUtils {

    fun Modifier.debugBorder(color: Color = Color.Blue, width: Dp = 1.dp): Modifier {
        return this.border(width = width, color = color)
    }
}