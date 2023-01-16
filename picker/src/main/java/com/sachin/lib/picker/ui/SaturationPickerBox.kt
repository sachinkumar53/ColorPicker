package com.sachin.lib.picker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.github.ajalt.colormath.model.HSV
import com.sachin.lib.picker.data.model.HsvColor

/**
 * Saturation Value area Component that invokes onSaturationValueChanged when the saturation or value is mutated.
 *
 * @param modifier modifiers to set on the Alpha Bar
 * @param currentColor the initial color to set on the alpha bar.
 * @param onSaturationValueChanged the callback that is invoked when saturation or value component of the changes.
 * saturation, value both between 0 - 1.
 */
@Composable
internal fun SaturationPickerBox(
    modifier: Modifier = Modifier,
    currentColor: HsvColor,
    onSaturationValueChanged: (saturation: Float, value: Float) -> Unit
) {
    val blackGradientBrush = remember {
        Brush.verticalGradient(listOf(Color(0xffffffff), Color(0xff000000)))
    }

    val currentColorGradientBrush = remember(currentColor.hue) {
        val hsv = HSV(currentColor.hue, 1.0f, 1.0f)
        val rgb = hsv.toSRGB()
        Brush.horizontalGradient(
            listOf(
                Color(0xffffffff),
                Color(rgb.redInt, rgb.greenInt, rgb.blueInt, rgb.alphaInt)
            )
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        val down = awaitFirstDown()
                        val (s, v) = getSaturationPoint(down.position, size)
                        onSaturationValueChanged(s, v)
                        drag(down.id) { change ->
                            change.consumePositionChange()
                            val (newSaturation, newValue) = getSaturationPoint(
                                change.position,
                                size
                            )
                            onSaturationValueChanged(newSaturation, newValue)
                        }
                    }
                }
            }
    ) {
        val cornerRadius = CornerRadius(6.dp.toPx())
        drawRoundRect(
            brush = blackGradientBrush,
            cornerRadius = cornerRadius
        )
        drawRoundRect(
            brush = currentColorGradientBrush,
            blendMode = BlendMode.Modulate,
            cornerRadius = cornerRadius
        )
        drawRoundRect(
            color = Color.Gray,
            style = Stroke(0.5.dp.toPx()),
            cornerRadius = cornerRadius
        )

        drawCircleSelector(currentColor)
    }
}

private fun DrawScope.drawCircleSelector(currentColor: HsvColor) {
    val radius = 8.dp.toPx()
    val strokeWidth = 2.dp.toPx()
    val point = getSaturationValuePoint(currentColor, size)

    drawCircle(
        color = Color.White,
        radius = radius,
        center = point
    )

    drawCircle(
        color = Color.Gray,
        radius = radius - 1.dp.toPx(),
        center = point
    )

    drawCircle(
        color = currentColor.copy(alpha = 1.0f).toColor(),
        radius = radius - strokeWidth,
        center = point
    )
}

private fun getSaturationPoint(
    offset: Offset,
    size: IntSize
): Pair<Float, Float> {
    val (saturation, value) = getSaturationValueFromPosition(
        offset,
        size.toSize()
    )
    return saturation to value
}

/**
 * Gets the X/Y offset for a color based on the input Size
 * (This is for the large inner area)
 *
 * @return an Offset within the Size that represents the saturation and value of the supplied Color.
 */
private fun getSaturationValuePoint(color: HsvColor, size: Size): Offset {
    val height: Float = size.height
    val width: Float = size.width

    return Offset((color.saturation * width), (1f - color.value) * height)
}

/**
 * Given an offset and size, this function calculates a saturation and value amount based on that.
 *
 * @return new saturation and value
 */
private fun getSaturationValueFromPosition(offset: Offset, size: Size): Pair<Float, Float> {
    val width = size.width
    val height = size.height

    val newX = offset.x.coerceIn(0f, width)

    val newY = offset.y.coerceIn(0f, size.height)
    val saturation = 1f / width * newX
    val value = 1f - 1f / height * newY

    return saturation.coerceIn(0f, 1f) to value.coerceIn(0f, 1f)
}
