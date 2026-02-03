package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.plan.component.TimeAxisConstants.TimeAxisWidth
import com.andone.memorip.presentation.screen.plan.utill.HOURS_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_HOUR
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripTheme

private object TimeAxisConstants {
    val TimeAxisWidth = 68.dp
}

@Composable
fun TimeAxis(
    modifier: Modifier = Modifier,
    totalMinutes: Int = MINUTES_PER_DAY,
) {
    val totalDays = totalMinutes / MINUTES_PER_DAY
    val lineColor = MemoripTheme.colors.lightGray

    Column(
        modifier = modifier
            .width(TimeAxisWidth)
            .height(height = (totalMinutes * MINUTE_HEIGHT_DP).dp)
            .background(color = MemoripTheme.colors.primaryContainer),
    ) {
        repeat(times = totalDays * HOURS_PER_DAY) { hour ->
            val hour = hour % HOURS_PER_DAY

            Box(
                modifier = Modifier
                    .height((MINUTES_PER_HOUR * MINUTE_HEIGHT_DP).dp)
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = MemoripLineWidth.TimeTick.toPx()

                        drawLine(
                            color = lineColor,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )

                        drawLine(
                            color = lineColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.plan_time_axis_hour, hour),
                    color = MemoripTheme.colors.onSurface,
                    style = MemoripTheme.typography.bodyRegular12
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeAxisPreview() {
    MemoripTheme {
        TimeAxis()
    }
}