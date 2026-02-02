package com.andone.memorip.presentation.screen.tripdetail.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripElevation
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.calculateDDay
import com.andone.memorip.presentation.util.formatDateRange

private object TripScheduleCardDimen {
    val PRIMARY_STRIP_WIDTH = 5.dp
    val ICON_BOX_SIZE = 48.dp
    val ICON_BOX_CORNER_RADIUS = 12.dp
}

@Composable
fun TripScheduleCard(
    tripName: String,
    startDate: String,
    endDate: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedSmall,
        colors = CardDefaults.cardColors(containerColor = MemoripTheme.colors.background.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = MemoripElevation.ElevationMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(TripScheduleCardDimen.PRIMARY_STRIP_WIDTH)
                    .fillMaxHeight()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = MemoripPadding.PaddingSmall,
                            bottomStart = MemoripPadding.PaddingSmall
                        )
                    )
                    .background(MemoripTheme.colors.primary)
            )
            Box(
                modifier = Modifier
                    .padding(MemoripPadding.PaddingMedium)
                    .size(TripScheduleCardDimen.ICON_BOX_SIZE)
                    .clip(RoundedCornerShape(TripScheduleCardDimen.ICON_BOX_CORNER_RADIUS))
                    .background(MemoripTheme.colors.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_calendar_month),
                    contentDescription = null,
                    tint = MemoripTheme.colors.primary,
                    modifier = Modifier.size(MemoripIconSize.IconSizeMedium)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MemoripPadding.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.trip_detail_schedule_upcoming),
                        style = MemoripTheme.typography.titleBold16,
                        color = MemoripTheme.colors.primary
                    )
                    calculateDDay(startDate).takeIf { it.isNotEmpty() }?.let { dDay ->
                        Text(
                            text = dDay,
                            style = MemoripTheme.typography.titleBold16,
                            color = MemoripTheme.colors.primary
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.trip_detail_schedule_trip_format, tripName),
                    style = MemoripTheme.typography.titleBold18,
                    color = MemoripTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                formatDateRange(startDate, endDate).takeIf { it.isNotEmpty() }?.let { dateRange ->
                    Text(
                        text = dateRange,
                        style = MemoripTheme.typography.bodyBold16,
                        color = MemoripTheme.colors.gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripScheduleCardPreview() {
    MemoripTheme {
        Column(
            modifier = Modifier
                .background(MemoripTheme.colors.primaryContainer)
                .padding(MemoripPadding.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
        ) {
            TripScheduleCard(
                tripName = "서울대공원 주암 나들이",
                startDate = "2026-02-05",
                endDate = "2026-02-07"
            )
        }
    }
}
