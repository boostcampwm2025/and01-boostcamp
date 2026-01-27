package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun ImageCountCard(
    currentImageIndex: Int,
    totalImageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(MemoripPadding.PaddingSmall)
            .clip(MemoripTheme.shapes.roundedMax)
            .background(MemoripTheme.colors.lightGray.copy(alpha = MemoripAlpha.BUTTON))
            .padding(
                horizontal = MemoripPadding.PaddingSmall,
                vertical = MemoripPadding.PaddingXSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(
            space = MemoripSpace.SpaceXXSmall,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_photo),
            contentDescription = null,
            modifier = Modifier.size(MemoripIconSize.IconSizeXSmall)
        )
        Text(
            text = stringResource(
                R.string.place_create_image_count_format,
                currentImageIndex + 1, totalImageCount
            ),
            style = MemoripTheme.typography.labelRegular12
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageCountCardPreview() {
    ImageCountCard(
        currentImageIndex = 3,
        totalImageCount = 5
    )
}