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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.domain.model.NetworkStatus
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.coroutines.delay

private object NetworkStatusBannerConstants {
    const val ConnectedMessageDelay: Long = 2000L
}

private object NetworkStatusBannerDimens {
    val BannerHeight: Dp = 40.dp
    val VerticalPadding: Dp = 8.dp
    val PreviewSpacing: Dp = 16.dp
}

@Composable
fun NetworkStatusBanner(
    status: NetworkStatus,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        isVisible = when (status) {
            is NetworkStatus.Unavailable, NetworkStatus.Losing, NetworkStatus.Lost -> true
            is NetworkStatus.Available -> {
                if (isVisible) {
                    delay(timeMillis = NetworkStatusBannerConstants.ConnectedMessageDelay)
                }
                false
            }
        }
    }

    NetworkStatusBannerContent(
        status = status,
        isVisible = isVisible,
        modifier = modifier
    )
}

@Composable
private fun NetworkStatusBannerContent(
    status: NetworkStatus,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when (status) {
            is NetworkStatus.Available -> MemoripTheme.colors.green
            else -> MemoripTheme.colors.error
        }
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(NetworkStatusBannerDimens.BannerHeight)
                .background(backgroundColor)
                .padding(vertical = NetworkStatusBannerDimens.VerticalPadding),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = when (status) {
                    NetworkStatus.Lost, NetworkStatus.Unavailable ->
                        stringResource(R.string.network_status_disconnected)

                    NetworkStatus.Losing ->
                        stringResource(R.string.network_status_weak_signal)

                    NetworkStatus.Available ->
                        stringResource(R.string.network_status_connected)
                },
                color = MemoripTheme.colors.white,
                style = MemoripTheme.typography.bodyMedium14
            )
        }
    }
}

@Preview
@Composable
private fun NetworkStatusBannerContentPreview() {
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = NetworkStatusBannerDimens.PreviewSpacing)
        ) {
            NetworkStatusBannerContent(
                status = NetworkStatus.Unavailable,
                isVisible = true
            )
            NetworkStatusBannerContent(
                status = NetworkStatus.Losing,
                isVisible = true
            )
            NetworkStatusBannerContent(
                status = NetworkStatus.Available,
                isVisible = true
            )
        }
    }
}