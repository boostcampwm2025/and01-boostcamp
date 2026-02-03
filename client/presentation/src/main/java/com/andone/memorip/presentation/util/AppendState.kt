package com.andone.memorip.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import com.andone.memorip.presentation.component.ErrorScreen
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.theme.MemoripPadding

fun LazyStaggeredGridScope.handleAppendState(
    appendState: LoadState,
    onRetry: () -> Unit
) {
    when (appendState) {
        is LoadState.Loading -> {
            item {
                LoadingIndicatorScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium)
                )
            }
        }

        is LoadState.Error -> {
            item {
                ErrorScreen(
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium),
                    message = appendState.error.localizedMessage
                )
            }
        }

        is LoadState.NotLoading -> {}
    }
}

fun LazyListScope.handleAppendState(
    appendState: LoadState,
    onRetry: () -> Unit
) {
    when (appendState) {
        is LoadState.Loading -> {
            item {
                LoadingIndicatorScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium)
                )
            }
        }

        is LoadState.Error -> {
            item {
                ErrorScreen(
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium),
                    message = appendState.error.localizedMessage
                )
            }
        }

        is LoadState.NotLoading -> {}
    }
}

fun LazyGridScope.handleAppendState(
    appendState: LoadState,
    onRetry: () -> Unit
) {
    when (appendState) {
        is LoadState.Loading -> {
            item {
                LoadingIndicatorScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium)
                )
            }
        }

        is LoadState.Error -> {
            item {
                ErrorScreen(
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MemoripPadding.PaddingMedium),
                    message = appendState.error.localizedMessage
                )
            }
        }

        is LoadState.NotLoading -> {}
    }
}
