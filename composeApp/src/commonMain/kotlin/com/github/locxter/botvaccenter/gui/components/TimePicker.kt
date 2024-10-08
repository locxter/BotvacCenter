package com.github.locxter.botvaccenter.gui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.github.locxter.botvaccenter.model.Time
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimePicker(
    time: Time,
    enabled: Boolean = true,
    onSelect: (time: Time) -> Unit,
    modifier: Modifier = Modifier
) {
    var hourExpanded by remember { mutableStateOf(false) }
    var minuteExpanded by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(0) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon =
        if (hourExpanded || minuteExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    Column(modifier) {
        OutlinedTextField(
            value = time.getFormated(),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(icon,
                    "Arrow ${if (hourExpanded || minuteExpanded) "up" else "down"}",
                    Modifier.clickable {
                        if (enabled) {
                            if (hourExpanded || minuteExpanded) {
                                hourExpanded = false
                                minuteExpanded = false
                            } else {
                                hourExpanded = true
                            }
                        }
                    })
            },
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                if (hourExpanded || minuteExpanded) {
                                    hourExpanded = false
                                    minuteExpanded = false
                                } else {
                                    hourExpanded = true
                                }

                            }
                        }
                    }
                },
            readOnly = true,
            enabled = enabled
        )
        DropdownMenu(
            expanded = hourExpanded,
            onDismissRequest = { hourExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            for (hour in 0..23) {
                DropdownMenuItem(onClick = {
                    selectedHour = hour
                    hourExpanded = false
                    minuteExpanded = true
                }) {
                    Text(text = "${if (hour < 10) "0$hour" else hour}:00")
                }
            }
        }
        DropdownMenu(
            expanded = minuteExpanded,
            onDismissRequest = { minuteExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            for (minute in 0..59) {
                DropdownMenuItem(onClick = {
                    onSelect(Time(selectedHour, minute))
                    minuteExpanded = false
                }) {
                    Text(text = "${if (selectedHour < 10) "0$selectedHour" else selectedHour}:${if (minute < 10) "0$minute" else minute}")
                }
            }
        }
    }
}

@Preview
@Composable
fun TimePickerPreview() {
    TimePicker(Time(11, 53), onSelect = { _ -> })
}