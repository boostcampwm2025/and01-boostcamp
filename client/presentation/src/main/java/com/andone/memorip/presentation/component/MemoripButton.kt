package com.andone.memorip.presentation.component

import android.content.res.Configuration
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
    Card(
        onClick = onClick,
        enabled = enabled,
        shape = MemoripTheme.shapes.roundedMedium,
        colors = CardDefaults.cardColors(
            containerColor = MemoripTheme.colors.primary,
            contentColor = MemoripTheme.colors.white,
            disabledContainerColor = MemoripTheme.colors.lightGray,
            disabledContentColor = MemoripTheme.colors.gray
        )
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MemoripTheme.typography.labelBold16
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MemoripButtonPreview() {
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