package com.zoho.todo.ui.composeui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.ui.theme.ToDoTypography

@Composable
fun DetailScreenComponent(
    modifier: Modifier,
    onBackClicked: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUpdateClicked: () -> Unit,
    descritpion: String,
    toDoTaskEntity: ToDoTaskEntity?,
    onStatus: (Boolean) -> Unit,
    taskStatus: Boolean,
    toShowErrorDialog: Boolean,
    onDismissDialog: (Boolean) -> Unit,
    onAddClick: () -> Unit,
    onDelete: () -> Unit
) {

    var isEdit by remember {
        mutableStateOf(false)
    }

    if (toShowErrorDialog) {
        DialogProgress(onDismiss = onDismissDialog)
    }


    val openAlertDialog = remember { mutableStateOf(false) }


    if (openAlertDialog.value) {
        AlertDialogToDo(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                onDelete()
            },
            dialogTitle = "Delete Task",
            dialogText = "Are you sure you want delete this task?",
            icon = Icons.Filled.Delete
        )
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        //


        Column(
            modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = modifier.clickable(onClick = onBackClicked)
                )


                Button(onClick = onAddClick) {
                    Icon(Icons.Filled.AddCircle, contentDescription = null)
                    Text(text = " Add New Task")
                }
            }



            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                value = if (isEdit) descritpion else toDoTaskEntity?.todo ?: "",
                onValueChange = onDescriptionChange,
                label = {
                    Text(text = "Description", style = ToDoTypography.labelMedium)
                },
                enabled = isEdit
            )

            Text(
                text = "${if (isEdit) descritpion.length else toDoTaskEntity?.todo?.length ?: 0}/100",
                style = ToDoTypography.labelMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Status :", style = ToDoTypography.labelMedium)
                Checkbox(
                    checked = if (isEdit) taskStatus else toDoTaskEntity?.completed ?: false,
                    onCheckedChange = onStatus,
                    enabled = isEdit
                )
            }



            Button(onClick = {
                openAlertDialog.value = true
            }, modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                Text(text = "Delete")
            }

        }

        Column(
            modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {

            Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                OutlinedButton(onClick = {
                    isEdit = false
                }) {
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Cancel", style = ToDoTypography.labelMedium)
                    Spacer(modifier = modifier.width(10.dp))
                }

                Button(onClick = {
                    isEdit = if (isEdit) {
                        onUpdateClicked()
                        false
                    } else {
                        onStatus(toDoTaskEntity?.completed ?: false)
                        onDescriptionChange(toDoTaskEntity?.todo ?: "")
                        true
                    }

                }) {
                    Spacer(modifier = modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Text(
                        text = if (isEdit) "Update" else "Edit", style = ToDoTypography.labelMedium
                    )
                    Spacer(modifier = modifier.width(10.dp))
                }


            }


            Spacer(modifier = modifier.height(20.dp))

        }
    }

}


@Composable
@Preview(showBackground = true, device = "id:pixel_4")
fun DetailScreenComponentPreview() {
    DetailScreenComponent(Modifier, {

    }, {

    }, {

    }, "", ToDoTaskEntity(), {}, false, false, {

    }, {

    }, onDelete = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogToDo(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("No")
            }
        }
    )
}