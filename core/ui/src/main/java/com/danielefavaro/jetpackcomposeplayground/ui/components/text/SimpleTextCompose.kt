package com.danielefavaro.jetpackcomposeplayground.ui.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

@Composable
fun SimpleText(
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = Color.Unspecified,
    text: () -> String
) {
    Text(
        fontSize = fontSize,
        color = color,
        text = text.invoke()
    )
}
