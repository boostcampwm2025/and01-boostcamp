package com.andone.memorip.presentation.screen.selecttrip.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun TripImageGridCard(
    name: String,
    images: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaceAdded: Boolean = false,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TripImageGridMax3(
            images = images,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isPlaceAdded) {
                        Modifier.border(
                            width = MemoripLineWidth.Small,
                            color = MemoripTheme.colors.primary,
                            shape = memoripShapes.roundedSmall
                        )
                    } else {
                        Modifier
                    }
                ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MemoripSpace.SpaceXXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                color = MemoripTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MemoripTheme.typography.bodyBold14
            )
            if (isPlaceAdded) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MemoripTheme.colors.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripImageGridCardPlaceAddedPreview() {
    MemoripTheme {
        TripImageGridCard(
            name = "겨울 여행",
            images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg",
            ),
            onClick = {},
            isPlaceAdded = true,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TripImageGridCardPlaceNotAddedPreview() {
    MemoripTheme {
        TripImageGridCard(
            name = "겨울 여행",
            images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg",
            ),
            onClick = {},
            isPlaceAdded = false,
        )
    }
}