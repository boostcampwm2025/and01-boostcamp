package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.util.handleAppendState
import kotlinx.coroutines.flow.flowOf

@Composable
fun <T : Any> MemoripPagingList(
    query: String,
    pagingItems: LazyPagingItems<T>,
    itemKey: (T) -> Any,
    modifier: Modifier = Modifier,
    staggeredCells: StaggeredGridCells? = null,
    initialContent: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val loadState = pagingItems.loadState

    if (query.isEmpty()) {
        initialContent()
        return
    }

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
            if (refreshState is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                pagingItems.itemCount == 0
            ) {
                emptyContent()
            } else {
                staggeredCells?.let { columns ->
                    LazyVerticalStaggeredGrid(
                        columns = columns,
                        modifier = modifier,
                        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
                        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
                    ) {
                        items(
                            count = pagingItems.itemCount,
                            key = pagingItems.itemKey { itemKey(it) }
                        ) { index ->
                            pagingItems[index]?.let { item ->
                                itemContent(item)
                            }
                        }

                        handleAppendState(loadState.append, pagingItems::retry)
                    }
                } ?: run {
                    LazyColumn(modifier = modifier) {
                        items(
                            count = pagingItems.itemCount,
                            key = pagingItems.itemKey { itemKey(it) }
                        ) { index ->
                            pagingItems[index]?.let { item ->
                                itemContent(item)
                            }
                        }

                        handleAppendState(loadState.append, pagingItems::retry)
                    }
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
        query = "",
        pagingItems = dummyPagingItems,
        itemKey = { "" },
        modifier = Modifier.fillMaxSize(),
        initialContent = {},
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