package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.plan.component.TimeAxisConstants.HOURS_PER_DAY
import com.andone.memorip.presentation.screen.plan.component.TimeAxisConstants.MINUTES_PER_HOUR
import com.andone.memorip.presentation.screen.plan.component.TimeAxisConstants.TimeAxisWidth
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.theme.MemoripTheme

private object TimeAxisConstants {
    val TimeAxisWidth = 68.dp
    const val HOURS_PER_DAY = 24
    const val MINUTES_PER_HOUR = 60
}

@Composable
fun TimeAxis() {
    Column(
        modifier = Modifier
            .width(TimeAxisWidth)
            .height(height = (MINUTES_PER_DAY * MINUTE_HEIGHT_DP).dp)
            .background(color = MemoripTheme.colors.primaryContainer),
    ) {
        repeat(times = HOURS_PER_DAY) { hour ->
            Box(
                modifier = Modifier
                    .height(height = (MINUTES_PER_HOUR * MINUTE_HEIGHT_DP).dp)
                    .fillMaxWidth()
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        id = R.string.plan_time_axis_hour,
                        hour
                    ),
                    color = MemoripTheme.colors.onSurface
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