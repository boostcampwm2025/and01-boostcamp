package com.andone.memorip.presentation.placedetail.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage

@Composable
fun ImageDialog(
    imageUrl: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onDismissRequest
                )
        )
    }
}