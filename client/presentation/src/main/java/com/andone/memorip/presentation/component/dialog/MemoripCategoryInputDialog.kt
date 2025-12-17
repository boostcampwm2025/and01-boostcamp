package com.andone.memorip.presentation.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.DialogConstants.MAX_LENGTH
import com.andone.memorip.presentation.component.dialog.DialogDimens.LEADING_ICON_SIZE
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.rememberColorState

private object DialogConstants {
    val MAX_LENGTH = 6
}

@Composable
fun MemoripCategoryInputDialog(
    title: String,
    onConfirmClick: (name: String, color: Color) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val colorState = rememberColorState()

    DefaultDialog(
        title = title,
        onConfirmClick = { onConfirmClick(name, colorState.color) },
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
        confirmEnabled = name.isNotEmpty() && colorState.isValidColorInput()
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            textStyle = MemoripTheme.typography.body2,
            label = {
                Text(text = stringResource(R.string.dialog_name_place_holder))
            },
            placeholder = {
                Text(text = stringResource(R.string.dialog_name_place_holder))
            },
            trailingIcon = {
                IconButton(onClick = { name = "" }) {
                    Icon(
                        tint = MemoripTheme.colors.black,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.dialog_close_button_description)
                    )
                }
            },
            supportingText = {
                if (name.isEmpty()) {
                    Text(
                        text = stringResource(R.string.category_dialog_empty_name_err_hint),
                        style = MemoripTheme.typography.hint1
                    )
                }
            },
            isError = name.isEmpty(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MemoripTheme.colors.primaryContainer,
                unfocusedContainerColor = MemoripTheme.colors.primaryContainer,
                errorContainerColor = MemoripTheme.colors.primaryContainer,
                focusedTextColor = MemoripTheme.colors.black,
                unfocusedTextColor = MemoripTheme.colors.black,
                focusedPlaceholderColor = MemoripTheme.colors.outline,
                unfocusedPlaceholderColor = MemoripTheme.colors.outline,
                focusedLabelColor = MemoripTheme.colors.outline,
                unfocusedLabelColor = MemoripTheme.colors.outline,
                errorTextColor = MemoripTheme.colors.error,
                errorSupportingTextColor = MemoripTheme.colors.error
            )
        )
        TextField(
            value = stringResource(R.string.category_dialog_color_format,colorState.inputColor),
            onValueChange = { value ->
                if (value.length <= MAX_LENGTH + 1) { colorState.updateColor(value) }
            },
            textStyle = MemoripTheme.typography.body2,
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(LEADING_ICON_SIZE)
                        .background(
                            color = colorState.color,
                            shape = MemoripTheme.shapes.defaultCorner
                        )
                )
            },
            trailingIcon = {
                IconButton(
                    enabled = colorState.inputColor.length == MAX_LENGTH,
                    onClick = { colorState.refreshColor() }
                ) {
                    Icon(
                        tint = MemoripTheme.colors.black,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                        contentDescription = stringResource(R.string.dialog_refresh_button_description)
                    )
                }
            },
            supportingText = {
                if (!colorState.isValidColorInput()) {
                    Text(
                        text = stringResource(colorState.getErrMsg()),
                        style = MemoripTheme.typography.hint1
                    )
                }
            },
            isError = !colorState.isValidColorInput(),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MemoripTheme.colors.primaryContainer,
                unfocusedContainerColor = MemoripTheme.colors.primaryContainer,
                errorContainerColor = MemoripTheme.colors.primaryContainer,
                focusedTextColor = MemoripTheme.colors.black,
                unfocusedTextColor = MemoripTheme.colors.black,
                errorTextColor = MemoripTheme.colors.error,
                errorSupportingTextColor = MemoripTheme.colors.error
            )
        )
    }
}

@Preview
@Composable
private fun MemoripCategoryInputDialogPrev() {
    MemoripTheme(darkTheme = false) {
        MemoripCategoryInputDialog(
            title = "카테고리 입력",
            onConfirmClick = { _, _ -> },
            onCancelClick = {},
            onDismissRequest = {}
        )
    }
}