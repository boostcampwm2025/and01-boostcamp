package com.andone.memorip.presentation.screen.triplist.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

private object CreateNewTripCardDimen {
    val IconSize = 40.dp
    val TextSpacing = 4.dp
    val ButtonCornerRadius = 8.dp
}

@Composable
fun CreateNewTripCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(memoripShapes.roundedMedium)
            .background(MemoripTheme.colors.primaryContainer)
            .clickable(onClick = onClick)
            .padding(
                horizontal = MemoripPadding.PaddingMedium,
                vertical = MemoripPadding.PaddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_outline_trip),
                contentDescription = null,
                tint = MemoripTheme.colors.primary,
                modifier = Modifier.size(CreateNewTripCardDimen.IconSize)
            )
            Spacer(Modifier.width(MemoripPadding.PaddingSmall))
            Column(
                verticalArrangement = Arrangement.spacedBy(CreateNewTripCardDimen.TextSpacing)
            ) {
                Text(
                    text = stringResource(R.string.trip_list_create_new_trip),
                    style = MemoripTheme.typography.labelMedium16,
                    color = MemoripTheme.colors.onSurface
                )
                Text(
                    text = stringResource(R.string.trip_list_create_new_trip_description),
                    style = MemoripTheme.typography.bodyRegular12,
                    color = MemoripTheme.colors.onSurface
                )
            }
        }
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MemoripTheme.colors.primary,
                contentColor = MemoripTheme.colors.background
            ),
            shape = RoundedCornerShape(CreateNewTripCardDimen.ButtonCornerRadius),
            modifier = Modifier.wrapContentWidth(),
            contentPadding = ButtonDefaults.TextButtonContentPadding
        ) {
            Text(
                text = stringResource(R.string.trip_list_create_new_trip_button),
                style = MemoripTheme.typography.labelMedium14,
                color = MemoripTheme.colors.white
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateNewTripCardPreview() {
    MemoripTheme {
        CreateNewTripCard(
            onClick = {}
        )
    }
}
