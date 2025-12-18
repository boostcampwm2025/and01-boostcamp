package com.andone.memorip.presentation.groupdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.groupdetail.component.GalleryTab
import com.andone.memorip.presentation.groupdetail.component.GroupDetailAppBar
import com.andone.memorip.presentation.groupdetail.component.MapTab
import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.coroutines.launch

@Composable
fun GroupDetailScreen(
    onBackClick: () -> Unit,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // 임시 더미 데이터 생성
    // todo: 실제 groupId를 가져와서 그룹 정보를 가져와야 함
    val groupName = "Group1"
    val dummyImages = remember {
        List(30) { index ->
            val randomHeight = (150..400).random()
            val fixedWidth = 200
            PlaceImageItem(
                id = index,
                url = "https://picsum.photos/id/${index + 1}/$fixedWidth/$randomHeight",
                width = fixedWidth,
                height = randomHeight
            )
        }
    }

    GroupDetailScreenContent(
        groupName = groupName,
        images = dummyImages,
        onBackClick = onBackClick,
        onImageClick = onImageClick,
        onSearchClick = { /* TODO: 검색 기능 구현 */ },
        onMenuClick = { /* TODO: 메뉴 기능 구현 */ },
        modifier = modifier
    )
}

@Composable
fun GroupDetailScreenContent(
    groupName: String,
    images: List<PlaceImageItem>,
    onBackClick: () -> Unit,
    onImageClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0
) {
    val tabs = listOf(
        stringResource(R.string.groupdetail_tab_gallery),
        stringResource(R.string.groupdetail_tab_map)
    )
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { tabs.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            GroupDetailAppBar(
                title = groupName,
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> GalleryTab(
                        images = images,
                        onImageClick = onImageClick,
                        modifier = Modifier.fillMaxSize()
                    )

                    1 -> MapTab(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Preview(name = "Gallery Tab Selected")
@Composable
private fun GroupDetailScreenContentGalleryPreview() {
    MemoripTheme {
        GroupDetailScreenContent(
            groupName = "Group1",
            images = DummyData.dummyImages,
            onBackClick = {},
            onImageClick = {},
            onSearchClick = {},
            onMenuClick = {},
            initialPage = 0
        )
    }
}

@Preview(name = "Map Tab Selected")
@Composable
private fun GroupDetailScreenContentMapPreview() {
    MemoripTheme {
        GroupDetailScreenContent(
            groupName = "Group1",
            images = DummyData.dummyImages,
            onBackClick = {},
            onImageClick = {},
            onSearchClick = {},
            onMenuClick = {},
            initialPage = 1
        )
    }
}