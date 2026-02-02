package com.andone.memorip.presentation.screen.selecttrip

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.dialog.MemoripInputDialog
import com.andone.memorip.presentation.screen.selecttrip.component.SelectTripTopBar
import com.andone.memorip.presentation.screen.selecttrip.component.TripImageGridCard
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripEvent
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceLarge
import com.andone.memorip.presentation.theme.MemoripSpace.SpaceXSmall
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

private object SelectTripScreenDimens {
    val GridMinWidth = 160.dp
}

@Composable
fun SelectTripScreen(
    onTripSelect: (List<SelectTripUiModel>) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_trip_title),
    placeId: String? = null,
    initialSelectedTripIds: List<String>? = null,
    viewModel: SelectTripViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(placeId, initialSelectedTripIds) {
        viewModel.onAction(
            SelectTripAction.OnInitialize(
                placeId = placeId,
                initialSelectedTripIds = initialSelectedTripIds
            )
        )
    }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            SelectTripEvent.NavigateBack -> {
                onBackClick()
            }

            is SelectTripEvent.SelectTrip -> {
                onTripSelect(event.trips)
            }

            SelectTripEvent.PlaceTripsUpdated -> {
                onBackClick()
            }

            SelectTripEvent.ShowDialog -> {
                showDialog = true
            }

            SelectTripEvent.DismissDialog -> {
                showDialog = false
            }

            SelectTripEvent.ShowSnackBar -> {
                /** TODO Snackbar 보여주기 */
            }
        }
    }

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    } else {
        SelectGroupContent(
            groups = uiState.trips,
            selectedGroupIds = uiState.selectedTripIds,
            initialSelectedGroupIds = uiState.initialSelectedTripIds,
            onAction = viewModel::onAction,
            title = title,
            modifier = modifier
        )
    }

    if (showDialog) {
        MemoripInputDialog(
            title = stringResource(R.string.select_trip_dialog_title),
            onConfirmClick = { groupName ->
                viewModel.onAction(action = SelectTripAction.OnDialogConfirmClick(groupName))
            },
            onCancelClick = { viewModel.onAction(action = SelectTripAction.OnDialogCancelClick) },
            onDismissRequest = { viewModel.onAction(action = SelectTripAction.OnDialogCancelClick) },
            hint = stringResource(R.string.select_trip_dialog_hint),
            label = stringResource(R.string.select_trip_dialog_label)
        )
    }
}

@Composable
private fun SelectGroupContent(
    groups: List<SelectTripUiModel>,
    selectedGroupIds: Set<String>,
    initialSelectedGroupIds: Set<String>,
    onAction: (SelectTripAction) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_trip_title),
) {
    val hasChanges = selectedGroupIds != initialSelectedGroupIds
    val isPlaceDetailScreen = groups.any { it.isPlaceAdded }

    Scaffold(
        topBar = {
            SelectTripTopBar(
                enabled = hasChanges && selectedGroupIds.isNotEmpty(),
                onBackClick = { onAction(SelectTripAction.OnBackClick) },
                onCheckClick = { onAction(SelectTripAction.OnCheckClick) },
                title = title
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SelectTripAction.OnFABClick) },
                containerColor = MemoripTheme.colors.primary,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.trip_list_add_content_description)
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(all = MemoripPadding.AppHorizontalPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = SelectTripScreenDimens.GridMinWidth),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(SpaceXSmall),
                verticalArrangement = Arrangement.spacedBy(SpaceLarge)
            ) {
                items(
                    items = groups,
                    key = { it.id }
                ) { group ->
                    val isSelected = group.id in selectedGroupIds

                    val shouldShowCheck = if (isPlaceDetailScreen) {
                        if (hasChanges) isSelected else group.isPlaceAdded
                    } else {
                        isSelected
                    }

                    TripImageGridCard(
                        name = group.name,
                        images = group.images,
                        onClick = { onAction(SelectTripAction.OnTripClick(group)) },
                        isPlaceAdded = shouldShowCheck,
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectGroupScreenPreview() {
    MemoripTheme {
        val dummyGroups = DummyData.trips.map { trip ->
            SelectTripUiModel(
                id = trip.id,
                name = trip.name,
                images = trip.images,
                isPlaceAdded = false
            )
        }
        val selectedIds = setOf(dummyGroups[0].id, dummyGroups[2].id)
        SelectGroupContent(
            groups = dummyGroups,
            selectedGroupIds = selectedIds,
            initialSelectedGroupIds = emptySet(),
            onAction = {},
            title = stringResource(R.string.select_trip_title)
        )
    }
}
