package com.andone.memorip.presentation.screen.placecreate.model

enum class PlaceCreateStep(val stepIndex: Int? = null) {
    SelectImage(1),
    SelectLocation(2),
    PlaceCreate(3),
    SelectCategory,
    SelectTrip
}