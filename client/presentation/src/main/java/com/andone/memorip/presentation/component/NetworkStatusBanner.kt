package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.domain.model.NetworkStatus
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusBanner(
    status: NetworkStatus,
    modifier: Modifier = Modifier
) {
    var showBanner by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = when (status) {
            is NetworkStatus.Available -> MemoripTheme.colors.green
            else -> MemoripTheme.colors.error
        }
    )

    LaunchedEffect(status) {
        showBanner = when (status) {
            is NetworkStatus.Unavailable, NetworkStatus.Losing, NetworkStatus.Lost -> true
            is NetworkStatus.Available -> {
                delay(2000L)
                false
            }
        }
    }

    AnimatedVisibility(
        visible = showBanner,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(backgroundColor)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.BottomCenter,

        ) {
            Text(
                text = when (status) {
                    NetworkStatus.Lost, NetworkStatus.Unavailable -> "네트워크 연결이 끊어졌습니다"
                    NetworkStatus.Losing -> "네트워크 신호가 약합니다"
                    NetworkStatus.Available -> "네트워크가 연결되었습니다"
                },
                color = MemoripTheme.colors.white,
                style = MemoripTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun NetworkStatusBannerPreview() {
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NetworkStatusBanner(status = NetworkStatus.Unavailable)
            HorizontalDivider()
            NetworkStatusBanner(status = NetworkStatus.Available)
            HorizontalDivider()
            NetworkStatusBanner(status = NetworkStatus.Lost)
            HorizontalDivider()
            NetworkStatusBanner(status = NetworkStatus.Losing)
        }
    }
}