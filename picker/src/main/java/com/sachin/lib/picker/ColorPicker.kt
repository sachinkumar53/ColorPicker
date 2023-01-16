package com.sachin.lib.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sachin.lib.picker.data.model.HsvColor
import com.sachin.lib.picker.ui.ColorPickerUi

/**
 * Color Picker Component that to pick a color.
 * @param modifier modifiers to set to this color picker.
 * @param color the initial color to set on the picker.
 * @param showAlphaBar whether or not to show the bottom alpha bar on the color picker.
 * @param onColorChanged callback that is triggered when the color changes
 *
 */
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    showAlphaBar: Boolean = true,
    onColorChanged: (Color) -> Unit
) {

    ColorPickerUi(
        modifier = modifier,
        color = HsvColor.from(color),
        showAlphaBar = showAlphaBar,
        onColorChanged = { onColorChanged(it.toColor()) }
    )

}