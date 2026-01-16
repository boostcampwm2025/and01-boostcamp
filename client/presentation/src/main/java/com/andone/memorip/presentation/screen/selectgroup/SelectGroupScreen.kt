package com.andone.memorip.presentation.screen.selectgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.dialog.MemoripInputDialog
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.screen.selectgroup.component.GroupImageGridCard
import com.andone.memorip.presentation.screen.selectgroup.component.SelectGroupTopBar
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingMedium
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingXSmall
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceLarge
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceXSmall
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

private object SelectGroupScreenDimens {
    val GridMinWidth = 160.dp
}

@Composable
fun SelectGroupScreen(
    onGroupSelect: (GroupUiModel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_group_title),
    viewModel: SelectGroupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            SelectGroupEvent.NavigateBack -> {
                onBackClick()
            }

            is SelectGroupEvent.SelectGroup -> {
                onGroupSelect(event.group)
            }

            SelectGroupEvent.ShowDialog -> {
                showDialog = true
            }

            SelectGroupEvent.DismissDialog -> {
                showDialog = false
            }

            SelectGroupEvent.ShowSnackBar -> {
                /** TODO Snackbar 보여주기 */
            }
        }
    }

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    } else {
        SelectGroupContent(
            groups = uiState.groups,
            onAction = viewModel::onAction,
            title = title,
            modifier = modifier
        )
    }

    if (showDialog) {
        MemoripInputDialog(
            title = stringResource(R.string.select_group_dialog_title),
            onConfirmClick = { groupName ->
                viewModel.onAction(action = SelectGroupAction.OnDialogConfirmClick(groupName))
            },
            onCancelClick = { viewModel.onAction(action = SelectGroupAction.OnDialogCancelClick) },
            onDismissRequest = { viewModel.onAction(action = SelectGroupAction.OnDialogCancelClick) },
            hint = stringResource(R.string.select_group_dialog_hint),
            label = stringResource(R.string.select_group_dialog_label)
        )
    }
}

@Composable
private fun SelectGroupContent(
    groups: List<GroupUiModel>,
    onAction: (SelectGroupAction) -> Unit,
    title: String = stringResource(R.string.select_group_title),
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { 
            SelectGroupTopBar(
                onBackClick = { onAction(SelectGroupAction.OnBackClick) },
                title = title
            ) 
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SelectGroupAction.OnFABClick) },
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.group_list_add_content_description)
                )
            }
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = SelectGroupScreenDimens.GridMinWidth),
            modifier = modifier.padding(paddingValues = innerPadding),
            contentPadding = PaddingValues(
                horizontal = PaddingMedium,
                vertical = PaddingXSmall
            ),
            horizontalArrangement = Arrangement.spacedBy(SpaceXSmall),
            verticalArrangement = Arrangement.spacedBy(SpaceLarge)
        ) {
            items(
                items = groups,
                key = { it.id }
            ) { group ->
                GroupImageGridCard(
                    name = group.name,
                    images = group.images,
                    onClick = { onAction(SelectGroupAction.OnGroupClick(group)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectGroupScreenPrev() {
    MemoripTheme {
        SelectGroupContent(
            groups = DummyData.groups,
            onAction = {}
        )
    }
}