package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripHeight

@Composable
fun MemoripInputBox(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = MemoripHeight.TextBoxDefault
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = MemoripBorderWidth.Strong,
                color = MemoripTheme.colors.primary,
                shape = MemoripTheme.shapes.defaultCorner
            )
            .background(
                color = MemoripTheme.colors.offWhite,
                shape = MemoripTheme.shapes.defaultCorner
            )
            .padding(all = MemoripPadding.PaddingSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = MemoripTheme.colors.black,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (value.isNotEmpty()) {
                IconButton(
                    onClick = onClear,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.place_create_clear),
                        tint = MemoripTheme.colors.gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(height = MemoripPadding.PaddingXSmall))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = height),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MemoripTheme.typography.body2
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripInputBoxPreview() {
    var text by remember { mutableStateOf("test") }

    MemoripTheme {
        MemoripInputBox(
            label = "내용",
            value = text,
            placeholder = "Input",
            onValueChange = { text = it },
            onClear = { text = "" }
        )
    }
}