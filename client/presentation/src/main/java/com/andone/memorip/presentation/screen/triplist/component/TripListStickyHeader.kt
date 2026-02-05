package com.andone.memorip.presentation.screen.triplist.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object TripListStickyHeaderDimen {
    const val ANIMATION_DURATION = 300
}

@Composable
fun TripListStickyHeader(
    searchQuery: String,
    isStickyHeaderVisible: Boolean,
    onCreateNewTripClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isStickyHeaderVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = TripListStickyHeaderDimen.ANIMATION_DURATION)),
        exit = fadeOut(animationSpec = tween(durationMillis = TripListStickyHeaderDimen.ANIMATION_DURATION)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MemoripTheme.colors.background)
        ) {
            TripListSearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.padding(bottom = MemoripPadding.PaddingSmall)
            )
            CreateNewTripCard(
                onClick = onCreateNewTripClick,
                modifier = Modifier.padding(bottom = MemoripPadding.PaddingSmall)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TripListStickyHeaderPreview() {
    MemoripTheme {
        TripListStickyHeader(
            searchQuery = "",
            isStickyHeaderVisible = true,
            onCreateNewTripClick = {},
            onSearchQueryChange = {}
        )
    }
}

@Preview(showBackground = true, name = "Hidden State")
@Composable
private fun TripListStickyHeaderHiddenPreview() {
    MemoripTheme {
        TripListStickyHeader(
            searchQuery = "",
            isStickyHeaderVisible = false,
            onCreateNewTripClick = {},
            onSearchQueryChange = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripListStickyHeaderDarkPreview() {
    MemoripTheme {
        TripListStickyHeader(
            searchQuery = "",
            isStickyHeaderVisible = true,
            onCreateNewTripClick = {},
            onSearchQueryChange = {}
        )
    }
}
