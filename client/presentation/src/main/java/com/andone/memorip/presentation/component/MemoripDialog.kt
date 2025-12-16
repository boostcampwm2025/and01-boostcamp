package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.DialogConstants.MAX_LENGTH
import com.andone.memorip.presentation.component.DialogDimens.INNER_PADDING
import com.andone.memorip.presentation.component.DialogDimens.LEADING_ICON_SIZE
import com.andone.memorip.presentation.component.DialogDimens.SPACING
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.LocalMemoripShapes
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.utils.rememberColorState
import org.andone.memorip.presentation.theme.LocalMemoripTypography

private object DialogDimens {
    val INNER_PADDING = 24.dp
    val SPACING = 8.dp
    val LEADING_ICON_SIZE = 36.dp
}

object DialogConstants {
    val MAX_LENGTH = 6
}

@Composable
fun MemoripDialog(
    title: String,
    content: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DefaultDialog(
        modifier = modifier,
        title = title,
        onConfirmClick = onConfirmClick,
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest
    ) {
        Text(
            text = content,
            style = LocalMemoripTypography.current.body1
        )
    }
}

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
        modifier = modifier,
        title = title,
        onConfirmClick = { onConfirmClick(value) },
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
    ) {
        TextField(
            value = value,
            onValueChange = { value = it },
            placeholder = {
                if (hint != null) {
                    Text(text = hint)
                }
            },
            label = {
                if (label != null) {
                    Text(text = label)
                }
            },
            trailingIcon = {
                IconButton(onClick = { value = "" }) {
                    Icon(
                        tint = LocalMemoripColors.current.black,
                        imageVector = ImageVector.vectorResource(R.drawable.close_ic),
                        contentDescription = ""
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
    }
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
                        imageVector = ImageVector.vectorResource(R.drawable.close_ic),
                        contentDescription = ""
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
                if (value.length <= MAX_LENGTH + 1) {
                    colorState.updateColor(value)
                }
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
                        imageVector = ImageVector.vectorResource(R.drawable.refresh_ic),
                        contentDescription = ""
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

@Composable
private fun DefaultDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            contentColor = LocalMemoripColors.current.black,
            color = LocalMemoripColors.current.primaryContainer,
            shape = LocalMemoripShapes.current.largeCorner
        ) {
            Column(
                modifier = Modifier.padding(horizontal = INNER_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = SPACING)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = INNER_PADDING),
                    text = title,
                    style = LocalMemoripTypography.current.headline2
                )
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = INNER_PADDING),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = SPACING,
                        alignment = Alignment.End
                    )
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text(
                            text = stringResource(R.string.dialog_cancel_message),
                            style = LocalMemoripTypography.current.label1,
                            color = LocalMemoripColors.current.primary
                        )
                    }
                    TextButton(onClick = onConfirmClick) {
                        Text(
                            text = stringResource(R.string.dialog_confirm_message),
                            style = LocalMemoripTypography.current.label1,
                            color = LocalMemoripColors.current.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultDialogPrev() {
    MemoripTheme(darkTheme = false) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            DefaultDialog(
                title = "앱 확인 알림",
                onConfirmClick = {},
                onCancelClick = {},
                onDismissRequest = {}
            ) {
                Text(
                    text = "테스트하고자 생성한 Preview 입니다.\n어쩌고 저쩌고",
                    style = LocalMemoripTypography.current.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun MemoripDialogPrev() {
    MemoripTheme(darkTheme = false) {
        MemoripDialog(
            title = "앱 확인 알림",
            content = "테스트하고자 생성한 Preview 입니다.\n어쩌고 저쩌고",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun MemoripInputDialogPrev() {
    MemoripTheme {
        MemoripInputDialog(
            title = "그룹 추가",
            hint = "input",
            label = "이름",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {}
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