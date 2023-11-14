package com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbignumber

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.AnimationModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.RatingTransitionState
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.SCORE_MAX
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.SCORE_MIN
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.text.SimpleText

@Composable
fun RatingBigNumberCompose(
    score: Float,
    animDuration: Int,
    animDelay: Int
) {
    val animationModel = remember {
        AnimationModel(
            targetValue = score.coerceIn(
                minimumValue = SCORE_MIN.toFloat(), maximumValue = SCORE_MAX.toFloat()
            ), animDuration = animDuration, animDelay = animDelay
        )
    }

    var currentState: RatingTransitionState by remember {
        mutableStateOf(RatingTransitionState.Initial)
    }
    val transition = updateTransition(currentState, label = "")

    val animatedScore: Int by transition.animateInt(label = "", transitionSpec = {
        tween(
            durationMillis = animationModel.animDuration,
            delayMillis = animationModel.animDelay,
            easing = FastOutSlowInEasing
        )
    }) { state ->
        when (state) {
            RatingTransitionState.Initial -> 0
            RatingTransitionState.Rated -> animationModel.targetValue.toInt()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
//        delay(5000) // layout inspector startup time-ish
        currentState = RatingTransitionState.Rated
    })

    BigNumberCanvas(
        animationModel = AnimationModel(
            targetValue = (animationModel.targetValue * 180 / 10),
            animDuration = animationModel.animDuration,
            animDelay = animationModel.animDelay
        ),
        size = 120.dp,
        transition = transition
    ) {
        SimpleText(
            fontSize = 30.sp, color = MaterialTheme.colorScheme.onPrimary
        ) {
            animatedScore.toString()
        }
    }
}
