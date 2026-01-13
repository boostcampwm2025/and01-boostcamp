package com.andone.memorip.presentation.screen.selectcategory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.selectcategory.component.CategoryItemDimens.CATEGORY_ITEM_WIDTH
import com.andone.memorip.presentation.theme.MemoripIconSize.IconSizeMedium
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingXSmall
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object CategoryItemDimens {
    val CATEGORY_ITEM_WIDTH = 6.dp
}

@Composable
fun CategoryItem(
    tagUiModel: TagUiModel,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = PaddingXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = CATEGORY_ITEM_WIDTH, height = IconSizeMedium)
                .background(color = tagUiModel.color, shape = MemoripTheme.shapes.roundedSmall)
        )
        Text(
            text = tagUiModel.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = PaddingXSmall),
            style = MemoripTheme.typography.body2
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MemoripTheme.colors.primary,
                uncheckedColor = MemoripTheme.colors.primary,
                checkmarkColor = MemoripTheme.colors.white,
                disabledCheckedColor = MemoripTheme.colors.white
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryItemPrev() {
    MemoripTheme {
        var checked by remember { mutableStateOf(false) }
        CategoryItem(
            tagUiModel = DummyData.categories.first(),
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}