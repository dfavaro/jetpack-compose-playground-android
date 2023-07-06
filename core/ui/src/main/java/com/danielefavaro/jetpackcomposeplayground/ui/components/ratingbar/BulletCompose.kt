package com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference

@Composable
internal fun BulletCompose(
    size: Dp,
    borderWidth: Dp,
    borderColor: Color,
    backgroundColor: () -> Color = { Color.Gray }
) {
    Box(
        modifier = Modifier
            .size(size)
            .border(borderWidth, borderColor, CircleShape)
            .clip(CircleShape)
            .drawBehind {
                drawCircle(backgroundColor.invoke())
            }
    )
}
