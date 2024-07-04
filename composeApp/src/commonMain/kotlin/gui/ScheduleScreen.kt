package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Label
import gui.components.Navigation
import gui.components.Switch
import gui.components.TimePicker
import gui.components.Title
import lib.BotvacController
import model.Schedule
import model.Time


data class ScheduleScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var enableSchedule by remember { mutableStateOf(true) }
        var enableMonday by remember { mutableStateOf(true) }
        var enableTuesday by remember { mutableStateOf(false) }
        var enableWednesday by remember { mutableStateOf(true) }
        var enableThursday by remember { mutableStateOf(false) }
        var enableFriday by remember { mutableStateOf(true) }
        var enableSaturday by remember { mutableStateOf(false) }
        var enableSunday by remember { mutableStateOf(false) }
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
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Label("Enable schedule:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableSchedule,
                    onCheckedChange = {
                        enableSchedule = it
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Monday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableMonday,
                    onCheckedChange = {
                        enableMonday = it
                    }
                )
            }
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Tuesday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableTuesday,
                    onCheckedChange = {
                        enableTuesday = it
                    }
                )
            }
            TimePicker(Time(0, 0), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Wednesday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableWednesday,
                    onCheckedChange = {
                        enableWednesday = it
                    }
                )
            }
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Thursday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableThursday,
                    onCheckedChange = {
                        enableThursday = it
                    }
                )
            }
            TimePicker(Time(0, 0), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Friday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableFriday,
                    onCheckedChange = {
                        enableFriday = it
                    }
                )
            }
            TimePicker(Time(11, 30), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Saturday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableSaturday,
                    onCheckedChange = {
                        enableSaturday = it
                    }
                )
            }
            TimePicker(Time(0, 0), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Sunday:")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = enableSunday,
                    onCheckedChange = {
                        enableSunday = it
                    }
                )
            }
            TimePicker(Time(0, 0), { _ -> }, modifier = Modifier.padding(bottom = 10.dp))
            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), onClick = {}) {
                Label("Upload schedule")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Label("Upload day and time")
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return ScheduleScreen(BotvacController())
        }
    }
}