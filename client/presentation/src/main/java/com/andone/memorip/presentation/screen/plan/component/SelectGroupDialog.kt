package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.DefaultDialog
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.screen.plan.component.SelectGroupDialogDimen.DIALOG_ITEM_TOTAL_HEIGHT
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private object SelectGroupDialogDimen {
    val DIALOG_ITEM_TOTAL_HEIGHT = 300.dp
}

@Composable
fun SelectGroupDialog(
    groups: ImmutableList<GroupUiModel>,
    onDismissRequest: () -> Unit,
    onConfirmClick: (GroupUiModel) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedGroup by remember { mutableStateOf(groups.first()) }

    DefaultDialog(
        title = stringResource(R.string.select_group_dialog_title),
        onConfirmClick = {
            onConfirmClick(selectedGroup)
            onDismissRequest()
        },
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        HorizontalDivider(
            thickness = MemoripLineWidth.Thin,
            color = MemoripTheme.colors.primaryContainer
        )
        LazyColumn(
            modifier = Modifier.height(DIALOG_ITEM_TOTAL_HEIGHT),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
        ) {
            items(
                items = groups,
                key = { it.id }
            ) { group ->
                SelectGroupItem(
                    group = group,
                    selected = selectedGroup.id == group.id,
                    onItemClick = { selectedGroup = group },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectGroupDialogPreview() {
    MemoripTheme {
        SelectGroupDialog(
            groups = DummyData.groups.toImmutableList(),
            onDismissRequest = { },
            onConfirmClick = { },
            onCancelClick = { },
        )
    }
}