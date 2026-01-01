package com.andone.memorip.presentation.grouplist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
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
import com.andone.memorip.presentation.grouplist.component.AddFloatingActionButton
import com.andone.memorip.presentation.grouplist.component.GroupListTopBar
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.grouplist.model.GroupListAction
import com.andone.memorip.presentation.grouplist.model.GroupListEvent
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun GroupListScreen(
    onGroupClick: (Int) -> Unit,
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

            GroupListEvent.ShowSnackBar -> {
                // TODO: 설정 필요
            }
        }
    }

    GroupListScreenContents(
        groups = uiState.groups,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreenContents(
    groups: List<GroupUiModel>,
    onAction: (GroupListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { GroupListTopBar(onSearchClick = { }) },
        floatingActionButton = { AddFloatingActionButton(onClick = { onAction(GroupListAction.OnFABClick) }) },
        contentWindowInsets = WindowInsets(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(horizontal = MemoripPadding.PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingXSmall)
        ) {
            items(items = groups) { group ->
                GroupView(
                    name = group.name,
                    onGroupClick = { onAction(GroupListAction.OnGroupClick("")) },
                    onAddClick = { onAction(GroupListAction.OnGroupClick("")) },
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
        GroupListScreenContents(
            groups = DummyData.groups,
            onAction = {}
        )
    }
}