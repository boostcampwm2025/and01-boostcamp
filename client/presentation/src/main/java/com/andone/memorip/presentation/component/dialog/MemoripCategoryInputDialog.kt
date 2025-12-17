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
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.utils.rememberColorState
import org.andone.memorip.presentation.theme.LocalMemoripTypography

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
        onDismissRequest = onDismissRequest
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(text = stringResource(R.string.dialog_name_place_holder))
            },
            label = {
                Text(text = stringResource(R.string.dialog_name_place_holder))
            },
            textStyle = LocalMemoripTypography.current.body2,
            trailingIcon = {
                IconButton(onClick = { name = "" }) {
                    Icon(
                        tint = LocalMemoripColors.current.black,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.dialog_close_button_description)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LocalMemoripColors.current.primaryContainer,
                unfocusedContainerColor = LocalMemoripColors.current.primaryContainer,
                focusedTextColor = LocalMemoripColors.current.black,
                unfocusedTextColor = LocalMemoripColors.current.black,
                focusedPlaceholderColor = LocalMemoripColors.current.outline,
                unfocusedPlaceholderColor = LocalMemoripColors.current.outline,
                focusedLabelColor = LocalMemoripColors.current.outline,
                unfocusedLabelColor = LocalMemoripColors.current.outline,
            )
        )
        TextField(
            value = stringResource(
                R.string.category_dialog_color_format,
                colorState.inputColor
            ),
            onValueChange = { value ->
                if (value.length <= MAX_LENGTH + 1) { colorState.updateColor(value) }
            },
            textStyle = LocalMemoripTypography.current.body2,
            maxLines = 1,
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
            isError = !colorState.isValidColorInput(),
            supportingText = {
                if (!colorState.isValidColorInput()) {
                    Text(
                        text = stringResource(colorState.getErrMsg()),
                        style = LocalMemoripTypography.current.hint1
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    enabled = colorState.inputColor.length == MAX_LENGTH,
                    onClick = { colorState.refreshColor() }
                ) {
                    Icon(
                        tint = LocalMemoripColors.current.black,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                        contentDescription = stringResource(R.string.dialog_refresh_button_description)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LocalMemoripColors.current.primaryContainer,
                unfocusedContainerColor = LocalMemoripColors.current.primaryContainer,
                errorContainerColor = LocalMemoripColors.current.primaryContainer,
                focusedTextColor = LocalMemoripColors.current.black,
                unfocusedTextColor = LocalMemoripColors.current.black,
                errorTextColor = LocalMemoripColors.current.error,
                errorSupportingTextColor = LocalMemoripColors.current.error
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