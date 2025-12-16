package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripTheme

data class BentoItem(
    val id: Int,
    val span: Int,
    val height: Dp
)


val items = listOf(
    BentoItem(1, span = 2, height = 220.dp),
    BentoItem(2, span = 1, height = 110.dp),
    BentoItem(3, span = 1, height = 110.dp),
    BentoItem(4, span = 1, height = 220.dp),
    BentoItem(5, span = 2, height = 110.dp),
    BentoItem(6, span = 5, height = 220.dp)
)

@Composable
fun BentoCard(item: BentoItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(item.height)
            .background(Color.LightGray)
            .border(1.dp, Color.Black)
    ) {
        Text(
            text = "Item ${item.id}",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BentoLayout(
    items: List<BentoItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable (BentoItem) -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            items.forEach { content(it) }
        }
    ) { measurables, constraints ->

        val columnWidth = constraints.maxWidth / columns
        val columnHeights = IntArray(columns) { 0 }

        data class Placed(
            val placeable: Placeable,
            val col: Int,
            val span: Int
        )

        val placedItems = mutableListOf<Placed>()

        measurables.forEachIndexed { index, measurable ->
            val item = items[index]
            val span = item.span.coerceAtMost(columns)

            var bestCol = 0
            var minHeight = Int.MAX_VALUE

            for (c in 0..columns - span) {
                val height = (c until c + span).maxOf { columnHeights[it] }
                if (height < minHeight) {
                    minHeight = height
                    bestCol = c
                }
            }

            val width = columnWidth * span

            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = width,
                    maxWidth = width
                )
            )

            val newHeight = minHeight + placeable.height
            for (c in bestCol until bestCol + span) {
                columnHeights[c] = newHeight
            }

            placedItems += Placed(placeable, bestCol, span)
        }

        val layoutHeight = columnHeights.maxOrNull() ?: constraints.minHeight

        layout(constraints.maxWidth, layoutHeight) {
            val colY = IntArray(columns) { 0 }

            placedItems.forEach { (placeable, col, span) ->
                val y = (col until col + span).maxOf { colY[it] }
                val x = col * columnWidth

                placeable.placeRelative(x, y)

                val bottom = y + placeable.height
                for (c in col until col + span) {
                    colY[c] = bottom
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BentoPreview() {
    MemoripTheme {
        BentoLayout(
            items = items,
            columns = 5,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) { item ->
            BentoCard(item)
        }
    }
}

