package com.andone.memorip.domain.group

fun buildGroupItems(
    images: List<String>,
    columns: Int = 5,
    rows: Int = 3
): List<GroupItem> {
    return when (images.size) {
        1 -> listOf(
            GroupItem(columns, rows, images[0])
        )
        2 -> buildTwo(images, columns, rows)
        3 -> buildThree(images, columns, rows)
        in 4..6 -> buildFromPatternTable(images, columns, rows)
        else -> buildOverflow(images, columns, rows)
    }
}

private fun buildTwo(
    images: List<String>,
    columns: Int,
    rows: Int
): List<GroupItem> {
    return listOf(
        GroupItem(colSpan = 2, rowSpan = 2, imageUrl = images[0]),
        GroupItem(colSpan = 2, rowSpan = 2, imageUrl = images[1])
    )
}

private fun buildThree(
    images: List<String>,
    columns: Int,
    rows: Int
): List<GroupItem> {
    return listOf(
        GroupItem(colSpan = 3, rowSpan = rows, imageUrl = images[0]),
        GroupItem(colSpan = 2, rowSpan = 1, imageUrl = images[1]),
        GroupItem(colSpan = 2, rowSpan = 1, imageUrl = images[2])
    )
}
