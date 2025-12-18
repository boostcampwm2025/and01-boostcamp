package com.andone.memorip.presentation.component.dialog

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MemoripInputDialog(
    title: String,
    onConfirmClick: (value: String) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    label: String? = null
) {
    var value by remember { mutableStateOf("") }

    DefaultDialog(
        title = title,
        onConfirmClick = { onConfirmClick(value) },
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        confirmEnabled = value.isNotEmpty(),
    ) {
        TextField(
            value = value,
            onValueChange = { value = it },
            label = {
                if (label != null) {
                    Text(text = label)
                }
            },
            placeholder = {
                if (hint != null) {
                    Text(text = hint)
                }
            },
            trailingIcon = {
                IconButton(onClick = { value = "" }) {
                    Icon(
                        tint = MemoripTheme.colors.black,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.dialog_close_button_description)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MemoripTheme.colors.primaryContainer,
                unfocusedContainerColor = MemoripTheme.colors.primaryContainer,
                focusedTextColor = MemoripTheme.colors.black,
                unfocusedTextColor = MemoripTheme.colors.black,
                focusedPlaceholderColor = MemoripTheme.colors.outline,
                unfocusedPlaceholderColor = MemoripTheme.colors.outline,
                focusedLabelColor = MemoripTheme.colors.outline,
                unfocusedLabelColor = MemoripTheme.colors.outline,
            )
        )
    }
}

@Preview
@Composable
private fun MemoripInputDialogPrev() {
    MemoripTheme {
        MemoripInputDialog(
            title = "그룹 추가",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {},
            hint = "input",
            label = "이름"
        )
    }
}