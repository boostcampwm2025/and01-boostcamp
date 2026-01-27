package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripButton
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentConstant.ELEVATION_ALPHA
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentConstant.ICON_BOX_DECORATION_ALPHA
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentConstant.ROTATE_DEGREE
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentDimen.ICON_BOX_SIZE
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentDimen.ICON_SIZE
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContentDimen.INNER_BOX_SIZE
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

private object DateNotSelectedContentConstant {
    const val ICON_BOX_DECORATION_ALPHA = 0.05f
    const val ROTATE_DEGREE = 3f
    const val ELEVATION_ALPHA = 12f
}

private object DateNotSelectedContentDimen {
    val ICON_BOX_SIZE = 192.dp
    val INNER_BOX_SIZE = 96.dp
    val ICON_SIZE = 36.dp
    val INNER_BOX_ELEVATION = 2.dp
}

@Composable
fun DateNotSelectedContent(
    onSelectDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotSelectedIcon()
        NotSelectedDescription()
        MemoripButton(
            value = stringResource(R.string.plan_select_date),
            onClick = onSelectDateClick,
            modifier = Modifier.padding(
                horizontal = MemoripPadding.PaddingXLarge,
                vertical = MemoripPadding.PaddingSmall
            )
        )
    }
}

@Composable
private fun NotSelectedIcon(
    modifier: Modifier = Modifier
) {
    val mediumShape = MemoripTheme.shapes.roundedMedium
    val ambientShadowColor = MemoripTheme.colors.black.copy(alpha = 12f)
    val spotShadowColor = MemoripTheme.colors.black.copy(alpha = 12f)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size = ICON_BOX_SIZE)
                .background(
                    color = MemoripTheme.colors.primary.copy(alpha = ICON_BOX_DECORATION_ALPHA),
                    shape = MemoripTheme.shapes.roundedMax
                )
                .border(
                    width = MemoripLineWidth.Thin,
                    color = MemoripTheme.colors.primary,
                    shape = MemoripTheme.shapes.roundedMax
                )
                .clip(shape = MemoripTheme.shapes.roundedMax),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size = INNER_BOX_SIZE)
                    .rotate(ROTATE_DEGREE)
                    .graphicsLayer {
                        rotationZ = ROTATE_DEGREE
                        shadowElevation = ELEVATION_ALPHA
                        shape = mediumShape
                        clip = false


                        this.ambientShadowColor = ambientShadowColor
                        this.spotShadowColor = spotShadowColor
                    }
                    .background(
                        color = MemoripTheme.colors.background,
                        shape = mediumShape
                    )
            )
        }
        Icon(
            modifier = Modifier.size(size = ICON_SIZE),
            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_today),
            contentDescription = null,
            tint = MemoripTheme.colors.primary
        )
    }
}

@Composable
private fun NotSelectedDescription(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = MemoripPadding.PaddingXXXLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
    ) {
        Text(
            text = stringResource(R.string.plan_not_select_date_title),
            color = MemoripTheme.colors.onSurface,
            style = MemoripTheme.typography.titleBold20
        )
        Text(
            text = stringResource(R.string.plan_not_select_date_description),
            color = MemoripTheme.colors.gray,
            textAlign = TextAlign.Center,
            style = MemoripTheme.typography.labelRegular14
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateNotSelectedContentPreview() {
    MemoripTheme {
        DateNotSelectedContent(onSelectDateClick = {})
    }
}