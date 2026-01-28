package com.andone.memorip.presentation.screen.grouplist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.component.GroupView
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.screen.grouplist.component.GroupListTopBar
import com.andone.memorip.presentation.screen.grouplist.model.GroupListAction
import com.andone.memorip.presentation.screen.grouplist.model.GroupListEvent
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun GroupListScreen(
    onGroupClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is GroupListEvent.NavigateToGroupDetail -> {
                onGroupClick(event.groupId)
            }

            is GroupListEvent.NavigateToPlaceCreate -> {
                onCreatePlaceClick()
            }
        }
    }

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    } else {
        GroupListScreenContent(
            groups = uiState.groups,
            onAction = viewModel::onAction,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreenContent(
    groups: List<GroupUiModel>,
    onAction: (GroupListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { GroupListTopBar(onSearchClick = { }) },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = MemoripPadding.AppHorizontalPadding,
                top = innerPadding.calculateTopPadding(),
                end = MemoripPadding.AppHorizontalPadding,
                bottom = innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingXSmall)
        ) {
            items(items = groups) { group ->
                GroupView(
                    name = group.name,
                    onGroupClick = { onAction(GroupListAction.OnGroupClick(groupId = group.id)) },
                    onAddClick = { onAction(GroupListAction.OnGroupClick(groupId = group.id)) },
                    images = group.images
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GroupListScreenContentsPreview() {
    MemoripTheme {
        GroupListScreenContent(
            groups = DummyData.groups,
            onAction = {}
        )
    }
}
