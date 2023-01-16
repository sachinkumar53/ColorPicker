package com.sachin.lib.picker.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
internal fun CustomRow(
    cellSize: Dp,
    modifier: Modifier = Modifier,
    item: @Composable RowScope.(Int) -> Unit
) {
    val localDensity = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier
    ) {
        val columnCount = with(localDensity) { maxWidth.toPx() / cellSize.toPx() }.toInt() - 1
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            repeat(columnCount) {
                item(it)
            }
        }
    }
}