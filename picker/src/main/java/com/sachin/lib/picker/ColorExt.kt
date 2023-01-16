package com.sachin.lib.picker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red


val Color.redInt: Int
    get() = this.toArgb().red

val Color.greenInt: Int
    get() = this.toArgb().green

val Color.blueInt: Int
    get() = this.toArgb().blue


fun Color.toHex(withAlpha: Boolean = false): String {
    return if (withAlpha) String.format("#%08X", -0x1 and toArgb())
    else String.format("%06X", 0xFFFFFF and this.toArgb())
}
