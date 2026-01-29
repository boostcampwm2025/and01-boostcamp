package com.andone.memorip.presentation.component.dialog

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun DeleteDialog(
    title: String,
    content: String,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DefaultDialog(
        title = title,
        onConfirmClick = onDeleteClick,
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        confirmButtonText = stringResource(R.string.dialog_delete_message)
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
private fun DeleteDialogPreview() {
    MemoripTheme {
        DeleteDialog(
            title = "장소 삭제",
            content = "정말 삭제하시겠습니까?",
            onDeleteClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        )
    }
}
