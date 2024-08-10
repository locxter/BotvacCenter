package com.github.locxter.botvaccenter.gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.locxter.botvaccenter.gui.components.ConfirmDialog
import com.github.locxter.botvaccenter.gui.components.InfoDialog
import com.github.locxter.botvaccenter.gui.components.Label
import com.github.locxter.botvaccenter.gui.components.MapVisualization
import com.github.locxter.botvaccenter.gui.components.Navigation
import com.github.locxter.botvaccenter.gui.components.Title
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.lib.Pathfinder
import com.github.locxter.botvaccenter.model.EStatus
import com.github.locxter.botvaccenter.model.Map
import com.github.locxter.botvaccenter.model.Path
import com.github.locxter.botvaccenter.model.Point
import com.github.locxter.botvaccenter.model.Scan
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min


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
        val pathfinder by remember { mutableStateOf(Pathfinder(50)) }
        var path by remember { mutableStateOf(Path()) }
        var speedInput by remember { mutableStateOf(175) }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        var showFollowPathPopup by remember { mutableStateOf(false) }
        var showNoPathPopup by remember { mutableStateOf(false) }
        LifecycleEffect(onStarted = {
            botvacController.botvac.scan = Scan()
            botvacController.botvac.map = Map()
            botvacController.botvac.location = Point()
            botvacController.botvac.angle = 0.0
            botvacController.botvac.oldScan = Scan()
            botvacController.botvac.oldLocation = Point()
            botvacController.botvac.oldAngle = 0.0
            botvacController.botvac.scanLocation = Point()
            botvacController.botvac.scanAngle = 0.0
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
                onClick = { target ->
                    if (mappingEnabled && botvac.map.points.isNotEmpty() && path.points.isEmpty()) {
                        showLoadingPopup = true
                        CoroutineScope(Dispatchers.IO).launch {
                            // Plan path
                            pathfinder.map = botvac.map
                            path = pathfinder.findPath(
                                botvac.location,
                                target
                            )
                            // Show a confirmation dialog
                            if (path.points.isNotEmpty()) {
                                showFollowPathPopup = true
                            } else {
                                showNoPathPopup = true
                            }
                            showLoadingPopup = false
                        }
                    }
                },
                modifier = Modifier.weight(1f).fillMaxSize().padding(bottom = 10.dp)
            )
            Label("Speed:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = speedInput.toString(),
                onValueChange = {
                    speedInput = try {
                        min(max(Integer.valueOf(it), 1), 350)
                    } catch (_: Exception) {
                        1
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
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
                        botvacController.botvac.scanLocation = Point()
                        botvacController.botvac.scanAngle = 0.0
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
            ConfirmDialog(
                visible = showFollowPathPopup,
                title = "Pathfinder",
                message = "Follow generated path autonomously?",
                onDismissRequest = { path.points.clear(); showFollowPathPopup = false },
                onConfirmation = {
                    showFollowPathPopup = false
                    // Follow path if possible
                    if (path.points.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val target = Point(path.points.last().x, path.points.last().y)
                                val threshold = max(pathfinder.simplificationFactor, 50)
                                while (path.points.isNotEmpty() &&
                                    (botvac.location.x < target.x - threshold || botvac.location.x > target.x + threshold ||
                                            botvac.location.y < target.y - threshold || botvac.location.y > target.y + threshold)
                                ) {
                                    botvacController.moveToPoint(
                                        path.points.first(),
                                        speedInput,
                                        500
                                    )
                                    botvac = botvacController.botvac.getDeepCopy()
                                    botvacController.updateLidar()
                                    botvac = botvacController.botvac.getDeepCopy()
                                    path.points.clear()
                                    pathfinder.map = botvac.map
                                    path = pathfinder.findPath(
                                        botvac.location,
                                        target
                                    )
                                    println("\nTarget: $target")
                                    println("Location: ${botvac.location}")
                                    println(
                                        "Not there: ${
                                            botvac.location.x <= target.x - pathfinder.simplificationFactor ||
                                                    botvac.location.x >= target.x + pathfinder.simplificationFactor ||
                                                    botvac.location.y <= target.y - pathfinder.simplificationFactor ||
                                                    botvac.location.y >= target.y + pathfinder.simplificationFactor
                                        }"
                                    )
                                    println("Path size: ${path.points.size}")
                                }
                                /*
                                for (point in path.points) {
                                    botvacController.moveToPoint(point, 175)
                                    botvac = botvacController.botvac.getDeepCopy()
                                    botvacController.updateLidar()
                                    botvac = botvacController.botvac.getDeepCopy()
                                }*/
                                path.points.clear()
                            } catch (exception: Exception) {
                                showErrorPopup = true
                            }
                        }
                    }
                }
            )
            ConfirmDialog(
                visible = showNoPathPopup,
                title = "Pathfinder",
                message = "No path found",
                onDismissRequest = { showNoPathPopup = false },
                onConfirmation = { showNoPathPopup = false }
            )
        }
    }

    companion object {
        fun Preview(): Screen {
            return MappingScreen(BotvacController())
        }
    }
}
