package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.selectlocation.component.LocationItem
import com.andone.memorip.presentation.theme.MemoripPadding
import kotlinx.coroutines.flow.flowOf

@Composable
fun <T : Any> MemoripPagingList(
    pagingItems: LazyPagingItems<T>,
    itemKey: (T) -> Any,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val loadState = pagingItems.loadState

    when (val refreshState = loadState.refresh) {
        is LoadState.Loading -> {
            LoadingIndicatorScreen(modifier = Modifier.fillMaxSize())
        }

        is LoadState.Error -> {
            ErrorScreen(
                onRetry = { pagingItems.retry() },
                modifier = Modifier.fillMaxSize(),
                message = refreshState.error.localizedMessage
            )
        }

        else -> {
            LazyColumn(modifier = modifier) {
                if (refreshState is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    pagingItems.itemCount == 0
                ) {
                    item { emptyContent() }
                } else {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { itemKey(it) }
                    ) { index ->
                        pagingItems[index]?.let { item ->
                            itemContent(item)
                        }
                    }
                }

                when (val appendState = loadState.append) {
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
                                onRetry = { pagingItems.retry() },
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoripPagingListPreview() {
    val dummyList = listOf(
        LocationUiModel(
            id = "",
            name = "",
            category = "",
            address = "",
            roadAddress = "",
            latitude = 0.0,
            longitude = 0.0
        )
    )
    val dummyPagingItems = flowOf(PagingData.from(dummyList)).collectAsLazyPagingItems()

    MemoripPagingList(
        pagingItems = dummyPagingItems,
        itemKey = { "" },
        modifier = Modifier.fillMaxSize(),
        emptyContent = {},
        itemContent = { location ->
            LocationItem(
                location = location,
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}