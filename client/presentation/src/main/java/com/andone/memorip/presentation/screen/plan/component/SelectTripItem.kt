package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.plan.model.TripListUiModel
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.rememberColorState

@Composable
fun SelectTripItem(
    trip: TripListUiModel,
    selected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorState = rememberColorState()

    LaunchedEffect(Unit) {
        colorState.refreshColor()
    }

    Row(
        modifier = modifier
            .clickable { onItemClick() }
            .padding(
                vertical = MemoripPadding.PaddingMedium,
                horizontal = MemoripPadding.PaddingXLarge
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(size = MemoripIconSize.IconSizeLarge)
                    .background(color = colorState.color, shape = MemoripTheme.shapes.roundedMedium)
                    .clip(shape = MemoripTheme.shapes.roundedMedium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = trip.title[0].toString(),
                    style = MemoripTheme.typography.headlineBold20,
                    color = MemoripTheme.colors.black
                )
            }
            Text(
                text = trip.title,
                modifier = Modifier.padding(start = MemoripPadding.PaddingMedium),
                color = MemoripTheme.colors.onSurface,
                style = MemoripTheme.typography.titleBold20
            )
        }

        if (selected) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                contentDescription = stringResource(R.string.plan_trip_select_btn_description),
                modifier = Modifier.size(size = MemoripIconSize.IconSizeMedium),
                tint = MemoripTheme.colors.primary
            )
        }
    }
}

@Preview(name = "선택 안된 아이템", showBackground = true)
@Composable
private fun NotSelectTripItemPreview() {
    MemoripTheme {
        SelectTripItem(
            trip = DummyData.tripListItems.first(),
            selected = false,
            onItemClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "선택된 아이템", showBackground = true)
@Composable
private fun SelectTripItemPreview() {
    MemoripTheme {
        SelectTripItem(
            trip = DummyData.tripListItems.first(),
            selected = true,
            onItemClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}