package com.andone.memorip.domain.model

data class TimeBlock(
    val id: String,
    val startMinute: Int,
    val durationMinute: Int,
    val day: Int = 1,
    val column: Int = 0
) {
    init {
        require(durationMinute > 0)
    }
    val endMinute: Int
        get() = startMinute + durationMinute

    fun canMoveTo(
        newStartMinute: Int,
        blocks: List<TimeBlock>,
        totalMinutes: Int
    ): Boolean {
        val maxStart = totalMinutes - durationMinute
        val start = newStartMinute
            .coerceAtLeast(0)
            .coerceAtMost(maxStart)
        val end = start + durationMinute

        return blocks
            .filter { it.id != id && it.day == day && it.column == column }
            .none { other ->
                start < other.endMinute && other.startMinute < end
            }
    }

    fun movedTo(newStartMinute: Int, totalMinutes: Int): TimeBlock {
        val maxStart = totalMinutes - durationMinute
        val start = newStartMinute
            .coerceIn(0, maxStart)

        return copy(
            day = (start / 1440) + 1,
            startMinute = start
        )
    }
}