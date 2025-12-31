package com.andone.memorip.presentation.placelist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun PlaceListScreen(
    onPlaceClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event -> }

    PlaceListScreenContents(
        places = uiState.places,
        onAction = {},
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    places: List<Place>,
    onAction: (PlaceListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PlaceListTopBar(
                scrollBehavior = scrollBehavior,
                query = "",
                onQueryChange = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlaceListScreenContentsPreview() {
    MemoripTheme {
        PlaceListScreenContents(
            places = emptyList(),
            onAction = {},
        )
    }
}