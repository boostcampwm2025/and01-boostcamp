package com.andone.memorip.presentation.placecreate.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
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
}

@Preview
@Composable
private fun PlaceCreateContentSectionPreview(){
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    MemoripTheme {
        PlaceCreateContentSection(
            title = title,
            content = content,
            onTitleChange = {title = it},
            onContentChange = {content = it}
        )
    }
}