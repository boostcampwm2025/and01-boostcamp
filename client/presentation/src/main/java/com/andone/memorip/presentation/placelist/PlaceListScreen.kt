package com.andone.memorip.presentation.placelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.GroupView
import com.andone.memorip.presentation.placelist.model.GroupUiModel
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun PlaceListScreen(
    onGroupClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is PlaceListEvent.NavigateToGroupDetail -> {
                onGroupClick(event.groupId)
            }

            is PlaceListEvent.NavigateToPlaceCreate -> {
                onCreatePlaceClick()
            }

            PlaceListEvent.ShowSnackBar -> {
                // TODO: 설정 필요
            }
        }
    }

    PlaceListScreenContents(
        groups = uiState.groups,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    groups: List<GroupUiModel>,
    onAction: (PlaceListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = stringResource(R.string.place_list_search_content_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MemoripTheme.colors.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(PlaceListAction.OnFABClick) },
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.place_list_add_content_description)
                )
            }
        },
        contentWindowInsets = WindowInsets(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(horizontal = MemoripPadding.PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingXXXSmall)
        ) {
            items(items = groups) { group ->
                GroupView(
                    name = group.name,
                    onGroupClick = { onAction(PlaceListAction.OnGroupClick("")) },
                    onAddClick = { onAction(PlaceListAction.OnGroupClick("")) },
                    images = group.images
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PlaceListScreenContentsPreview() {
    MemoripTheme {
        PlaceListScreenContents(
            groups = DummyData.groups,
            onAction = {}
        )
    }
}