package com.github.locxter.botvaccenter.gui

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
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.locxter.botvaccenter.gui.components.InfoDialog
import com.github.locxter.botvaccenter.gui.components.Label
import com.github.locxter.botvaccenter.gui.components.Navigation
import com.github.locxter.botvaccenter.gui.components.Switch
import com.github.locxter.botvaccenter.gui.components.TimePicker
import com.github.locxter.botvaccenter.gui.components.Title
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.model.Day
import com.github.locxter.botvaccenter.model.EDay
import com.github.locxter.botvaccenter.model.EStatus
import com.github.locxter.botvaccenter.model.Schedule
import com.github.locxter.botvaccenter.model.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


data class ScheduleScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val status by remember { mutableStateOf(botvacController.status) }
        var enableSchedule by remember { mutableStateOf(true) }
        var enableMonday by remember { mutableStateOf(true) }
        var mondayTimeInput by remember { mutableStateOf(Time(11, 30)) }
        var enableTuesday by remember { mutableStateOf(false) }
        var tuesdayTimeInput by remember { mutableStateOf(Time()) }
        var enableWednesday by remember { mutableStateOf(true) }
        var wednesdayTimeInput by remember { mutableStateOf(Time(11, 30)) }
        var enableThursday by remember { mutableStateOf(false) }
        var thursdayTimeInput by remember { mutableStateOf(Time()) }
        var enableFriday by remember { mutableStateOf(true) }
        var fridayTimeInput by remember { mutableStateOf(Time(11, 30)) }
        var enableSaturday by remember { mutableStateOf(false) }
        var saturdayTimeInput by remember { mutableStateOf(Time()) }
        var enableSunday by remember { mutableStateOf(false) }
        var sundayTimeInput by remember { mutableStateOf(Time()) }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        LifecycleEffect(onStarted = {
            if (status == EStatus.CONNECTED) {
                showLoadingPopup = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        botvacController.updateSchedule()
                        val schedule = botvacController.botvac.schedule
                        enableSchedule = schedule.isEnabled
                        enableMonday = schedule.monday != null
                        mondayTimeInput = schedule.monday ?: Time()
                        enableTuesday = schedule.tuesday != null
                        tuesdayTimeInput = schedule.tuesday ?: Time()
                        enableWednesday = schedule.wednesday != null
                        wednesdayTimeInput = schedule.wednesday ?: Time()
                        enableThursday = schedule.thursday != null
                        thursdayTimeInput = schedule.thursday ?: Time()
                        enableFriday = schedule.friday != null
                        fridayTimeInput = schedule.friday ?: Time()
                        enableSaturday = schedule.saturday != null
                        saturdayTimeInput = schedule.saturday ?: Time()
                        enableSunday = schedule.sunday != null
                        sundayTimeInput = schedule.sunday ?: Time()
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                    showLoadingPopup = false
                }
            }
        })
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
            TimePicker(
                mondayTimeInput,
                enabled = enableMonday,
                onSelect = { mondayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                tuesdayTimeInput,
                enabled = enableTuesday,
                onSelect = { tuesdayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                wednesdayTimeInput,
                enabled = enableWednesday,
                onSelect = { wednesdayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                thursdayTimeInput,
                enabled = enableThursday,
                onSelect = { thursdayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                fridayTimeInput,
                enabled = enableFriday,
                onSelect = { time -> fridayTimeInput = time },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                saturdayTimeInput,
                enabled = enableSaturday,
                onSelect = { saturdayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
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
            TimePicker(
                sundayTimeInput,
                enabled = enableSunday,
                onSelect = { sundayTimeInput = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), onClick = {
                    showLoadingPopup = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            botvacController.uploadSchedule(
                                Schedule(
                                    monday = if (enableMonday) mondayTimeInput else null,
                                    tuesday = if (enableTuesday) tuesdayTimeInput else null,
                                    wednesday = if (enableWednesday) wednesdayTimeInput else null,
                                    thursday = if (enableThursday) thursdayTimeInput else null,
                                    friday = if (enableFriday) fridayTimeInput else null,
                                    saturday = if (enableSaturday) saturdayTimeInput else null,
                                    sunday = if (enableSunday) sundayTimeInput else null,
                                    isEnabled = enableSchedule
                                )
                            )
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                        showLoadingPopup = false
                    }
                },
                enabled = status == EStatus.CONNECTED
            ) {
                Label("Upload schedule")
            }
            Button(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    showLoadingPopup = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val time = Clock.System.now()
                            val zone = TimeZone.currentSystemDefault()
                            botvacController.uploadDayAndTime(
                                Day(EDay.entries[time.toLocalDateTime(zone).dayOfWeek.ordinal]),
                                Time(
                                    time.toLocalDateTime(zone).hour,
                                    time.toLocalDateTime(zone).minute
                                )
                            )
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                        showLoadingPopup = false
                    }
                },
                enabled = status == EStatus.CONNECTED
            ) {
                Label("Upload day and time")
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
            InfoDialog(showErrorPopup, "Failed to communicate with robot") {
                showErrorPopup = false
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return ScheduleScreen(BotvacController())
        }
    }
}