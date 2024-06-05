package com.sachin.lib.picker.dialog

import android.graphics.Color.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sachin.lib.picker.*
import com.sachin.lib.picker.data.persistence.PersistenceManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    showAlphaBar:Boolean = false
) {
    if (visible) {
        val context = LocalContext.current
        val savedColors by PersistenceManager.getColorList(context)
            .collectAsState(initial = emptyList())
        val coroutineScope = rememberCoroutineScope()

        val keyboardController = LocalSoftwareKeyboardController.current
        var selectedColor by remember { mutableStateOf(color) }
        var colorHex by remember { mutableStateOf(selectedColor.toHex()) }
        var redValue by remember {
            mutableStateOf(TextFieldValue(selectedColor.redInt.toString()))
        }
        var greenValue by remember {
            mutableStateOf(TextFieldValue(selectedColor.greenInt.toString()))
        }
        var blueValue by remember {
            mutableStateOf(TextFieldValue(selectedColor.blueInt.toString()))
        }

        var refreshPickerState by remember { mutableStateOf(false) }

        fun refreshPicker() {
            refreshPickerState = !refreshPickerState
        }
        LaunchedEffect(selectedColor) {
            colorHex = selectedColor.toHex()
            redValue = redValue.copy(selectedColor.redInt.toString())
            greenValue = greenValue.copy(selectedColor.greenInt.toString())
            blueValue = blueValue.copy(selectedColor.blueInt.toString())
        }

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp)
            ) {

                key(refreshPickerState) {
                    ColorPicker(
                        onColorChanged = { color ->
                            selectedColor = color
                            keyboardController?.hide()
                        },
                        color = selectedColor,
                        showAlphaBar = false,
                        modifier = Modifier.height(256.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ColorPreviewBox(
                        oldColor = color,
                        newColor = selectedColor,
                        modifier = Modifier.align(Alignment.Bottom)
                    )

                    HexTextField(
                        text = colorHex,
                        onTextChange = {
                            colorHex = it
                            if (it.length == 6) {
                                selectedColor = Color(parseColor("#FF$it"))
                                refreshPicker()
                            }
                        },
                        modifier = Modifier.weight(0.4f)
                    )

                    RgbTextField(
                        label = "Red",
                        text = redValue,
                        onTextChange = {
                            redValue = it
                            selectedColor = selectedColor.copy(red = it.text.toInt() / 255f)
                            refreshPicker()
                        },
                        modifier = Modifier.weight(0.2f)
                    )

                    RgbTextField(
                        label = "Green",
                        text = greenValue,
                        onTextChange = {
                            greenValue = it
                            selectedColor =
                                selectedColor.copy(green = it.text.toInt() / 255f)
                            refreshPicker()
                        },
                        modifier = Modifier.weight(0.2f)
                    )
                    RgbTextField(
                        label = "Blue",
                        text = blueValue,
                        onTextChange = {
                            blueValue = it
                            selectedColor = selectedColor.copy(blue = it.text.toInt() / 255f)
                            refreshPicker()
                        },
                        modifier = Modifier.weight(0.2f)
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                RecentColors(
                    colors = savedColors,
                    onColorClick = {
                        selectedColor = it
                        refreshPicker()
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                DialogButtonBar(
                    onPositiveButtonClick = {
                        onColorSelected(selectedColor)
                        coroutineScope.launch {
                            if (!savedColors.contains(selectedColor))
                                PersistenceManager.addColor(context, selectedColor)
                        }
                    },
                    onNegativeButtonClick = onDismissRequest
                )

            }
        }
    }
}

@Composable
fun RecentColors(
    colors: List<Color>,
    onColorClick: (Color) -> Unit
) {
    Column {
        Text(
            text = "Recent colors",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        CustomRow(
            cellSize = 40.dp,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(
                        if (index < colors.size) {
                            colors[index]
                        } else Color.Black.copy(alpha = 0.1f)
                    )
                    .border(
                        width = 0.5.dp,
                        color = Color.Gray,
                        shape = CircleShape
                    ).run {
                        if (index < colors.size) {
                            clickable {
                                onColorClick(colors[index])
                            }
                        } else this
                    }
            )
        }
    }
}

@Composable
private fun ColorPreviewBox(
    oldColor: Color,
    newColor: Color,
    modifier: Modifier,
) {
    val boxShape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .clip(boxShape)
            .border(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                width = 1.dp,
                shape = boxShape
            )
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(40.dp)
                .background(oldColor)
        )
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(40.dp)
                .background(newColor)
        )
    }
}

@Composable
private fun HexTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hexChars = remember { 'A'..'F' union '0'..'9' }
    BasicTextField(
        value = text,
        onValueChange = { str ->
            if (str.all { it in hexChars } && str.length <= 6) {
                onTextChange(str)
            }
        },
        decorationBox = { innerTextField ->
            CustomDecoration("Hex", innerTextField)
        },
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            color = LocalContentColor.current
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
        modifier = modifier,
        cursorBrush = SolidColor(MaterialTheme.colors.primary)
    )
}

@Composable
private fun RgbTextField(
    label: String,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    fun setOneTextFieldValue(value: String) {
        onTextChange(TextFieldValue(value, selection = TextRange(1)))
    }

    BasicTextField(
        value = text,
        onValueChange = { tfv ->
            val str = tfv.text
            if (str.isEmpty()) {
                setOneTextFieldValue("0")
            } else if (str.all { it.isDigit() } && str.length <= 3) {
                val value = str.toInt()
                if (value <= 255) {
                    if (str.startsWith("0")) {
                        setOneTextFieldValue(value.toString())
                    } else {
                        onTextChange(tfv)
                    }

                }
            }
        },
        decorationBox = { innerTextField ->
            CustomDecoration(label, innerTextField)
        },
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            color = LocalContentColor.current
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.widthIn(48.dp),
        cursorBrush = SolidColor(MaterialTheme.colors.primary)
    )
}


@Composable
private fun CustomDecoration(label: String, innerTextField: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            innerTextField()
        }
    }
}
