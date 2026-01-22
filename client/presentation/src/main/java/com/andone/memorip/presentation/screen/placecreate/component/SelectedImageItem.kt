package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.placecreate.component.SelectedImageItemDimen.IMAGE_SIZE
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object SelectedImageItemDimen {
    val IMAGE_SIZE = 64.dp
}

@Composable
fun SelectedImageItem(
    imageUri: Any,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MemoripTheme.colors.primary else MemoripTheme.colors.gray

    Box(modifier = modifier.size(IMAGE_SIZE)) {
        MemoripImage(
            imageUrl = imageUri.toString(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = MemoripTheme.shapes.roundedSmall)
                .border(
                    width = MemoripLineWidth.Small,
                    color = borderColor,
                    shape = MemoripTheme.shapes.roundedSmall
                )
                .clickable(onClick = onClick)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(MemoripPadding.PaddingXXSmall)
                .size(MemoripIconSize.IconSizeSmall)
                .clip(CircleShape)
                .background(MemoripTheme.colors.gray.copy(alpha = MemoripAlpha.BUTTON))
                .clickable(onClick = onRemoveClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.place_delete_image),
                tint = MemoripTheme.colors.white,
                modifier = Modifier.size(MemoripIconSize.IconSizeXSmall)
            )
        }
    }
}

@Preview
@Composable
private fun SelectedImageItemPreview() {
    MemoripTheme {
        SelectedImageItem(
            imageUri = "",
            isSelected = true,
            onClick = {},
            onRemoveClick = {},
        )
    }
}