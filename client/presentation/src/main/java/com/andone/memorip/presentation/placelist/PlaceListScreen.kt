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
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun PlaceListScreen(
    onGroupClick: (String) -> Unit,
    onAddClick: () -> Unit,
    onCreateGroupClick: () -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlaceListScreenContents(
        groups = uiState.groups,
        onGroupClick = onGroupClick,
        onAddClick = onAddClick,
        onCreateGroupClick = onCreateGroupClick,
        onCreatePlaceClick = onCreatePlaceClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    groups: List<GroupUiModel>,
    onGroupClick: (String) -> Unit,
    onAddClick: () -> Unit,
    onCreateGroupClick: () -> Unit,
    onCreatePlaceClick: () -> Unit,
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
                onClick = onCreatePlaceClick,
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
                    onGroupClick = { onGroupClick("") },
                    onAddClick = onAddClick,
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
            onGroupClick = {},
            onAddClick = {},
            onCreateGroupClick = {},
            onCreatePlaceClick = {},
        )
    }
}