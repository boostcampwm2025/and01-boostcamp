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
import com.andone.memorip.presentation.screen.plan.component.SelectTripDialogDimen.DIALOG_ITEM_TOTAL_HEIGHT
import com.andone.memorip.presentation.screen.plan.model.TripListUiModel
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private object SelectTripDialogDimen {
    val DIALOG_ITEM_TOTAL_HEIGHT = 300.dp
}

@Composable
fun SelectTripDialog(
    trips: ImmutableList<TripListUiModel>,
    selectedTrip: TripListUiModel?,
    onDismissRequest: () -> Unit,
    onConfirmClick: (TripListUiModel) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTrip by remember { mutableStateOf(selectedTrip ?: trips.first()) }

    DefaultDialog(
        title = stringResource(R.string.select_trip_dialog_title),
        onConfirmClick = {
            onConfirmClick(selectedTrip)
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
                items = trips,
                key = { it.id }
            ) { trip ->
                SelectTripItem(
                    trip = trip,
                    selected = selectedTrip.id == trip.id,
                    onItemClick = { selectedTrip = trip },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectTripDialogPreview() {
    MemoripTheme {
        SelectTripDialog(
            trips = DummyData.tripListItems.toImmutableList(),
            selectedTrip = DummyData.tripListItems.first(),
            onDismissRequest = { },
            onConfirmClick = { },
            onCancelClick = { },
        )
    }
}