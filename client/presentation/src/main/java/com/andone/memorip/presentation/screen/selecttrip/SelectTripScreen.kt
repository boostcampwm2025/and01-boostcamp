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
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.selecttrip.SelectTripScreenConstant.INPUT_DIALOG_MAX_LENGTH
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

private object SelectGroupScreenDimen {
    val GridMinWidth = 160.dp
}

private object SelectTripScreenConstant {
    const val INPUT_DIALOG_MAX_LENGTH = 20
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
    var dialogInputValue by remember { mutableStateOf("") }

    LaunchedEffect(showDialog) {
        if (showDialog) dialogInputValue = ""
    }

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
        SelectTripContent(
            trips = uiState.trips,
            selectedTripIds = uiState.selectedTripIds,
            initialSelectedTripIds = uiState.initialSelectedTripIds,
            onAction = viewModel::onAction,
            title = title,
            modifier = modifier
        )
    }

    if (showDialog) {
        MemoripInputDialog(
            value = dialogInputValue,
            onValueChange = { newValue ->
                val filtered = newValue.replace("\n", "").let {
                    if (it.length > INPUT_DIALOG_MAX_LENGTH) it.take(n = INPUT_DIALOG_MAX_LENGTH) else it
                }
                dialogInputValue = filtered
            },
            title = stringResource(R.string.select_trip_dialog_title),
            onConfirmClick = { tripName ->
                viewModel.onAction(action = SelectTripAction.OnDialogConfirmClick(tripName))
            },
            onCancelClick = { viewModel.onAction(action = SelectTripAction.OnDialogCancelClick) },
            onDismissRequest = { viewModel.onAction(action = SelectTripAction.OnDialogCancelClick) },
            hint = stringResource(R.string.select_trip_dialog_hint),
            label = stringResource(R.string.select_trip_dialog_label),
            maxLength = INPUT_DIALOG_MAX_LENGTH
        )
    }
}

@Composable
private fun SelectTripContent(
    trips: List<SelectTripUiModel>,
    selectedTripIds: Set<String>,
    initialSelectedTripIds: Set<String>,
    onAction: (SelectTripAction) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_trip_title),
) {
    val hasChanges = selectedTripIds != initialSelectedTripIds
    val isPlaceDetailScreen = trips.any { it.isPlaceAdded }

    Scaffold(
        topBar = {
            SelectTripTopBar(
                enabled = hasChanges && selectedTripIds.isNotEmpty(),
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
                columns = GridCells.Adaptive(minSize = SelectGroupScreenDimen.GridMinWidth),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(SpaceXSmall),
                verticalArrangement = Arrangement.spacedBy(SpaceLarge)
            ) {
                items(
                    items = trips,
                    key = { it.id }
                ) { trip ->
                    val isSelected = trip.id in selectedTripIds

                    val shouldShowCheck = if (isPlaceDetailScreen) {
                        if (hasChanges) isSelected else trip.isPlaceAdded
                    } else {
                        isSelected
                    }

                    TripImageGridCard(
                        name = trip.name,
                        images = trip.images,
                        onClick = { onAction(SelectTripAction.OnTripClick(trip)) },
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
private fun SelectTripScreenPreview() {
    MemoripTheme {
        val dummyTrips = DummyData.trips.map { trip ->
            SelectTripUiModel(
                id = trip.id,
                name = trip.name,
                images = trip.images,
                isPlaceAdded = false
            )
        }
        val selectedIds = setOf(dummyTrips[0].id, dummyTrips[2].id)
        SelectTripContent(
            trips = dummyTrips,
            selectedTripIds = selectedIds,
            initialSelectedTripIds = emptySet(),
            onAction = {},
            title = stringResource(R.string.select_trip_title)
        )
    }
}
