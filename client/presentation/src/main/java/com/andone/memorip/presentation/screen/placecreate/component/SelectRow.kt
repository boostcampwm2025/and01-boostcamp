package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.ReadOnlyMapView
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.screen.placecreate.component.SelectRowConstant.MAP_VIEW_ASPECT_RATIO
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

private object SelectRowConstant {
    const val MAP_VIEW_ASPECT_RATIO = 2f
}

@Composable
fun SelectRow(
    label: String,
    value: String,
    leadingIcon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    location: LocationUiModel? = null,
    trailingIcon: Painter? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MemoripTheme.shapes.roundedSmall,
        colors = CardDefaults.cardColors(containerColor = MemoripTheme.colors.gray4),
        border = BorderStroke(
            width = MemoripLineWidth.Thin,
            brush = SolidColor(MemoripTheme.colors.gray2)
        )
    ) {
        location?.let {
            ReadOnlyMapView(
                modifier = Modifier.aspectRatio(MAP_VIEW_ASPECT_RATIO),
                location = it
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(MemoripIconSize.IconSizeLarge)
                    .shadow(
                        elevation = MemoripShadow.Small,
                        shape = CircleShape,
                    )
                    .background(MemoripTheme.colors.white, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(MemoripIconSize.IconSizeMedium),
                    tint = MemoripTheme.colors.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
            ) {
                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MemoripTheme.typography.bodyLarge,
                )

                Text(
                    text = value,
                    color = MemoripTheme.colors.gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MemoripTheme.typography.bodySmall,
                )
            }

            trailingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = MemoripTheme.colors.gray3
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectRowPreview() {
    SelectRow(
        label = "태그",
        onClick = { },
        value = "맛집",
        leadingIcon = painterResource(R.drawable.ic_tag),
        trailingIcon = painterResource(R.drawable.ic_chevron_forward)
    )
}
