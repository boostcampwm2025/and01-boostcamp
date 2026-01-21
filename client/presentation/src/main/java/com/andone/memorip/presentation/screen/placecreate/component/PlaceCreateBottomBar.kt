package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.MemoripButton
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlaceCreateBottomBar(
    value: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HorizontalDivider(color = MemoripTheme.colors.gray2)
        Column(
            modifier = Modifier.padding(
                horizontal = MemoripPadding.PaddingMedium,
                vertical = MemoripPadding.PaddingXLarge
            )
        ) {
            MemoripButton(
                value = value,
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MemoripPadding.PaddingMedium),
                enabled = enabled
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceCreateBottomBarPreview() {
    PlaceCreateBottomBar(
        value = "test",
        onClick = {},
        enabled = true
    )
}