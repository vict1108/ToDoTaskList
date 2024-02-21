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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.ui.theme.ToDoTypography

@Composable
fun AddTaskScreenComponent(
    modifier: Modifier,
    onBackClicked: () -> Unit,
    onAddClicked:() -> Unit,
    onDescriptionChange: (String) -> Unit,
    descritpion: String,
    toShowErrorDialog: Boolean,
    onDismissDialog: (Boolean) -> Unit
) {
    if (toShowErrorDialog) {
        DialogProgress(onDismiss = onDismissDialog)
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                value = descritpion,
                onValueChange = onDescriptionChange,
                label = {
                    Text(text = "Description", style = ToDoTypography.labelMedium)
                },
            )

            Text(
                text = "${descritpion.length}/100",
                style = ToDoTypography.labelMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )

        }

        Column(
            modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {

            Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                OutlinedButton(onClick = onBackClicked) {
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Cancel", style = ToDoTypography.labelMedium)
                    Spacer(modifier = modifier.width(10.dp))
                }

                Button(onClick = onAddClicked) {
                    Spacer(modifier = modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Text(
                        text = "Add", style = ToDoTypography.labelMedium
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
fun AddTaskScreenComponentPreview() {
    AddTaskScreenComponent(
        modifier = Modifier,
        onBackClicked = {  },
        onDescriptionChange = {},
        descritpion = "",
        toShowErrorDialog = false ,
        onDismissDialog = {},
        onAddClicked = {}
    )
}