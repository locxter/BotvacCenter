package gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Dropdown
import gui.components.Label
import gui.components.Navigation
import gui.components.Switch
import gui.components.TimePicker
import gui.components.TimePickerPreview
import gui.components.Title
import model.Schedule
import model.Time
import org.jetbrains.compose.ui.tooling.preview.Preview


data class ScheduleScreen(
    val isEnabled: Boolean,
    val schedule: Schedule
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var enabledInput by remember { mutableStateOf(isEnabled) }
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Schedule")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Enable schedule:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enabledInput,
                    onCheckedChange = {
                        enabledInput = it
                    }
                )
            }
            Label("Monday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Tuesday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Wednesday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Thursday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Friday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Saturday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Label("Sunday:", modifier = Modifier.padding(bottom = 5.dp))
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp), onClick = {}) {
                Label("Sync schedule")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Label("Sync time")
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return ScheduleScreen(true, Schedule(null, null, null, null, null, null, null))
        }
    }
}