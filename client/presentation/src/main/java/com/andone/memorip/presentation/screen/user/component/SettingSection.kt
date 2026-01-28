package com.andone.memorip.presentation.screen.user.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData

@Composable
fun SettingSection(
    title: String,
    items: List<SettingItemUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)
    ) {
        Text(
            text = title,
            style = MemoripTheme.typography.bodyBold14,
        )

        Surface(
            shape = memoripShapes.roundedMedium,
            color = MemoripTheme.colors.primaryContainer,
            contentColor = MemoripTheme.colors.onSurface
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingRowItem(item = item)

                    if (index != items.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingSectionPreview(){
    MemoripTheme {
        SettingSection(
            title = "권한 설정",
            items = DummyData.permissionItems
        )
    }
}
