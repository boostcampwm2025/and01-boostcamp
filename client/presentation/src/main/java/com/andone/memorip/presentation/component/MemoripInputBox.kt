package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MemoripInputBox(
    value: String,
    valueMaxLength: Int,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MemoripTheme.typography.bodyMedium,
    singleLine: Boolean = false,
    showValueLength: Boolean = true,
    height: Dp = MemoripHeight.TextBoxHigh
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= valueMaxLength) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            textStyle = textStyle,
            placeholder = {
                Text(
                    text = placeholder,
                    style = textStyle,
                    color = MemoripTheme.colors.gray1
                )
            },
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MemoripTheme.colors.transparent,
                unfocusedBorderColor = MemoripTheme.colors.transparent
            )
        )

        if (showValueLength) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(
                        R.string.place_create_value_length_format,
                        value.length, valueMaxLength
                    ),
                    color = MemoripTheme.colors.gray1,
                    style = MemoripTheme.typography.caption1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripInputBoxPreview() {
    var text by remember { mutableStateOf("test") }

    MemoripTheme {
        Column {
            MemoripInputBox(
                value = text,
                valueMaxLength = 30,
                placeholder = "Input",
                onValueChange = { text = it },
                textStyle = MemoripTheme.typography.body2,
                singleLine = true,
                showValueLength = false,
                height = OutlinedTextFieldDefaults.MinHeight
            )
            MemoripInputBox(
                value = "",
                valueMaxLength = 300,
                placeholder = "Input",
                onValueChange = { text = it },
            )
        }
    }
}