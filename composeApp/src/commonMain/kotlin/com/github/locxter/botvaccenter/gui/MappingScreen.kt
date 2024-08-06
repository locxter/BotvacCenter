package com.github.locxter.botvaccenter.gui

import androidx.compose.foundation.layout.Column
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
import com.github.locxter.botvaccenter.gui.components.MapVisualization
import com.github.locxter.botvaccenter.gui.components.Navigation
import com.github.locxter.botvaccenter.gui.components.Title
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.lib.SettingsController
import com.github.locxter.botvaccenter.model.Botvac
import com.github.locxter.botvaccenter.model.EStatus
import com.github.locxter.botvaccenter.model.Map
import com.github.locxter.botvaccenter.model.Point
import com.github.locxter.botvaccenter.model.Scan
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class MappingScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val fileSaver = rememberFileSaverLauncher { }
        val status by remember { mutableStateOf(botvacController.status) }
        var mappingEnabled by remember { mutableStateOf(false) }
        var botvac by remember { mutableStateOf(botvacController.botvac.getDeepCopy()) }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        LifecycleEffect(onStarted = {
            botvacController.botvac.scan = Scan()
            botvacController.botvac.map = Map()
            botvacController.botvac.location = Point()
            botvacController.botvac.angle = 0.0
            botvacController.botvac.oldScan = Scan()
            botvacController.botvac.oldLocation = Point()
            botvacController.botvac.oldAngle = 0.0
            botvac = botvacController.botvac.getDeepCopy()
        })
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Mapping")
            }
            MapVisualization(
                botvac = botvac,
                onClick = {
                    if (mappingEnabled) {
                        println("Target: $it")
                    }
                },
                modifier = Modifier.weight(1f).fillMaxSize().padding(bottom = 10.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                onClick = {
                    mappingEnabled = !mappingEnabled
                    if (mappingEnabled) {
                        showLoadingPopup = true
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                botvacController.updateLidar()
                                botvac = botvacController.botvac.getDeepCopy()
                            } catch (exception: Exception) {
                                showErrorPopup = true
                            }
                            showLoadingPopup = false
                        }
                    } else {
                        botvacController.botvac.scan = Scan()
                        botvacController.botvac.map = Map()
                        botvacController.botvac.location = Point()
                        botvacController.botvac.angle = 0.0
                        botvacController.botvac.oldScan = Scan()
                        botvacController.botvac.oldLocation = Point()
                        botvacController.botvac.oldAngle = 0.0
                        botvac = botvacController.botvac.getDeepCopy()
                    }
                },
                enabled = status == EStatus.CONNECTED
            ) {
                if (!mappingEnabled) {
                    Label("Enable mapping")
                } else {
                    Label("Disable mapping")
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        var data = "X:, Y:\n"
                        for (point in botvac.map.points) {
                            data += "${point.x}, ${point.y}\n"
                        }
                        fileSaver.launch(
                            bytes = data.encodeToByteArray(),
                            baseName = "map",
                            extension = "csv",
                        )
                    }
                },
                enabled = mappingEnabled && botvac.map.points.isNotEmpty()
            ) {
                Label("Save map")
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
            InfoDialog(showErrorPopup, "Failed to communicate with robot") {
                showErrorPopup = false
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return MappingScreen(BotvacController())
        }
    }
}
