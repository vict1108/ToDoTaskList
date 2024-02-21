package com.zoho.todo.ui.composeui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogProgress(onDismiss :(Boolean) -> Unit){
        Dialog(onDismissRequest = { onDismiss(false)}, DialogProperties(dismissOnBackPress = false,dismissOnClickOutside = false)) {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
}