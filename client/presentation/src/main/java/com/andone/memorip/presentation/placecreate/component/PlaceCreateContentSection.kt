package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlaceCreateContentSection(
    title: String,
    content: String,
    isPublic: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCheckedChange: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.place_create_content_title),
            style = MemoripTheme.typography.title1
        )
        MemoripInputBox(
            label = stringResource(R.string.place_create_content_title),
            value = title,
            placeholder = stringResource(R.string.place_create_title_input),
            onValueChange = onTitleChange,
            onClear = { onTitleChange("") },
            height = MemoripHeight.TextBoxDefault
        )

        Text(
            text = stringResource(R.string.place_create_content),
            style = MemoripTheme.typography.title1
        )
        MemoripInputBox(
            label = stringResource(R.string.place_create_content),
            value = content,
            placeholder = stringResource(R.string.place_create_content_input),
            onValueChange = onContentChange,
            onClear = { onContentChange("") }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.place_create_open_to_everyone),
                style = MemoripTheme.typography.hint1
            )
            Checkbox(
                checked = isPublic,
                onCheckedChange = { onCheckedChange() }
            )
        }
    }
}

@Preview
@Composable
private fun PlaceCreateContentSectionPreview() {
    var title by remember { mutableStateOf(value = "") }
    var content by remember { mutableStateOf(value = "") }
    var isPublic by remember { mutableStateOf(value = false) }

    MemoripTheme {
        PlaceCreateContentSection(
            title = title,
            content = content,
            isPublic = isPublic,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            onCheckedChange = { isPublic = !isPublic }
        )
    }
}