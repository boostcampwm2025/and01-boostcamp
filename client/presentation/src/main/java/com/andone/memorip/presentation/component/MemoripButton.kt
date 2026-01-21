package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MemoripButton(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val textColor = if (enabled) MemoripTheme.colors.white else MemoripTheme.colors.gray

    Card(
        onClick = onClick,
        enabled = enabled,
        shape = MemoripTheme.shapes.roundedMedium,
        colors = CardDefaults.cardColors(containerColor = MemoripTheme.colors.primary)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                color = textColor,
                style = MemoripTheme.typography.title2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoripButtonPreview() {
    MemoripTheme {
        Column {
            MemoripButton(
                value = "test",
                onClick = {}
            )
            MemoripButton(
                value = "test",
                onClick = {},
                enabled = false
            )
        }
    }
}