package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.screen.triplist.model.TripItem
import com.andone.memorip.presentation.screen.triplist.model.LayoutBlock
import com.andone.memorip.presentation.util.BENTO5x3.VISIBLE

private object BENTO5x3 {
    const val VISIBLE = 7
}

fun buildBento5x3Items(images: List<String>): List<TripItem> {
    val visible = images.take(VISIBLE)
    val overflowCount = (images.size - VISIBLE).coerceAtLeast(0)

    val patterns = BENTO_5x3_PATTERNS[visible.size]
        ?: return emptyList()

    return patterns.map { pattern ->
        TripItem(
            colSpan = pattern.colSpan,
            rowSpan = pattern.rowSpan,
            imageUrl = visible[pattern.index],
            overNumber = if (pattern.isOverflowTarget) overflowCount else 0
        )
    }
}

private val BENTO_5x3_PATTERNS = mapOf(
    1 to listOf(
        LayoutBlock(5, 3, 0)
    ),

    2 to listOf(
        LayoutBlock(3, 3, 0),
        LayoutBlock(2, 3, 1)
    ),

    3 to listOf(
        LayoutBlock(3, 3, 0),
        LayoutBlock(2, 2, 1),
        LayoutBlock(2, 1, 2)
    ),

    4 to listOf(
        LayoutBlock(3, 3, 0),
        LayoutBlock(2, 2, 1),
        LayoutBlock(1, 1, 2),
        LayoutBlock(1, 1, 3)
    ),

    5 to listOf(
        LayoutBlock(2, 1, 0),
        LayoutBlock(1, 3, 1),
        LayoutBlock(2, 2, 2),
        LayoutBlock(2, 2, 3),
        LayoutBlock(2, 1, 4)
    ),

    6 to listOf(
        LayoutBlock(2, 1, 0),
        LayoutBlock(1, 3, 1),
        LayoutBlock(2, 2, 2),
        LayoutBlock(2, 2, 3),
        LayoutBlock(1, 1, 4),
        LayoutBlock(1, 1, 5)
    ),

    7 to listOf(
        LayoutBlock(1, 1, 0),
        LayoutBlock(1, 1, 1),
        LayoutBlock(1, 3, 2),
        LayoutBlock(2, 2, 3),
        LayoutBlock(2, 2, 4),
        LayoutBlock(1, 1, 5),
        LayoutBlock(1, 1, 6, isOverflowTarget = true)
    )
)
