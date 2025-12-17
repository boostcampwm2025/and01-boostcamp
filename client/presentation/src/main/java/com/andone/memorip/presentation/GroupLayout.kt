package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.domain.group.GroupItem
import com.andone.memorip.presentation.BentoGridSpec.COLUMNS
import com.andone.memorip.presentation.BentoGridSpec.ROWS
import com.andone.memorip.presentation.theme.MemoripTheme

object BentoGridSpec {
    const val COLUMNS = 5
    const val ROWS = 3
}

@Composable
fun GroupLayout(
    items: List<GroupItem>,
    modifier: Modifier = Modifier,
    columns: Int = COLUMNS,
    rows: Int = ROWS,
    content: @Composable (GroupItem) -> Unit
) {
    Layout(
        modifier = modifier,
        content = { items.forEach { content(it) } }
    ) { measurables, constraints ->

        val cellWidth = constraints.maxWidth / columns
        val cellHeight = constraints.maxWidth / rows

        val columnHeights = IntArray(columns)

        data class Placed(
            val placeable: Placeable,
            val row: Int,
            val col: Int
        )

        val placedItems = mutableListOf<Placed>()

        measurables.forEachIndexed { index, measurable ->
            val item = items[index]

            for (c in 0..columns - item.colSpan) {

                val baseRow = (c until c + item.colSpan)
                    .maxOf { columnHeights[it] }

                if (baseRow + item.rowSpan > rows) continue

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

                for (cc in c until c + item.colSpan) {
                    columnHeights[cc] = baseRow + item.rowSpan
                }

                placedItems += Placed(placeable, baseRow, c)
                break
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

