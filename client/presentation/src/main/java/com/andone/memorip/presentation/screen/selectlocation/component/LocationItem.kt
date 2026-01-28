package com.andone.memorip.presentation.screen.selectlocation.component

import android.content.res.Configuration
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
            color = MemoripTheme.colors.onSurface,
            style = MemoripTheme.typography.bodyBold16
        )
        Text(
            text = location.roadAddress,
            color = MemoripTheme.colors.gray,
            style = MemoripTheme.typography.labelRegular14
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationItemPreview() {
    MemoripTheme {
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
}