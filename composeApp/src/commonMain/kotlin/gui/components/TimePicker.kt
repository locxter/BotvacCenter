package gui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import model.Time
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimePicker(
    time: Time,
    onSelect: (time: Time) -> Unit,
    modifier: Modifier = Modifier
) {
    var hourExpanded by remember { mutableStateOf(false) }
    var minuteExpanded by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedTime by remember { mutableStateOf(time) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon =
        if (hourExpanded || minuteExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    Column(modifier) {
        OutlinedTextField(
            value = selectedTime.getFormated(),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }.onFocusChanged {
                    if (it.isFocused) {
                        hourExpanded = true
                    }
                },
            trailingIcon = {
                Icon(icon, "contentDescription", Modifier.clickable {
                    if (hourExpanded || minuteExpanded) {
                        hourExpanded = false
                        minuteExpanded = false
                    } else {
                        hourExpanded = true
                    }
                })
            },
            readOnly = true
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
                    selectedTime = Time(selectedHour, minute)
                    onSelect(selectedTime)
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
    TimePicker(Time(11, 53), { _ -> })
}