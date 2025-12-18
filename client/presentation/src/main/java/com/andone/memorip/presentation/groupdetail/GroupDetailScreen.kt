package com.andone.memorip.presentation.groupdetail

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.groupdetail.component.GalleryTab
import com.andone.memorip.presentation.groupdetail.component.GroupDetailAppBar
import com.andone.memorip.presentation.groupdetail.component.MapTab
import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private object MarkerImageConstants {
    const val WIDTH = 200
    const val HEIGHT = 200
    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
}

@Composable
fun GroupDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val groupName = "Group1"

    GroupDetailScreenContent(
        groupName = groupName,
        places = DummyData.places,
        onBackClick = onBackClick,
        onSearchClick = { /* TODO: 검색 기능 구현 */ },
        onMenuClick = { /* TODO: 메뉴 기능 구현 */ },
        modifier = modifier
    )
}

@Composable
fun GroupDetailScreenContent(
    groupName: String,
    places: List<Place>,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0
) {
    val context = LocalContext.current
    val tabs = listOf(
        stringResource(R.string.groupdetail_tab_gallery),
        stringResource(R.string.groupdetail_tab_map)
    )

    var selectedTabIndex by remember { mutableIntStateOf(initialPage) }
    
    val markerImages = remember { mutableStateMapOf<String, Bitmap>() }

    LaunchedEffect(places) {
        places.forEach { place ->
            val imageUrl = place.thumbnailImage.url

            launch(Dispatchers.IO) {
                runCatching {
                    val imageLoader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .size(MarkerImageConstants.WIDTH, MarkerImageConstants.HEIGHT)
                        .allowHardware(false)
                        .build()
                    imageLoader.execute(request)
                }.mapCatching { result ->
                    (result as? SuccessResult)?.drawable?.toBitmap(
                        width = MarkerImageConstants.WIDTH,
                        height = MarkerImageConstants.HEIGHT,
                        config = MarkerImageConstants.BITMAP_CONFIG
                    ) ?: error("이미지 로드 실패")
                }.onSuccess { bitmap ->
                    withContext(Dispatchers.Main) {
                        markerImages[imageUrl] = bitmap
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            GroupDetailAppBar(
                title = groupName,
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                onSearchClick = onSearchClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> GalleryTab(
                    places = places,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> MapTab(
                    places = places,
                    markerImages = markerImages,
                    modifier = Modifier.fillMaxSize()
                )
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
            places = DummyData.places,
            onBackClick = {},
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
            places = DummyData.places,
            onBackClick = {},
            onSearchClick = {},
            onMenuClick = {},
            initialPage = 1
        )
    }
}