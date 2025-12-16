package com.andone.memorip.domain.group

fun buildBento5x3Items(images: List<String>): List<GroupItem> {
    val visible = images.take(7)
    val overflowCount = (images.size - 7).coerceAtLeast(0)

    val patterns = BENTO_5x3_PATTERNS[visible.size]
        ?: return emptyList()

    return patterns.map { pattern ->
        GroupItem(
            colSpan = pattern.colSpan,
            rowSpan = pattern.rowSpan,
            imageUrl = visible[pattern.index],
            overNumber = if (pattern.isOverflowTarget) overflowCount else null
        )
    }
}

private val BENTO_5x3_PATTERNS = mapOf(
    1 to listOf(
        BentoPattern(5, 3, 0)
    ),

    2 to listOf(
        BentoPattern(3, 3, 0),
        BentoPattern(2, 3, 1)
    ),

    3 to listOf(
        BentoPattern(3, 3, 0),
        BentoPattern(2, 2, 1),
        BentoPattern(2, 1, 2)
    ),

    4 to listOf(
        BentoPattern(3, 3, 0),
        BentoPattern(2, 2, 1),
        BentoPattern(1, 1, 2),
        BentoPattern(1, 1, 3)
    ),

    5 to listOf(
        BentoPattern(2, 2, 0),
        BentoPattern(1, 3, 1),
        BentoPattern(2, 1, 2),
        BentoPattern(2, 1, 3),
        BentoPattern(2, 2, 4)
    ),

    6 to listOf(
        BentoPattern(2, 2, 0),
        BentoPattern(1, 3, 1),
        BentoPattern(1, 1, 2),
        BentoPattern(1, 1, 3),
        BentoPattern(2, 1, 4),
        BentoPattern(2, 2, 5)
    ),

    7 to listOf(
        BentoPattern(2, 2, 0),
        BentoPattern(1, 3, 1),
        BentoPattern(1, 1, 2),
        BentoPattern(1, 1, 3),
        BentoPattern(1, 1, 4),
        BentoPattern(1, 1, 5),
        BentoPattern(2, 2, 6, isOverflowTarget = true)
    )
)
