package com.sachin.lib.picker.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun DialogButtonBar(
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    positiveButtonText :String = "Done",
    negativeButtonText :String = "Cancel",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomButton(
            text = negativeButtonText,
            onClick = onNegativeButtonClick,
            modifier = Modifier.weight(1f)
        )

        CustomButton(
            text = positiveButtonText,
            onClick = onPositiveButtonClick,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}
