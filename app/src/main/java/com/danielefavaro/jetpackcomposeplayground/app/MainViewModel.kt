package com.danielefavaro.jetpackcomposeplayground.app

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model.SectorModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.theme.RatingBarGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val ANIMATION_DURATION = 2000
private const val ANIMATION_DELAY = 750

class MainViewModel : ViewModel() {
    val ratingState: StateFlow<RatingState> = MutableStateFlow(
        RatingState(
            score = 58f,
            scoreMaxLabel = "100",
            scoreMinLabel = "0",
            sectorList = listOf(
                SectorModel(
                    threshold = 0.3,
                    label = "Low",
                    color = Color(255, 92, 80)
                ),
                SectorModel(
                    threshold = 0.7,
                    label = "Average",
                    color = Color(254, 192, 86)
                ),
                SectorModel(
                    threshold = 1.0,
                    label = "Good",
                    RatingBarGreen
                )
            ),
            animDuration = ANIMATION_DURATION,
            animDelay = ANIMATION_DELAY
        )
    )
}
