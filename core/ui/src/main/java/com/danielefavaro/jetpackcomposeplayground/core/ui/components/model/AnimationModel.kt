package com.danielefavaro.jetpackcomposeplayground.core.ui.components.model

internal data class AnimationModel(
    val targetValue: Float,
    val animDuration: Int,
    val animDelay: Int = 0
)
