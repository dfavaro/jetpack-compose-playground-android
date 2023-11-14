package com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model

import androidx.compose.ui.graphics.Color

data class SectorDatasetModel(
    val scoreMaxLabel: String,
    val scoreMinLabel: String,
    val sectorList: List<SectorModel>
)

data class SectorModel(
    val threshold: Double,
    val label: String,
    val color: Color
)
