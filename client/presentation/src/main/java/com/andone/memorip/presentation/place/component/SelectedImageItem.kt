package com.andone.memorip.presentation.place.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding

private object MemoripOffset {
    val DeleteIcon = DpOffset(1.dp, (-1).dp)
}


@Composable
fun SelectedImageItem(
    imageUri: Any,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(MemoripIconSize.IconButton)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = MemoripBorderWidth.Thin,
                    color = MemoripTheme.colors.gray,
                    shape = MemoripTheme.shapes.roundedSmall
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MemoripTheme.shapes.roundedSmall)
            )
        }

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(size = MemoripIconSize.IconSizeMedium)
                .offset(
                    x = MemoripOffset.DeleteIcon.x,
                    y = MemoripOffset.DeleteIcon.y
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_cancel_24),
                contentDescription = stringResource(R.string.place_delete_image),
                tint = MemoripTheme.colors.error
            )
        }
    }
}

@Preview
@Composable
private fun SelectedImageItemPreview(){
    MemoripTheme {
        SelectedImageItem(
            imageUri = "",
            onRemoveClick = {},
        )
    }
}