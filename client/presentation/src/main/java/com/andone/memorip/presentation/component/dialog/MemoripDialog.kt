package com.andone.memorip.presentation.component.dialog

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

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
            color = MemoripTheme.colors.onSurface,
            style = MemoripTheme.typography.bodyMedium16
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MemoripDialogPreview() {
    MemoripTheme {
        MemoripDialog(
            title = "앱 확인 알림",
            content = "테스트하고자 생성한 Preview 입니다.\n어쩌고 저쩌고",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        )
    }
}