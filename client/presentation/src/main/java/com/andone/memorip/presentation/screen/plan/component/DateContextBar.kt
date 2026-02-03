package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DateFormatters
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DateContextBar(
    currentDate: LocalDate?,
    startDate: LocalDate?,
    endDate: LocalDate?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val isDaySelected = currentDate != null && startDate != null && endDate != null

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MemoripTheme.colors.background,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MemoripPadding.AppHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)
        ) {

            if (isDaySelected) {
                Text(
                    text = currentDate.format(DateFormatters.DAY_SHORT),
                    style = MemoripTheme.typography.headlineBold32,
                    modifier = Modifier.alignByBaseline()
                )

                Text(
                    text = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN),
                    style = MemoripTheme.typography.bodyMedium14,
                    modifier = Modifier.alignByBaseline()
                )
            } else {
                Text(
                    text = stringResource(R.string.plan_empty_day),
                    modifier = Modifier.clickable(onClick = onClick),
                    style = MemoripTheme.typography.bodyRegular18,
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1f))
            if (isDaySelected) {
                Text(
                    text = stringResource(
                        id = R.string.plan_date_range_format,
                        startDate.format(DateFormatters.DATE_RANGE),
                        endDate.format(DateFormatters.DATE_RANGE)
                    ),
                    modifier = Modifier
                        .alignByBaseline()
                        .clickable(onClick = onClick),
                    style = MemoripTheme.typography.bodyMedium14,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateContextBarPreview() {
    MemoripTheme {
        Column {
            DateContextBar(
                currentDate = LocalDate.of(2025, 1, 20),
                startDate = LocalDate.of(2025, 1, 20),
                endDate = LocalDate.of(2025, 1, 22),
                onClick = {}
            )
            DateContextBar(
                currentDate = null,
                startDate = null,
                endDate = null,
                onClick = {}
            )
        }
    }
}