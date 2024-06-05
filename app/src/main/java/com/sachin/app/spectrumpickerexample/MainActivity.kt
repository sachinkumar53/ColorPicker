package com.sachin.app.spectrumpickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sachin.app.spectrumpickerexample.ui.theme.SpectrumPickerTheme
import com.sachin.lib.picker.dialog.ColorPickerDialog

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SpectrumPickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    var showDialog by remember { mutableStateOf(false) }
                    var color by remember { mutableStateOf(Color.Yellow) }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { showDialog = true }) {
                            Text(text = "Show dialog")
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(color)
                        )
                    }
                    ColorPickerDialog(
                        onDismissRequest = {
                            showDialog = false

                        },
                        color = color,
                        visible = showDialog,
                        onColorSelected = {
                            color = it
                            showDialog = false
                        },
                        properties = DialogProperties(usePlatformDefaultWidth = false),
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints);
                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    placeable.place(
                                        0,
                                        constraints.maxHeight - placeable.height,
                                        10f
                                    )
                                }
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}
