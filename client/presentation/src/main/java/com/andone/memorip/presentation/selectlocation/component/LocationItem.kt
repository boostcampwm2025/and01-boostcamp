package com.andone.memorip.presentation.selectlocation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LocationItem(
    location: LocationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(
                horizontal = MemoripPadding.PaddingMedium,
                vertical = MemoripPadding.PaddingSmall
            )
    ) {
        Text(
            text = location.name,
            style = MemoripTheme.typography.bodyLarge
        )
        Text(
            text = location.roadAddress,
            color = MemoripTheme.colors.gray,
            style = MemoripTheme.typography.bodySmall
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun LocationItemPreview() {
    LocationItem(
        location = LocationUiModel(
            id = "",
            name = "국밥집",
            category = "음식",
            address = "서울시 이쪽구 저쪽동",
            roadAddress = "서울시 이쪽로 저쪽번지",
            latitude = 0.0,
            longitude = 0.0,
        ),
        onClick = {}
    )
}