package com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.danielefavaro.jetpackcomposeplayground.core.ui.R
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.AnimationModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.RatingTransitionState
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbar.model.SectorModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.text.SimpleText

@Composable
internal fun RatingHeaderCompose(
    animationModel: AnimationModel,
    sectorList: List<SectorModel>,
    scoreMin: Int,
    scoreMax: Int,
    transition: Transition<RatingTransitionState>
) {
    val context = LocalContext.current

    val scoreLabel: String by remember {
        derivedStateOf {
            var label: String = context.getString(R.string.rating_not_applicable)

            for (index in sectorList.indices) {
                animationModel.targetValue.toInt().let { targetValue ->
                    if (when (index) {
                            0 -> targetValue in scoreMin..(scoreMax * sectorList.first().threshold).toInt()
                            else -> targetValue in (scoreMax * sectorList[index - 1].threshold).toInt()..(scoreMax * sectorList[index].threshold).toInt()
                        }
                    ) {
                        label = sectorList[index].label
                    }
                }
            }
            label
        }
    }

    val animatedScore: Int by transition.animateInt(
        label = "",
        transitionSpec = {
            tween(
                durationMillis = animationModel.animDuration,
                delayMillis = animationModel.animDelay,
                easing = FastOutSlowInEasing
            )
        }
    ) { state ->
        when (state) {
            RatingTransitionState.Initial -> 0
            RatingTransitionState.Rated -> animationModel.targetValue.toInt()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        SimpleText { animatedScore.toString() }
        AnimatedVisibility(
            visible = transition.currentState == transition.targetState // signal for animation end
                    && transition.targetState != RatingTransitionState.Initial,
            enter = slideInVertically { height -> -height } + fadeIn()
        ) {
            SimpleText(text = { scoreLabel })
        }
    }
}
