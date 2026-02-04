package com.andone.memorip.presentation.screen.triplist.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun TripListSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(height = MemoripHeight.SearchBoxHeight),
        textStyle = MemoripTheme.typography.labelRegular14.copy(
            color = MemoripTheme.colors.onSurface
        ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MemoripTheme.colors.primaryContainer,
                        shape = memoripShapes.roundedXSmall
                    )
                    .padding(horizontal = MemoripPadding.PaddingSmall)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.trip_list_search_placeholder),
                        style = MemoripTheme.typography.labelRegular14,
                        color = MemoripTheme.colors.gray1
                    )
                }
                innerTextField()
                Spacer(modifier = Modifier.weight(1f))
                if (value.isNotEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.trip_list_search_clear_description),
                        modifier = Modifier
                            .size(size = MemoripIconSize.IconSizeXSmall)
                            .clip(CircleShape)
                            .clickable { onValueChange("") },
                        tint = MemoripTheme.colors.onSurface
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.trip_list_search_content_description),
                        modifier = Modifier.size(size = MemoripIconSize.IconSizeXSmall),
                        tint = MemoripTheme.colors.onSurface
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripListSearchBarPreview() {
    MemoripTheme {
        TripListSearchBar(
            value = "",
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripListSearchBarWithTextPreview() {
    MemoripTheme {
        TripListSearchBar(
            value = "제주도",
            onValueChange = {}
        )
    }
}
