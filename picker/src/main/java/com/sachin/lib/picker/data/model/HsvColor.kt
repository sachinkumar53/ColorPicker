package com.sachin.lib.picker.data.model

/**
 * A representation of Color in Hue, Saturation and Value form.
 */
import androidx.annotation.FloatRange
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.model.HSV
import com.github.ajalt.colormath.model.RGB

/**
 * A representation of Color in Hue, Saturation and Value form.
 */
internal data class HsvColor(
    @FloatRange(from = 0.0, to = 360.0)
    val hue: Float,

    @FloatRange(from = 0.0, to = 1.0)
    val saturation: Float,

    @FloatRange(from = 0.0, to = 1.0)
    val value: Float,

    @FloatRange(from = 0.0, to = 1.0)
    val alpha: Float
) {

    internal fun toColor(): Color {
        val hsv = HSV(hue, saturation, value, alpha)
        val rgb = hsv.toSRGB()
        return Color(rgb.redInt, rgb.greenInt, rgb.blueInt, rgb.alphaInt)
    }


    companion object {

        private val DEFAULT = HsvColor(360f, 1.0f, 1.0f, 1.0f)

        /**
         *  the color math hsv to local hsv color
         */
        private fun HSV.toColor(): HsvColor {
            return HsvColor(
                hue = if (this.h.isNaN()) 0f else this.h,
                saturation = this.s,
                value = this.v,
                alpha = this.alpha
            )
        }

        internal fun from(color: Color): HsvColor {
            return RGB(
                color.red,
                color.green,
                color.blue,
                color.alpha
            ).toHSV().toColor()
        }

        internal val Saver: Saver<HsvColor, *> = listSaver(
            save = {
                listOf(
                    it.hue,
                    it.saturation,
                    it.value,
                    it.alpha
                )
            },
            restore = {
                HsvColor(
                    it[0],
                    it[1],
                    it[2],
                    it[3]
                )
            }
        )
    }
}
