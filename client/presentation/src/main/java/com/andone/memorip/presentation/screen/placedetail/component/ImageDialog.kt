package com.andone.memorip.presentation.screen.placedetail.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import com.andone.memorip.presentation.component.MemoripImage

@Composable
fun ImageDialog(
    imageUrl: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        MemoripImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onDismissRequest
                ),
            contentScale = ContentScale.Fit
        )
    }
}