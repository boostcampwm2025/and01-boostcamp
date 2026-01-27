package com.andone.memorip.presentation.screen.placelist.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.ClickableTagChip
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.placelist.component.TagSelectBottomSheetConstant.TAG_LIST_HEIGHT
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData

private object TagSelectBottomSheetConstant{
    val TAG_LIST_HEIGHT = 104.dp
}

@Composable
fun TagSelectBottomSheet(
    modifier: Modifier = Modifier,
    tagPagingItems: LazyPagingItems<TagUiModel>,
    selectedTags: List<TagUiModel> = emptyList(),
    onConfirmClick: () -> Unit = {},
    onTagChipClick: (tagUiModel: TagUiModel) -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = MemoripPadding.AppHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
    ) {
        Text(
            text = stringResource(R.string.place_list_add_tag),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MemoripTheme.typography.headlineBold20
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
        ) {
            selectedTags.forEach { tag -> ClickableTagChip(tag = tag) }
        }

        HorizontalDivider()

        TagListSection(
            tagPagingItems = tagPagingItems,
            onChipClick = onTagChipClick
        )

        HorizontalDivider()

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            shape = memoripShapes.roundedXSmall,
            colors = ButtonDefaults.buttonColors()
                .copy(
                    containerColor = MemoripTheme.colors.primaryContainer,
                    contentColor = MemoripTheme.colors.onSurface
                )
        ) {
            Text(
                text = stringResource(R.string.place_list_bottom_sheet_confirm),
                style = MemoripTheme.typography.labelBold16
            )
        }
    }
}

@Composable
private fun TagListSection(
    modifier: Modifier = Modifier,
    tagPagingItems: LazyPagingItems<TagUiModel>,
    onChipClick: (tagUiModel: TagUiModel) -> Unit = {}
) {
    MemoripPagingList(
        pagingItems = tagPagingItems,
        itemKey = { it.id },
        emptyContent = {
            EmptyText(
                text = stringResource(R.string.place_list_empty),
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = modifier
            .padding(horizontal = MemoripPadding.AppHorizontalPadding)
            .height(height = TAG_LIST_HEIGHT),
        useHorizontalGrid = true,
        itemContent = {
            ClickableTagChip(tag = it)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TagSelectBottomSheetPreview() {
    MemoripTheme {
        TagSelectBottomSheet(
            tagPagingItems = DummyData.getTagPagingItems(),
            selectedTags = DummyData.categories.toList()
        )
    }
}