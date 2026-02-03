package com.andone.memorip.presentation.screen.tripdetail.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.tripdetail.model.PlaceViewMode
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object PlaceListHeaderDimen {
    val SEGMENT_SIZE = MemoripHeight.SearchBoxHeight
    val TOGGLE_PADDING = MemoripPadding.PaddingXXSmall
    val OUTER_RADIUS = 8.dp
    val TOGGLE_INNER_OFFSET = MemoripLineWidth.Small
    val INNER_RADIUS = OUTER_RADIUS - TOGGLE_INNER_OFFSET
    val TOTAL_WIDTH = SEGMENT_SIZE * 2 + TOGGLE_PADDING * 2
    val TOTAL_HEIGHT = SEGMENT_SIZE + TOGGLE_PADDING * 2
}

@Composable
fun BottomSheetPlaceListHeader(
    placeCount: Int,
    viewMode: PlaceViewMode,
    onViewModeToggle: () -> Unit,
    modifier: Modifier = Modifier,
    hasMorePages: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MemoripPadding.PaddingMedium,
                vertical = MemoripPadding.PaddingXSmall
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 다음 페이지 여부에 따라 다른 문자열 리소스 사용
        Text(
            text = if (hasMorePages) {
                stringResource(R.string.trip_detail_place_list_count_format_more, placeCount)
            } else {
                stringResource(R.string.trip_detail_place_list_count_format, placeCount)
            },
            style = MemoripTheme.typography.headlineBold20,
            color = MemoripTheme.colors.onSurface
        )

        ViewModeToggle(
            viewMode = viewMode,
            onViewModeToggle = onViewModeToggle,
            modifier = Modifier
        )
    }
}

@Composable
fun ViewModeToggle(
    viewMode: PlaceViewMode,
    onViewModeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .size(width = PlaceListHeaderDimen.TOTAL_WIDTH, height = PlaceListHeaderDimen.TOTAL_HEIGHT)
            .clip(RoundedCornerShape(PlaceListHeaderDimen.OUTER_RADIUS))
            .background(MemoripTheme.colors.gray2)
            .padding(PlaceListHeaderDimen.TOGGLE_PADDING),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ViewModeToggleSegment(
            isSelected = viewMode == PlaceViewMode.LIST,
            iconRes = R.drawable.ic_format_list_bulleted,
            contentDescription = stringResource(R.string.trip_detail_view_list_description),
            onClick = { if (viewMode != PlaceViewMode.LIST) onViewModeToggle() }
        )
        ViewModeToggleSegment(
            isSelected = viewMode == PlaceViewMode.GRID,
            iconRes = R.drawable.ic_grid_view,
            contentDescription = stringResource(R.string.trip_detail_view_grid_description),
            onClick = { if (viewMode != PlaceViewMode.GRID) onViewModeToggle() }
        )
    }
}

@Composable
private fun ViewModeToggleSegment(
    isSelected: Boolean,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .size(PlaceListHeaderDimen.SEGMENT_SIZE)
            .clip(RoundedCornerShape(PlaceListHeaderDimen.INNER_RADIUS))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(PlaceListHeaderDimen.SEGMENT_SIZE)
                    .shadow(
                        elevation = PlaceListHeaderDimen.TOGGLE_INNER_OFFSET,
                        shape = RoundedCornerShape(PlaceListHeaderDimen.INNER_RADIUS)
                    )
                    .background(MemoripTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    tint = MemoripTheme.colors.primary,
                    modifier = Modifier.size(MemoripIconSize.IconSizeMedium)
                )
            }
        } else {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = MemoripTheme.colors.gray,
                modifier = Modifier.size(MemoripIconSize.IconSizeMedium)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomSheetViewModeTogglePreview() {
    MemoripTheme {
        Row(
            modifier = Modifier
                .background(MemoripTheme.colors.background)
                .padding(MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(MemoripPadding.PaddingMedium)
        ) {
            ViewModeToggle(
                viewMode = PlaceViewMode.LIST,
                onViewModeToggle = {}
            )
            ViewModeToggle(
                viewMode = PlaceViewMode.GRID,
                onViewModeToggle = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomSheetPlaceListHeaderPreview() {
    MemoripTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.background(MemoripTheme.colors.background)
        ) {
            BottomSheetPlaceListHeader(
                placeCount = 4,
                viewMode = PlaceViewMode.LIST,
                onViewModeToggle = {}
            )
        }
    }
}
