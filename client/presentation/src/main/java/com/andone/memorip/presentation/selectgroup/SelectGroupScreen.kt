package com.andone.memorip.presentation.selectgroup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.GroupView
import com.andone.memorip.presentation.component.dialog.MemoripInputDialog
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.selectgroup.component.SelectGroupTopBar
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingMedium
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingXSmall
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceXSmall
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun SelectGroupScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelectGroupViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                SelectGroupEvent.onNavigateBack -> {
                    onBackClick()
                }

                is SelectGroupEvent.onNavigateAddPlace -> {
                    /** TODO 그룹 추가 화면으로 이동하되 데이터를 들고 이동하기 */
                    Log.d("UI TEST", "add group after : ${event.group }")
                }

                SelectGroupEvent.onShowDialog -> {
                    showDialog = true
                }

                SelectGroupEvent.onDismissDialog -> {
                    showDialog = false
                }

                SelectGroupEvent.onShowSnackbar -> {
                    /** TODO Snackbar 보여주기 */
                }
            }
        }
    }

    SelectGroupContent(
        groups = uiState.groups,
        onAction = viewModel::onAction,
        modifier = modifier
    )

    if (showDialog) {
        MemoripInputDialog(
            title = stringResource(R.string.select_group_dialog_title),
            onConfirmClick = {
                val newGroup = GroupUiModel(
                    name = it,
                    images = emptyList()
                )
                viewModel.onAction(action = SelectGroupAction.onConfirmDialogClick(newGroup))
            },
            onCancelClick = { viewModel.onAction(action = SelectGroupAction.onCancelDialogClick) },
            onDismissRequest = { viewModel.onAction(action = SelectGroupAction.onCancelDialogClick) },
            hint = stringResource(R.string.select_group_dialog_hint),
            label = stringResource(R.string.select_group_dialog_label)
        )
    }
}

@Composable
private fun SelectGroupContent(
    groups: List<GroupUiModel>,
    onAction: (SelectGroupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { SelectGroupTopBar(onBackClick = { onAction(SelectGroupAction.onBackClick) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SelectGroupAction.onFABClick) },
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.home_add_content_description)
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues = innerPadding)
                .padding(horizontal = PaddingMedium, vertical = PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(SpaceXSmall)
        ) {
            items(items = groups) { group ->
                GroupView(
                    name = group.name,
                    onGroupClick = { onAction(SelectGroupAction.onGroupClick(group)) },
                    onAddClick = { onAction(SelectGroupAction.onAddGroupClick) },
                    images = group.images,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectGroupScreenPrev() {
    MemoripTheme {
        SelectGroupScreen(onBackClick = {})
    }
}