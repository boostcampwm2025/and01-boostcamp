package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object MemoripSnackbarDimen {
    val MAX_WIDTH = 400.dp
    val MIN_HEIGHT = 80.dp
    val SNACKBAR_PADDING = 12.dp
}

@Composable
fun MemoripSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MemoripPadding.AppHorizontalPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = hostState) { snackbarData ->
            MemoripSnackbarContent(
                message = snackbarData.visuals.message,
                actionLabel = snackbarData.visuals.actionLabel,
                onActionClick = { snackbarData.performAction() }
            )
        }
    }
}

@Composable
private fun MemoripSnackbarContent(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
) {
    Snackbar(
        modifier = modifier
            .widthIn(max = MemoripSnackbarDimen.MAX_WIDTH)
            .heightIn(min = MemoripSnackbarDimen.MIN_HEIGHT)
            .padding(MemoripSnackbarDimen.SNACKBAR_PADDING),
        action = actionLabel?.let {
            {
                TextButton(onClick = onActionClick) {
                    Text(
                        text = it,
                        color = MemoripTheme.colors.primary,
                        style = MemoripTheme.typography.bodyBold14
                    )
                }
            }
        },
        shape = MemoripTheme.shapes.roundedMedium,
        containerColor = MemoripTheme.colors.gray,
        contentColor = MemoripTheme.colors.white,
        dismissActionContentColor = MemoripTheme.colors.white,
        content = {
            Text(
                text = message,
                style = MemoripTheme.typography.bodyRegular12
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MemoripSnackbarPreview() {
    MemoripTheme {
        MemoripSnackbarContent(
            message = "장소 가져오기에 성공했어요",
            actionLabel = "확인",
            onActionClick = {}
        )
    }
}
