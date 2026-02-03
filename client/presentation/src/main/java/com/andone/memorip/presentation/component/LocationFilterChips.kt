package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LocationFilterChips(
    selected: List<String> = emptyList(),
    unselected: List<String> = emptyList()
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        selected.forEach {
            FilterChip(text = it)
        }
        unselected.forEach {
            FilterChip(text = it, selected = false)
        }
    }
}

@Preview
@Composable
private fun LocationFilterChipsPreview(){
    MemoripTheme {
        LocationFilterChips(
            selected = listOf("전체"),
            unselected = listOf("서울특별시", "경기도", "강원도", "제주특별자치도", "부산광역시")
        )
    }
}

@Preview
@Composable
private fun LocationFilterChipsPreviewOnSeoul(){
    MemoripTheme {
        LocationFilterChips(
            selected = listOf("서울특별시"),
            unselected = listOf("강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구")
        )
    }
}

@Preview
@Composable
private fun LocationFilterChipsPreviewOnGangnam(){
    MemoripTheme {
        LocationFilterChips(selected = listOf("서울특별시", "강남구"))
    }
}