package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
    itemContent: @Composable (T) -> Unit
) {
    val loadState = pagingItems.loadState
    val refreshState = loadState.refresh

    if (pagingItems.itemCount > 0) {
        staggeredCells?.let { columns ->
            LazyVerticalStaggeredGrid(
                columns = columns,
                modifier = modifier,
                verticalItemSpacing = MemoripSpace.SpaceSmall,
                horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
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
        return
    }

    when (refreshState) {
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

        is LoadState.NotLoading -> {
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