package com.andone.memorip.presentation.screen.triplist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.triplist.component.BentoGridSpec.COLUMNS
import com.andone.memorip.presentation.screen.triplist.component.BentoGridSpec.RATIO
import com.andone.memorip.presentation.screen.triplist.component.BentoGridSpec.ROWS
import com.andone.memorip.presentation.screen.triplist.model.TripItem
import com.andone.memorip.presentation.screen.triplist.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme

private object BentoGridSpec {
    const val COLUMNS = 5
    const val ROWS = 3
    const val RATIO = 5f / 3f
}

@Composable
fun TripLayout(
    items: List<TripItem>,
    modifier: Modifier = Modifier,
    aspectRatio: Float = RATIO,
    columns: Int = COLUMNS,
    rows: Int = ROWS,
    content: @Composable (TripItem) -> Unit
) {
    Layout(
        modifier = modifier,
        content = { items.forEach { content(it) } }
    ) { measurables, constraints ->

        val totalWidth = constraints.maxWidth
        val totalHeight = (totalWidth / aspectRatio).toInt()

        val cellWidth = totalWidth / columns
        val cellHeight = totalHeight / rows

        val columnHeights = IntArray(size = columns)

        val placedItems = mutableListOf<Place>()

        measurables.forEachIndexed { index, measurable ->
            val item = items[index]

            var bestCol = -1
            var bestBaseRow = Int.MAX_VALUE

            for (c in 0..columns - item.colSpan) {
                val baseRow = (c until c + item.colSpan).maxOf { columnHeights[it] }
                if (baseRow + item.rowSpan > rows) continue

                if (baseRow < bestBaseRow) {
                    bestBaseRow = baseRow
                    bestCol = c
                }
            }

            if (bestCol != -1) {
                val width = cellWidth * item.colSpan
                val height = cellHeight * item.rowSpan

                val placeable = measurable.measure(
                    constraints = constraints.copy(
                        minWidth = width, maxWidth = width,
                        minHeight = height, maxHeight = height
                    )
                )

                for (cc in bestCol until bestCol + item.colSpan) {
                    columnHeights[cc] = bestBaseRow + item.rowSpan
                }

                placedItems += Place(
                    placeable = placeable,
                    row = bestBaseRow,
                    col = bestCol
                )
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
private fun TripLayoutPreview() {
    val items = listOf(
        TripItem(colSpan = 1, rowSpan = 1),
        TripItem(colSpan = 1, rowSpan = 1),
        TripItem(colSpan = 1, rowSpan = 3),
        TripItem(colSpan = 2, rowSpan = 2),
        TripItem(colSpan = 2, rowSpan = 2),
        TripItem(colSpan = 1, rowSpan = 1),
        TripItem(colSpan = 1, rowSpan = 1)
    )
    MemoripTheme {
        TripLayout(
            items = items,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MemoripTheme.colors.white)
        ) { item ->
            ImageCard(item = item)
        }
    }
}

