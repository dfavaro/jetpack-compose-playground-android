package com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.danielefavaro.jetpackcomposeplayground.core.ui.R
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.AnimationModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.RatingTransitionState
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.SCORE_MAX
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.SCORE_MIN
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model.SectorDatasetModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model.SectorModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.theme.RatingBarGreen
import com.danielefavaro.jetpackcomposeplayground.core.ui.theme.defaultMargin

private const val LAST_THRESHOLD = 1.0

@Composable
fun RatingCompose(
    score: Float,
    animDuration: Int,
    animDelay: Int,
    sectorDatasetModel: SectorDatasetModel
) {
    val context = LocalContext.current

    var currentState: RatingTransitionState by remember {
        mutableStateOf(RatingTransitionState.Initial)
    }
    val transition = updateTransition(currentState, label = "")

    val sectorList: List<SectorModel> = remember {
        sectorDatasetModel.sectorList.toMutableList().apply {
            // adding last sector if missing
            if (sectorDatasetModel.sectorList.isEmpty() || sectorDatasetModel.sectorList.last().threshold != LAST_THRESHOLD) {
                add(
                    SectorModel(
                        threshold = LAST_THRESHOLD,
                        label = context.getString(R.string.rating_not_applicable),
                        color = RatingBarGreen
                    )
                )
            }
        }
    }

    val animationModel = remember {
        AnimationModel(
            targetValue = score.coerceIn(
                minimumValue = SCORE_MIN.toFloat(),
                maximumValue = SCORE_MAX.toFloat()
            ),
            animDuration = animDuration,
            animDelay = animDelay
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(defaultMargin)
    ) {
        RatingHeaderCompose(
            animationModel = animationModel,
            sectorList = sectorList,
            scoreMin = SCORE_MIN,
            scoreMax = SCORE_MAX,
            transition = transition
        )
        RatingBarCompose(
            animationModel = animationModel,
            sectorList = sectorList,
            scoreMin = SCORE_MIN,
            scoreMinLabel = sectorDatasetModel.scoreMinLabel,
            scoreMax = SCORE_MAX,
            scoreMaxLabel = sectorDatasetModel.scoreMaxLabel,
            transition = transition,
            onRatingBarDraw = { currentState = RatingTransitionState.Rated }
        )
    }
}
