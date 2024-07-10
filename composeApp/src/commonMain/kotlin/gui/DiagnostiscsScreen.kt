package gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.InfoDialog
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lib.BotvacController
import model.EStatus

data class DiagnosticsScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val status by remember { mutableStateOf(botvacController.status) }
        var data by remember { mutableStateOf("No data") }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Diagnostics")
            }
            OutlinedTextField(
                value = data,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 10.dp),
                maxLines = 256,
                readOnly = true
            )
            Spacer(Modifier.weight(1f))
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                showLoadingPopup = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        data = "GetAccel\n" +
                                botvacController.runRawCommand("GetAccel") +
                                "\n\nGetAnalogSensors\n" +
                                botvacController.runRawCommand("GetAnalogSensors") +
                                "\n\nGetButtons\n" +
                                botvacController.runRawCommand("GetButtons") +
                                "\n\nGetCalInfo\n" +
                                botvacController.runRawCommand("GetCalInfo") +
                                "\n\nGetCharger\n" +
                                botvacController.runRawCommand("GetCharger") +
                                "\n\nGetDigitalSensors\n" +
                                botvacController.runRawCommand("GetDigitalSensors") +
                                "\n\nGetErr\n" +
                                botvacController.runRawCommand("GetErr") +
                                "\n\nGetLanguage\n" +
                                botvacController.runRawCommand("GetLanguage") +
                                "\n\nGetLDSScan\n" +
                                botvacController.runRawCommand("GetLDSScan") +
                                "\n\nGetMotors\n" +
                                botvacController.runRawCommand("GetMotors") +
                                "\n\nGetSchedule\n" +
                                botvacController.runRawCommand("GetSchedule") +
                                "\n\nGetTime\n" +
                                botvacController.runRawCommand("GetTime") +
                                "\n\nGetVersion\n" +
                                botvacController.runRawCommand("GetVersion") +
                                "\n\nGetWarranty\n" +
                                botvacController.runRawCommand("GetWarranty")
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                    showLoadingPopup = false
                }
            }, enabled = status == EStatus.CONNECTED) {
                Label("Get data")
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
            InfoDialog(showErrorPopup, "Failed to communicate with robot") {
                showErrorPopup = false
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return DiagnosticsScreen(BotvacController())
        }
    }
}