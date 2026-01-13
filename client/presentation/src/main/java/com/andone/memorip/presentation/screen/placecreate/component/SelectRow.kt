package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun SelectRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null,
    leadingIcon: Painter? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {

        leadingIcon?.let {
            Icon(
                painter = it,
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
        )

        Spacer(modifier = Modifier.width(width = MemoripSpace.SpaceMedium))

        value?.let {
            Text(
                text = it,
                style = MemoripTheme.typography.title2,
                color = MemoripTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        Icon(
            painter = painterResource(R.drawable.ic_chevron_forward),
            contentDescription = null,
            tint = MemoripTheme.colors.gray
        )
    }
}

@Preview
@Composable
private fun SelectRowPreview() {
    SelectRow(
        label = "카테고리",
        value = "맛집",
        leadingIcon = painterResource(R.drawable.ic_tag),
        onClick = { }
    )
}
