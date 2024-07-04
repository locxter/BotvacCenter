package gui

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Dropdown
import gui.components.InfoDialog
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lib.BotvacController
import model.EStatus
import model.Settings

data class HomeScreen(
    val settings: Settings,
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var status by rememberSaveable { mutableStateOf(botvacController.status) }
        var charge by rememberSaveable { mutableStateOf(botvacController.botvac.charge) }
        var cleaningTime by rememberSaveable { mutableStateOf(botvacController.botvac.cleaningTime) }
        var showLoadingPopup by rememberSaveable { mutableStateOf(false) }
        var cleanSpot by rememberSaveable { mutableStateOf(false) }
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Title(text = settings.robotName, modifier = Modifier.padding(bottom = 10.dp))
            Label(
                text = "Status: ${status.displayName}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Label(
                text = "Charge: ${charge}%",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Label(
                text = "Cleaning time: ${if (cleaningTime / 3600 < 10) "0${cleaningTime / 3600}" else cleaningTime / 3600}" +
                        ":${if ((cleaningTime % 3600) / 60 < 10) "0${(cleaningTime % 3600) / 60}" else (cleaningTime % 3600) / 60}" +
                        ":${if (cleaningTime % 60 < 10) "0${cleaningTime % 60}" else cleaningTime % 60}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Spacer(Modifier.weight(1f))
            Navigation(
                onClick = { navigator.push(RemoteScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Remote")
            }
            Navigation(
                onClick = { navigator.push(ScheduleScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Schedule")
            }
            Navigation(
                onClick = { navigator.push(DiagnosticsScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Diagnostics")
            }
            Navigation(
                onClick = { navigator.push(MappingScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Mapping")
            }
            Navigation(
                onClick = { navigator.push(SettingsScreen(settings)) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Settings")
            }
            Spacer(Modifier.weight(1f))
            Label("Mode:", modifier = Modifier.padding(bottom = 5.dp))
            Dropdown(
                listOf("House", "Spot"),
                selected = if (cleanSpot) 1 else 0,
                onSelect = { index, _ -> cleanSpot = index == 1 },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (botvacController.status == EStatus.CONNECTED) {
                            if (cleanSpot) {
                                botvacController.cleanSpot()
                            } else {
                                botvacController.cleanHouse()
                            }
                        } else {
                            botvacController.stopCleaning()
                            botvacController.updateCharge()
                            charge = botvacController.botvac.charge
                            botvacController.updateCleaningTime()
                            cleaningTime = botvacController.botvac.cleaningTime
                        }
                        status = botvacController.status
                    }
                },
                enabled = status != EStatus.DISCONNECTED
            ) {
                if (status == EStatus.CLEANING_HOUSE || status == EStatus.CLEANING_SPOT) {
                    Label("Stop cleaning")
                } else {
                    Label("Start cleaning")
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    showLoadingPopup = true
                    CoroutineScope(Dispatchers.IO).launch {
                        if (botvacController.status == EStatus.DISCONNECTED) {
                            botvacController.connect(
                                settings.address,
                                settings.username,
                                settings.password
                            )
                            botvacController.updateCharge()
                            botvacController.updateCleaningTime()
                        } else {
                            botvacController.disconnect()
                        }
                        status = botvacController.status
                        charge = botvacController.botvac.charge
                        cleaningTime = botvacController.botvac.cleaningTime
                        showLoadingPopup = false
                    }
                }) {
                if (status == EStatus.DISCONNECTED) {
                    Label("Connect")
                } else {
                    Label("Disconnect")
                }
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
        }
    }

    companion object {
        fun Preview(): Screen {
            return HomeScreen(Settings(), BotvacController())
        }
    }
}
