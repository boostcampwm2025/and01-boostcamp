package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.loading_indicator_message),
                style = MemoripTheme.typography.label1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    MemoripTheme {
        LoadingIndicator()
    }
}