package com.andone.memorip.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.home.component.CardSpec.RATIO
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import org.andone.memorip.presentation.theme.LocalMemoripTypography

private object CardSpec{
    const val RATIO = 5f / 3f
}

@Composable
fun EmptyGroupPlaceholder(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = RATIO)
            .background(
                color = MemoripTheme.colors.offWhite,
                shape = memoripShapes.defaultCorner
            )
            .border(
                width = MemoripBorderWidth.Thin,
                color = MemoripTheme.colors.outline,
                shape = memoripShapes.defaultCorner
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                tint = MemoripTheme.colors.primary
            )

            Text(
                text = stringResource(R.string.group_view_add_place),
                style = LocalMemoripTypography.current.body2,
                color = MemoripTheme.colors.outline
            )
        }
    }
}

@Preview
@Composable
private fun EmptyGroupPlaceholderPreview(){
    MemoripTheme{
        EmptyGroupPlaceholder(onClick = {})
    }
}

