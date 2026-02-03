package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.handleAppendState

@Composable
fun <T : Any> MemoripPagingList(
    pagingItems: LazyPagingItems<T>,
    itemKey: (T) -> Any,
    emptyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    staggeredCells: StaggeredGridCells? = null,
    useHorizontalGrid: Boolean = false,
    horizontalGridRows: Int = 3,
    itemContent: @Composable (T) -> Unit
) {
    val loadState = pagingItems.loadState
    val isRefreshing =
        loadState.mediator?.refresh is LoadState.Loading || loadState.refresh is LoadState.Loading
    val errorState =
        loadState.mediator?.refresh as? LoadState.Error ?: loadState.refresh as? LoadState.Error

    if (pagingItems.itemCount > 0) {
        staggeredCells?.let { columns ->
            LazyVerticalStaggeredGrid(
                columns = columns,
                modifier = modifier,
                verticalItemSpacing = MemoripSpace.SpaceXXSmall,
                horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { itemKey(it) }
                ) { index ->
                    pagingItems[index]?.let { itemContent(it) }
                }

                handleAppendState(loadState.append, pagingItems::retry)
            }
        } ?: run {
            if (useHorizontalGrid) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(horizontalGridRows),
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall),
                    verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { itemKey(it) }
                    ) { index ->
                        pagingItems[index]?.let { itemContent(it) }
                    }

                    handleAppendState(loadState.append, pagingItems::retry)
                }
            } else {
                LazyColumn(modifier = modifier) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { itemKey(it) }
                    ) { index ->
                        pagingItems[index]?.let { itemContent(it) }
                    }

                    handleAppendState(loadState.append, pagingItems::retry)
                }
            }
        }
        return
    }

    when {
        isRefreshing -> {
            LoadingIndicatorScreen(modifier = Modifier.fillMaxSize())
        }

        errorState != null -> {
            ErrorScreen(
                onRetry = { pagingItems.retry() },
                modifier = Modifier.fillMaxSize(),
                message = errorState.error.localizedMessage
            )
        }

        loadState.append.endOfPaginationReached && pagingItems.itemCount == 0 -> {
            emptyContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripPagingLocationsListPreview() {
    MemoripPagingList(
        pagingItems = DummyData.getPlacePagingItems(),
        itemKey = { it.id },
        emptyContent = {},
        modifier = Modifier.fillMaxSize(),
        itemContent = { Text(text = it.name) }
    )
}