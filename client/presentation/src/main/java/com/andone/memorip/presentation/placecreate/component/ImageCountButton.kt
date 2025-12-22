package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripIconSize

@Composable
fun ImageCountButton(
    current: Int,
    max: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = MemoripIconSize.IconButton, height = MemoripIconSize.IconButton)
            .border(
                width = MemoripBorderWidth.Thin,
                color = MemoripTheme.colors.gray,
                shape = MemoripTheme.shapes.roundedSmall
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_photo_camera),
            contentDescription = stringResource(R.string.place_create_add_image_content_description),
        )

        Spacer(modifier = Modifier.height(height = MemoripSpace.SpaceXXSmall))

        Text(text = stringResource(
            R.string.place_create_image_count_format,
            current,
            max
        ))
    }
}

@Preview
@Composable
private fun ImageCountButtonPreview(){
    MemoripTheme {
        ImageCountButton(
            current = 0,
            max = 10,
            onClick = { },
        )
    }
}