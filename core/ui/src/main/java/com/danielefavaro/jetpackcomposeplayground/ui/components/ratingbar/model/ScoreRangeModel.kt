package com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

internal data class ScoreRangeModel(
    val start: Int,
    val end: Int,
    val color: Color,
    val shape: Shape,
    val weight: Float
)

internal fun getSectorWeight(
    rangeStart: Int,
    rangeEnd: Int,
    scoreMax: Int,
    scoreMin: Int
): Float = (rangeEnd - rangeStart).toFloat() / (scoreMax - scoreMin)
