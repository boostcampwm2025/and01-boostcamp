package com.andone.memorip.presentation.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.GroupView
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun HomeScreen() {
    HomeScreenContents(
        groups = emptyList()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContents(
    groups: List<GroupUiModel>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(id = R.string.home_search_contentDescription)
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MemoripTheme.colors.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.home_add_contentDescription)
                )
            }
        }
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
                    images = group.images,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenContentsPreview() {
    val groups = listOf(
        GroupUiModel(
            name = "기본 그룹",
            images = List(size = 8) { "" }
        ),
        GroupUiModel(
            name = "부 산",
            images = List(size = 4) { "" }
        ),
        GroupUiModel(
            name = "제주도",
            images = List(size = 5) { "" }
        ),
        GroupUiModel(
            name = "대구 ",
            images = List(size = 1) { "" }
        )
    )

    MemoripTheme {
        HomeScreenContents(groups = groups)
    }
}