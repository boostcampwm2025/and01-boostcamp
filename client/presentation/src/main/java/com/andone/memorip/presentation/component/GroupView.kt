package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.home.component.GroupLayout
import com.andone.memorip.presentation.util.buildBento5x3Items
import com.andone.memorip.presentation.home.component.ImageCard
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R

@Composable
fun GroupView(
    modifier: Modifier = Modifier,
    name: String = stringResource(R.string.group_view_default_name),
    images: List<String> = emptyList(),
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
    ) {
        Text(text = name)
        GroupLayout(
            items = buildBento5x3Items(images),
            modifier = Modifier
                .fillMaxWidth()
                .background(MemoripTheme.colors.white)
        ) { item ->
            ImageCard(item = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupViewPreview() {
    MemoripTheme {
        GroupView(
            name = "기본 그룹",
            images = List(8) {""}
        )
    }
}