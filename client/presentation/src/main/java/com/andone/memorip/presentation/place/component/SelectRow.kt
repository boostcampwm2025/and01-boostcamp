package com.andone.memorip.presentation.place.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun SelectRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MemoripTheme.colors.primary,
                modifier = Modifier
                    .size(MemoripIconSize.IconSizeLarge)
                    .padding(end = MemoripPadding.PaddingXSmall)
            )
        }

        Text(
            text = label,
            style = MemoripTheme.typography.title2,
            color = MemoripTheme.colors.black
        )

        Spacer(modifier = Modifier.width(width = MemoripSpace.SpaceMedium))

        Text(
            text = value,
            style = MemoripTheme.typography.title2,
            color = MemoripTheme.colors.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MemoripTheme.colors.gray
        )
    }
}

@Preview
@Composable
private fun SelectRowPreview(){
    SelectRow(
        label = "카테고리",
        value = "맛집",
        leadingIcon = Icons.Filled.Tag,
        onClick = { }
    )
}
