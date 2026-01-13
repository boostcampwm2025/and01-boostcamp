package com.andone.memorip.presentation.screen.placelist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = MemoripHeight.SearchBoxHeight)
            .padding(end = MemoripPadding.PaddingMedium),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MemoripTheme.colors.primaryContainer,
                        shape = memoripShapes.roundedXLarge
                    )
                    .padding(horizontal = MemoripPadding.PaddingSmall)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.place_list_search),
                    modifier = Modifier.size(size = MemoripIconSize.IconSizeSmall)
                )
                Spacer(Modifier.width(width = MemoripSpace.SpaceXSmall))
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun SearchTextFieldPreview(){
    MemoripTheme {
        SearchTextField(
            value = "검색",
            onValueChange = {}
        )
    }
}
