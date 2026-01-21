package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun ImageCountButton(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = MemoripIconSize.IconButton, height = MemoripIconSize.IconButton)
            .border(
                width = MemoripLineWidth.Thin,
                color = MemoripTheme.colors.gray,
                shape = MemoripTheme.shapes.roundedSmall
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_photo_camera),
            contentDescription = stringResource(R.string.place_create_add_image_content_description),
        )

        Spacer(modifier = Modifier.height(height = MemoripSpace.SpaceXXSmall))

        Text(
            text = stringResource(
                R.string.place_create_image_count_format,
                current,
                max
            )
        )
    }
}

@Preview
@Composable
private fun ImageCountButtonPreview() {
    MemoripTheme {
        ImageCountButton(
            current = 0,
            max = 10
        )
    }
}