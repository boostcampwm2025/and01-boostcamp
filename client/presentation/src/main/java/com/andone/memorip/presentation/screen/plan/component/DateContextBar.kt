package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.util.DateFormatters

@Composable
fun DateContextBar(
    currentDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MemoripTheme.colors.background,
    ) {
        Row(
            modifier = Modifier.padding(vertical = MemoripPadding.PaddingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentDate.format(DateFormatters.DAY_SHORT),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = currentDate.dayOfWeek.getDisplayName(
                        java.time.format.TextStyle.FULL,
                        Locale.KOREAN
                    ),
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            Surface(
                shape = memoripShapes.roundedMedium,
                color = MemoripTheme.colors.primaryContainer,
                shadowElevation = MemoripShadow.Large,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = MemoripPadding.PaddingSmall,
                        vertical = MemoripPadding.PaddingXXSmall
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
                ) {
                    Text(
                        text = "${startDate.format(DateFormatters.DATE_RANGE)} ~ ${
                            endDate.format(
                                DateFormatters.DATE_RANGE
                            )
                        }",
                        style = MemoripTheme.typography.label1
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_outline_edit),
                        contentDescription = null,
                        modifier = Modifier.size(size = MemoripIconSize.IconSizeXSmall)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateContextBarPreview() {
    MemoripTheme {
        DateContextBar(
            currentDate = LocalDate.of(2025, 1, 20),
            startDate = LocalDate.of(2025, 1, 20),
            endDate = LocalDate.of(2025, 1, 22),
            onClick = {}
        )
    }
}