package com.andone.memorip.presentation.screen.triplist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.triplist.component.CardSpec.RATIO
import com.andone.memorip.presentation.theme.LocalMemoripTypography
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

private object CardSpec {
    const val RATIO = 5f / 3f
}

@Composable
fun EmptyTripPlaceholder(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = RATIO)
            .background(
                color = MemoripTheme.colors.primaryContainer,
                shape = memoripShapes.roundedSmall
            )
            .border(
                width = MemoripLineWidth.Thin,
                color = MemoripTheme.colors.primary,
                shape = memoripShapes.roundedSmall
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = MemoripTheme.colors.primary
            )

            Text(
                text = stringResource(R.string.trip_view_add_place),
                style = LocalMemoripTypography.current.bodyBold18,
                color = MemoripTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
private fun EmptyTripPlaceholderPreview() {
    MemoripTheme {
        EmptyTripPlaceholder(onClick = {})
    }
}

