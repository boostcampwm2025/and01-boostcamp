package com.andone.memorip.presentation.screen.selectimage.component

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun ImageCropBottomBar(
    imageUris: List<Uri>,
    croppedImageKeys: Set<Uri>,
    currentIndex: Int,
    onDismiss: () -> Unit,
    onClickImage: (Int) -> Unit,
    onImageCrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = MemoripSpace.SpaceMedium,
                vertical = MemoripSpace.SpaceSmall
            ),
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
        ) {
            itemsIndexed(
                items = imageUris,
                key = { _, uri -> uri }
            ) { index, uri ->
                ThumbnailItem(
                    imageUri = uri,
                    isSelected = index == currentIndex,
                    isDone = croppedImageKeys.contains(uri),
                    onClick = { onClickImage(index) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MemoripPadding.PaddingLarge,
                    vertical = MemoripPadding.PaddingMedium
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MemoripTheme.colors.gray)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.select_image_cancel),
                    tint = MemoripTheme.colors.white
                )
            }

            Text(
                text = stringResource(
                    R.string.select_image_image_count_format,
                    croppedImageKeys.size,
                    imageUris.size
                )
            )

            val iconRes =
                if (imageUris.size == croppedImageKeys.size) R.drawable.ic_arrow_forward else R.drawable.ic_check
            IconButton(
                onClick = onImageCrop,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MemoripTheme.colors.primary)
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = stringResource(R.string.select_image_done),
                    tint = MemoripTheme.colors.white
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageCropBottomBarPreview() {
    ImageCropBottomBar(
        imageUris = emptyList(),
        croppedImageKeys = emptySet(),
        currentIndex = 0,
        onDismiss = {},
        onClickImage = {},
        onImageCrop = {}
    )
}