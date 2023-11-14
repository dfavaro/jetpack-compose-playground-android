package com.danielefavaro.jetpackcomposeplayground.core.ui.components.ratingbignumber

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.AnimationModel
import com.danielefavaro.jetpackcomposeplayground.core.ui.components.model.RatingTransitionState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val SHAPE_EDGES = 10

@Composable
internal fun BigNumberCanvas(
    animationModel: AnimationModel,
    size: Dp,
    transition: Transition<RatingTransitionState>,
    content: @Composable () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    val animatedDegree: Float by transition.animateFloat(
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
            RatingTransitionState.Initial -> 0f
            RatingTransitionState.Rated -> animationModel.targetValue
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .size(size)
                .aspectRatio(1f)
                .graphicsLayer {
                    rotationZ = animatedDegree
                },
//                .graphicsLayer(rotationZ = animatedDegree),
            onDraw = {
                val radius = 0.10f * min(
                    this.size.width,
                    this.size.height
                ) // each edge going in of 10 points
                val angle =
                    2.0 * PI / SHAPE_EDGES // 2.0 * PI = 2 radians -> 360 degrees / amount of edges

                val path = Path().apply {
                    moveTo(
                        (this@Canvas.size.width / 2f + (this@Canvas.size.width / 2f - radius) * cos(
                            0.0
                        )).toFloat(),
                        (this@Canvas.size.height / 2f + (this@Canvas.size.height / 2f - radius) * sin(
                            0.0
                        )).toFloat()
                    )
                    for (i in 1..SHAPE_EDGES) {
                        val x1 =
                            (this@Canvas.size.width / 2f + this@Canvas.size.width / 2f * cos(angle * i - angle / 2)).toFloat()
                        val y1 =
                            (this@Canvas.size.height / 2f + this@Canvas.size.height / 2f * sin(angle * i - angle / 2)).toFloat()
                        val x2 =
                            (this@Canvas.size.width / 2f + (this@Canvas.size.width / 2f - radius) * cos(
                                angle * i
                            )).toFloat()
                        val y2 =
                            (this@Canvas.size.height / 2f + (this@Canvas.size.height / 2f - radius) * sin(
                                angle * i
                            )).toFloat()
                        lineTo(x1, y1)
                        lineTo(x2, y2)
                    }

                    close()
                }

                drawIntoCanvas { canvas ->
                    canvas.drawOutline(
                        outline = Outline.Generic(path),
                        paint = Paint().apply {
                            color = backgroundColor
                            pathEffect = PathEffect.cornerPathEffect(40f)
                        }
                    )
                }
            }
        )
        content.invoke()
    }
}
