package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.domain.model.NetworkStatus
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun NetworkStatusBanner(
    status: NetworkStatus,
    modifier: Modifier = Modifier
) {
    val isVisible = status == NetworkStatus.Unavailable || status == NetworkStatus.Lost
    val backgroundColor = MemoripTheme.colors.error

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundColor)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.BottomCenter,

        ) {
            Text(
                text = "네트워크 연결이 끊어졌습니다",
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