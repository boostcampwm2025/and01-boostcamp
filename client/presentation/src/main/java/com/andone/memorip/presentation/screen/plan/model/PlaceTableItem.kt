package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.TagUiModel
import java.time.LocalTime

data class PlaceTableItem(
    override val id: String,
    override val day: Int,
    override val startTime: LocalTime,
    override val endTime: LocalTime,

    val name: String,
    val address: String,
    val tags: List<TagUiModel>,
    val imageUrl: String
) : TableItem
