package com.sachin.lib.picker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.sachin.lib.picker.data.model.HsvColor

/**
 * Hue side bar Component that invokes onHueChanged when the value is mutated.
 *
 * @param modifier modifiers to set to the hue bar.
 * @param currentColor the initial color to set on the hue bar.
 * @param onHueChanged the callback that is invoked when hue value changes. Hue is between 0 - 360.
 */
@Composable
internal fun HueBar(
    modifier: Modifier = Modifier,
    currentColor: HsvColor,
    onHueChanged: (Float) -> Unit
) {
    val rainbowBrush = remember { Brush.horizontalGradient(getRainbowColors()) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        onHueChanged(getHueFromPoint(down.position.x, size.width.toFloat()))
                        drag(down.id) { change ->
                            change.consumePositionChange()
                            onHueChanged(getHueFromPoint(change.position.x, size.width.toFloat()))
                        }
                    }
                }
            }
    ) {
        drawRect(rainbowBrush)
        drawOutline()
        drawSliderThumb(currentColor)
    }
}

private fun DrawScope.drawOutline() {
    drawRoundRect(
        color = Color.Gray,
        cornerRadius = CornerRadius(this.size.height / 2f),
        style = Stroke(0.5.dp.toPx())
    )
}

private fun DrawScope.drawSliderThumb(currentColor: HsvColor) {
    val radius = this.size.height / 2f
    val strokeWidth = 2.dp.toPx()
    val huePoint = getPointFromHue(
        color = currentColor,
        width = this.size.width - (radius * 2f)
    )

    drawCircle(
        color = Color.Gray,
        radius = radius - (strokeWidth * 1.25f),
        center = Offset(huePoint + radius, radius),
        style = Stroke(width = strokeWidth)
    )
    drawCircle(
        color = Color.White,
        radius = radius - strokeWidth,
        center = Offset(radius + huePoint, radius),
        style = Stroke(width = strokeWidth)
    )
}

private fun getRainbowColors(): List<Color> {
    return listOf(
        Color(0xFFFF0040),
        Color(0xFFFF00FF),
        Color(0xFF8000FF),
        Color(0xFF0000FF),
        Color(0xFF0080FF),
        Color(0xFF00FFFF),
        Color(0xFF00FF80),
        Color(0xFF00FF00),
        Color(0xFF80FF00),
        Color(0xFFFFFF00),
        Color(0xFFFF8000),
        Color(0xFFFF0000)
    )
}

private fun getPointFromHue(color: HsvColor, width: Float): Float {
    return width - (color.hue * width / 360f)
}

private fun getHueFromPoint(y: Float, width: Float): Float {
    val newY = y.coerceIn(0f, width)
    return 360f - newY * 360f / width
}
