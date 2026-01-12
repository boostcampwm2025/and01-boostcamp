package com.andone.memorip.presentation.placedetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TagCard(
    tags: ImmutableList<TagUiModel>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        border = BorderStroke(
            width = MemoripBorderWidth.Thin,
            color = MemoripTheme.colors.gray
        ),
        colors = CardDefaults.cardColors(
            containerColor = MemoripTheme.colors.white,
            contentColor = MemoripTheme.colors.black
        )
    ) {
        Column(modifier = Modifier.padding(MemoripPadding.PaddingXLarge)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_tag_badge),
                    tint = MemoripTheme.colors.green,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.place_detail_tag),
                    style = MemoripTheme.typography.label1
                )
            }
            TagChipRow(
                tags = tags,
                modifier = Modifier.padding(top = MemoripSpace.SpaceMedium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagCardPrev() {
    MemoripTheme {
        TagCard(tags = DummyData.categories.toImmutableList())
    }
}