package com.zoho.todo.ui.composeui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoho.todo.R
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.ui.theme.ToDoTypography


@Composable
fun TaskComponentItem(modifier: Modifier,toDoTaskEntity: ToDoTaskEntity,onClick:(Int) -> Unit) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp).clickable(onClick = {
                onClick(toDoTaskEntity.id ?: 0)
            }),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(modifier.padding(24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween){
            Row {
                Checkbox(
                    checked = toDoTaskEntity.completed ?: false,
                    onCheckedChange = null,
                    enabled = false,
                    modifier = modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = toDoTaskEntity.todo ?: "",
                    modifier.fillMaxWidth(0.9f).align(Alignment.CenterVertically),
                    style = ToDoTypography.titleMedium,
                    maxLines = 2
                )
            }
            Icon(
                painter = if (toDoTaskEntity.isUploaded == true) painterResource(id = R.drawable.baseline_cloud_done_24) else painterResource(id = R.drawable.baseline_cloud_off_24) ,
                contentDescription = null,
                tint = Color.Black.copy(0.5f),
                modifier = modifier.align(Alignment.CenterVertically),
            )
        }

    }


}


@Composable
@Preview(showBackground = true, device = "id:pixel_4")
fun TaskComponentItemPreview() {
    TaskComponentItem(Modifier, ToDoTaskEntity(
        todo = "Contribute code or a monetary donation to an open-source software project"
    )){

    }
}