package com.danielefavaro.jetpackcomposeplayground.main.state

import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model.SectorModel

data class RatingState(
    val score: Float,
    val animDuration: Int,
    val animDelay: Int,
    val scoreMinLabel: String,
    val scoreMaxLabel: String,
    val sectorList: List<SectorModel>
)
