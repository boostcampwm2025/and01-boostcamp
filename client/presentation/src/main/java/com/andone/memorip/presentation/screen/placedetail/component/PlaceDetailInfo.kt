package com.andone.memorip.presentation.screen.placedetail.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlaceDetailInfoSection(
    infoString: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null
        )
        Text(
            text = infoString,
            style = MemoripTheme.typography.labelLarge
        )
    }
}