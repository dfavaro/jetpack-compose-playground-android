package com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.danielefavaro.jetpackcomposeplayground.ui.components.model.AnimationModel
import com.danielefavaro.jetpackcomposeplayground.ui.components.model.RatingTransitionState
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model.ScoreRangeModel
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model.SectorModel
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model.getSectorWeight
import com.danielefavaro.jetpackcomposeplayground.ui.theme.RatingBarBorderColor
import com.danielefavaro.jetpackcomposeplayground.ui.theme.defaultMarginS
import com.danielefavaro.jetpackcomposeplayground.ui.theme.indicatorBorderWidth
import com.danielefavaro.jetpackcomposeplayground.ui.theme.indicatorRadius
import com.danielefavaro.jetpackcomposeplayground.ui.theme.ratingBarHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun RatingBarCompose(
    animationModel: AnimationModel,
    sectorList: List<SectorModel>,
    scoreMin: Int,
    scoreMax: Int,
    scoreMinLabel: String,
    scoreMaxLabel: String,
    transition: Transition<RatingTransitionState>,
    onRatingBarDraw: () -> Unit
) {
    val scoreRanges: List<ScoreRangeModel> = remember {
        List(sectorList.size) { index ->
            val start =
                if (index == 0) scoreMin else (scoreMax * sectorList[index - 1].threshold).toInt()
            val end = (scoreMax * sectorList[index].threshold).toInt()
            ScoreRangeModel(
                start = start,
                end = end,
                color = sectorList[index].color,
                shape = if (index == 0 || index == sectorList.lastIndex) {
                    RoundedCornerShape(
                        topStart = if (index == 0) 10.dp else 0.dp,
                        bottomStart = if (index == 0) 10.dp else 0.dp,
                        topEnd = if (index == sectorList.lastIndex) 10.dp else 0.dp,
                        bottomEnd = if (index == sectorList.lastIndex) 10.dp else 0.dp
                    )
                } else {
                    RectangleShape
                },
                weight = getSectorWeight(
                    rangeStart = start,
                    rangeEnd = end,
                    scoreMin = scoreMin,
                    scoreMax = scoreMax
                )
            )
        }
    }

    val localDensity = LocalDensity.current
    val indicatorHalfWidthDp = remember {
        indicatorRadius + indicatorBorderWidth / 2
    }

    val scoreState: Int by remember {
        mutableStateOf(animationModel.targetValue.toInt())
    }
    var ratingBarWidthDpState: Dp by remember {
        mutableStateOf(0.dp)
    }

    val scoreTransitionDp: Dp by remember {
        derivedStateOf {
            ratingBarWidthDpState * getSectorWeight(
                rangeStart = scoreMin,
                rangeEnd = scoreState, // rating
                scoreMax = scoreMax,
                scoreMin = scoreMin
            ) - indicatorHalfWidthDp
        }
    }

    val animatedScoreDp: Dp by transition.animateDp(
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
            RatingTransitionState.Initial -> indicatorHalfWidthDp.unaryMinus()
            RatingTransitionState.Rated -> scoreTransitionDp
        }
    }

    val bulletColorTransition: Color by remember {
        derivedStateOf {
            (animatedScoreDp + indicatorHalfWidthDp).let { scoreDp ->
                var color: Color = scoreRanges.first().color
                for (i in 0..scoreRanges.lastIndex) {
                    if (scoreDp <= ratingBarWidthDpState * scoreRanges
                            .take(i + 1) // safe to go over
                            .map { it.weight }
                            .reduceRight { a, b -> a + b }
                    ) {
                        color = scoreRanges[i].color
                        break
                    }
                }
                color
            }
        }
    }

    val animatedBulletColor: Color by transition.animateColor(
        label = "",
    ) { state ->
        when (state) {
            RatingTransitionState.Initial -> scoreRanges.first().color
            RatingTransitionState.Rated -> bulletColorTransition
        }
    }

//    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxWidth()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    with(localDensity) {
//                        scope.launch {
//                            delay(5000)  // layout inspector startup time-ish
                            ratingBarWidthDpState = it.size.width.toDp()
                            onRatingBarDraw.invoke()
//                        }
                    }
                }
        ) {
            val (ratingBar, index) = createRefs()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ratingBarHeight)
                    .constrainAs(ratingBar) {
                        top.linkTo(index.top)
                        bottom.linkTo(index.bottom)
                    }
            ) {
                scoreRanges.forEach { range ->
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(
                                getSectorWeight(
                                    rangeStart = range.start,
                                    rangeEnd = range.end,
                                    scoreMax = scoreMax,
                                    scoreMin = scoreMin
                                )
                            )
                            .background(color = range.color, shape = range.shape)
                    )
                }
            }

            Box(modifier = Modifier.constrainAs(index) {
                top.linkTo(parent.top)
                translationX = animatedScoreDp
            }) {
                BulletCompose(
                    size = indicatorRadius * 2,
                    borderWidth = indicatorBorderWidth,
                    borderColor = RatingBarBorderColor,
                    backgroundColor = { animatedBulletColor },
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = defaultMarginS)
        ) {
            scoreRanges.forEachIndexed { index, range ->
                Row(
                    modifier = Modifier.weight(
                        getSectorWeight(
                            rangeStart = range.start,
                            rangeEnd = range.end,
                            scoreMax = scoreMax,
                            scoreMin = scoreMin
                        )
                    ),
                    horizontalArrangement = if (index == 0) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (index == 0) {
                        Text(
                            text = scoreMinLabel,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    if (index == scoreRanges.lastIndex) {
                        Text(
                            text = scoreMaxLabel,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Text(
                            text = range.end.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
