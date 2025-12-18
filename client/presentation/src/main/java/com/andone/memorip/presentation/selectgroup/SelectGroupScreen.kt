package com.andone.memorip.presentation.selectgroup

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.GroupView
import com.andone.memorip.presentation.component.dialog.MemoripInputDialog
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingMedium
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingXSmall
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceXSmall
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.dummydata.DummyData.groups
import org.andone.memorip.presentation.theme.LocalMemoripTypography

@Composable
fun SelectGroupScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val groups = remember { groups }

    SelectGroupContent(
        groups = groups,
        onFABClick = { showDialog = true },
        onGroupClick = {
            /** TODO GROUP 선택 시 이전 화면으로 이동 및 데이터 전달 */
            Log.d("UI TEST", "group ui model : $it")
        },
        onBackClick = onBackClick,
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
                groups.add(newGroup)
                showDialog = false
            },
            onCancelClick = { showDialog = false },
            onDismissRequest = { showDialog = false },
            hint = stringResource(R.string.select_group_dialog_hint),
            label = stringResource(R.string.select_group_dialog_label)
        )
    }
}

@Composable
private fun SelectGroupContent(
    groups: List<GroupUiModel>,
    onFABClick: () -> Unit,
    onGroupClick: (GroupUiModel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { SelectGroupTopBar(onBackClick = onBackClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFABClick,
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.home_add_contentDescription)
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
                    images = group.images,
                    modifier = Modifier.clickable { onGroupClick(group) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectGroupTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.select_category_title),
                style = LocalMemoripTypography.current.headline2
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.select_group_back_button_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalMemoripColors.current.offWhite,
            navigationIconContentColor = LocalMemoripColors.current.black,
            titleContentColor = LocalMemoripColors.current.black
        )
    )
}

@Preview
@Composable
private fun SelectGroupScreenPrev() {
    MemoripTheme {
        SelectGroupScreen(onBackClick = {})
    }
}