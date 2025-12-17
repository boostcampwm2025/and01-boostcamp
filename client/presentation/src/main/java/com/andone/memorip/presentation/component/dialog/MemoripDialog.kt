package com.andone.memorip.presentation.component.dialog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme
import org.andone.memorip.presentation.theme.LocalMemoripTypography

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