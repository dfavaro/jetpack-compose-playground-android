package com.danielefavaro.jetpackcomposeplayground.app

import com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model.SectorModel

data class RatingState(
    val score: Float,
    val animDuration: Int,
    val animDelay: Int,
    val scoreMinLabel: String,
    val scoreMaxLabel: String,
    val sectorList: List<SectorModel>
)
