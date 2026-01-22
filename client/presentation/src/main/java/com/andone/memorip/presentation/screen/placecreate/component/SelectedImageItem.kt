package com.andone.memorip.presentation.screen.placecreate.component

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
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R

private object IconOffset {
    val DeleteIcon = DpOffset(x = 1.dp, y = (-1).dp)
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
                    width = MemoripLineWidth.Thin,
                    color = MemoripTheme.colors.gray,
                    shape = MemoripTheme.shapes.roundedSmall
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(data = imageUri)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = MemoripTheme.shapes.roundedSmall)
            )
        }

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(size = MemoripIconSize.IconSizeMedium)
                .offset(
                    x = IconOffset.DeleteIcon.x,
                    y = IconOffset.DeleteIcon.y
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