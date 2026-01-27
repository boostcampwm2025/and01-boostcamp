package com.andone.memorip.presentation.screen.grouplist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.grouplist.model.GroupItem
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun ImageCard(
    item: GroupItem,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = MemoripPadding.PaddingXXXSmall)
                .clip(shape = memoripShapes.roundedSmall),
            contentAlignment = Alignment.Center
        ) {
            MemoripImage(
                imageUrl = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            if (item.overNumber > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MemoripTheme.colors.black.copy(alpha = MemoripAlpha.IMAGE_PLACEHOLDER)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.group_list_overflow_count,
                            item.overNumber
                        ),
                        color = MemoripTheme.colors.white
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ImageCardPreview() {
    MemoripTheme {
        ImageCard(
            item = GroupItem(
                colSpan = 4,
                rowSpan = 3,
            )
        )
    }
}