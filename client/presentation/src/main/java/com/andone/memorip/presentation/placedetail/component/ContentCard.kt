package com.andone.memorip.presentation.placedetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripElevation
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun ContentCard(
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        border = BorderStroke(
            width = MemoripBorderWidth.Thin,
            color = MemoripTheme.colors.gray
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = MemoripElevation.ElevationMedium)
    ) {
        Text(
            modifier = Modifier.padding(MemoripPadding.PaddingXLarge),
            text = content,
            style = MemoripTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentCardPrev() {
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
        ){
            ContentCard(content = "임의의 내용 임의의 내용 임의의 내용 임의의 내용 임의의 내용 임의의 내용")
            ContentCard(content = "임의의 내용 임의의 내용 임의의 내용 임의의 내용 임의의 내용 임의의 내용")
        }
    }
}