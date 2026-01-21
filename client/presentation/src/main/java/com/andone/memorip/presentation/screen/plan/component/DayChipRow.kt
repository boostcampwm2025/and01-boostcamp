package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.plan.component.DayChipRowConstants.HEIGHT
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object DayChipRowConstants{
    val HEIGHT = 52.dp
}
@Composable
fun DayChipRow(
    totalDays: Int,
    selectedDay: Int?,
    modifier: Modifier = Modifier,
    onDaySelected: (Int) -> Unit,
    onLongClick: (Int) -> Unit = {},
    onAddDayClick: () -> Unit = {},
    longClickedDay: Int? = null,
) {
    val days = (1..totalDays).toList()

    Row(
        modifier = modifier
            .padding(horizontal = MemoripPadding.PaddingMedium)
            .padding(bottom = MemoripPadding.PaddingXXSmall)
            .height(height = HEIGHT)
            .horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            DayChip(
                day = day,
                selected = day == selectedDay,
                onClick = onDaySelected,
                onLongClick = onLongClick,
                isDeleteMode = longClickedDay != null,
                isDeletedTarget = longClickedDay == day
            )
        }
        if (longClickedDay == null) {
            IconButton(onClick = onAddDayClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_add_circle),
                    contentDescription = stringResource(R.string.plan_add_day),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayChipRowPreview() {
    MemoripTheme {
        DayChipRow(
            totalDays = 5,
            selectedDay = 2,
            onDaySelected = {},
        )
    }
}
