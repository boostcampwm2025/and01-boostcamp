package com.andone.memorip.presentation.screen.plan.model

import androidx.compose.ui.geometry.Offset
import com.andone.memorip.presentation.model.Place

data class DraggablePlace(
    val place: Place,
    val originPos: Offset,
    val offset: Offset
)
