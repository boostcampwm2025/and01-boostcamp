package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun ErrorScreen(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
    retryButtonText: String = stringResource(R.string.errorfullscreen_retry_button),
) {
    val textMessage = message ?: stringResource(R.string.errorfullscreen_error_default_message)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = textMessage,
            color = MemoripTheme.colors.gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MemoripSpace.SpaceLarge))

        Button(onClick = onRetry) {
            Text(
                text = retryButtonText,
                color = MemoripTheme.colors.white,
                style = MemoripTheme.typography.label1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    MemoripTheme {
        ErrorScreen(
            onRetry = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenCustomPreview() {
    MemoripTheme {
        ErrorScreen(
            onRetry = {},
            message = "서버 연결에 실패했습니다\n잠시 후 다시 시도해주세요",
            retryButtonText = "새로고침",
        )
    }
}