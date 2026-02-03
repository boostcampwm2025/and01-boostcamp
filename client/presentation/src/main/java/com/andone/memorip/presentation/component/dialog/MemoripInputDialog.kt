package com.andone.memorip.presentation.component.dialog

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MemoripInputDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onConfirmClick: (value: String) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    label: String? = null,
    maxLength: Int? = null,
) {
    val supportingText: (@Composable () -> Unit)? = when {
        maxLength != null -> {
            {
                Text(
                    text = stringResource(
                        R.string.input_dialog_char_count_format,
                        value.length,
                        maxLength
                    )
                )
            }
        }

        else -> null
    }

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
            onValueChange = onValueChange,
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
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.dialog_close_button_description),
                        tint = MemoripTheme.colors.onSurface
                    )
                }
            },
            supportingText = supportingText,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MemoripTheme.colors.primaryContainer,
                unfocusedContainerColor = MemoripTheme.colors.primaryContainer,
                focusedTextColor = MemoripTheme.colors.black,
                unfocusedTextColor = MemoripTheme.colors.black,
                focusedPlaceholderColor = MemoripTheme.colors.primary,
                unfocusedPlaceholderColor = MemoripTheme.colors.primary,
                focusedLabelColor = MemoripTheme.colors.primary,
                unfocusedLabelColor = MemoripTheme.colors.primary,
                errorSupportingTextColor = MemoripTheme.colors.error,
                errorLabelColor = MemoripTheme.colors.error,
            )
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MemoripInputDialogPreview() {
    var value by remember { mutableStateOf("") }
    MemoripTheme {
        MemoripInputDialog(
            value = value,
            onValueChange = { value = it },
            title = "그룹 추가",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {},
            hint = stringResource(R.string.select_trip_dialog_preview_hint),
            label = stringResource(R.string.select_trip_dialog_preview_label),
            maxLength = 20
        )
    }
}
