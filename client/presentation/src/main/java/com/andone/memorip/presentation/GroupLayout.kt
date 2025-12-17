package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.domain.group.GroupItem
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun GroupLayout(
    items: List<GroupItem>,
    modifier: Modifier = Modifier,
    columns: Int = 5,
    rows: Int = 3,
    content: @Composable (GroupItem) -> Unit
) {
    Layout(
        modifier = modifier,
        content = { items.forEach { content(it) } }
    ) { measurables, constraints ->

        val cellWidth = constraints.maxWidth / columns
        val cellHeight = constraints.maxWidth / rows

        val occupied = Array(rows) { BooleanArray(columns) }

        data class Placed(
            val placeable: Placeable,
            val row: Int,
            val col: Int,
            val rowSpan: Int,
            val colSpan: Int
        )

        val placedItems = mutableListOf<Placed>()

        fun canPlace(r: Int, c: Int, rs: Int, cs: Int): Boolean {
            if (r + rs > rows || c + cs > columns) return false
            for (rr in r until r + rs) {
                for (cc in c until c + cs) {
                    if (occupied[rr][cc]) return false
                }
            }
            return true
        }

        fun mark(r: Int, c: Int, rs: Int, cs: Int) {
            for (rr in r until r + rs) {
                for (cc in c until c + cs) {
                    occupied[rr][cc] = true
                }
            }
        }

        measurables.forEachIndexed { index, measurable ->
            val item = items[index]

            outer@ for (r in 0 until rows) {
                for (c in 0 until columns) {
                    if (canPlace(r, c, item.rowSpan, item.colSpan)) {
                        val width = cellWidth * item.colSpan
                        val height = cellHeight * item.rowSpan

                        val placeable = measurable.measure(
                            constraints.copy(
                                minWidth = width,
                                maxWidth = width,
                                minHeight = height,
                                maxHeight = height
                            )
                        )

                        mark(r, c, item.rowSpan, item.colSpan)
                        placedItems += Placed(placeable, r, c, item.rowSpan, item.colSpan)
                        break@outer
                    }
                }
            }
        }

        val layoutHeight = rows * cellHeight

        layout(constraints.maxWidth, layoutHeight) {
            placedItems.forEach {
                it.placeable.placeRelative(
                    x = it.col * cellWidth,
                    y = it.row * cellHeight
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupLayoutPreview() {
    val items = listOf(
        GroupItem(colSpan = 1, rowSpan = 1),
        GroupItem(colSpan = 1, rowSpan = 1),
        GroupItem(colSpan = 1, rowSpan = 3),
        GroupItem(colSpan = 2, rowSpan = 2),
        GroupItem(colSpan = 2, rowSpan = 2),
        GroupItem(colSpan = 1, rowSpan = 1),
        GroupItem(colSpan = 1, rowSpan = 1)
    )
    MemoripTheme {
        GroupLayout(
            items = items,
            modifier = Modifier
                .fillMaxWidth()
                .background(MemoripTheme.colors.white)
        ) { item ->
            ImageCard(item)
        }
    }
}

