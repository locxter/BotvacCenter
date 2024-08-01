package com.github.locxter.botvaccenter.gui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.locxter.botvaccenter.gui.components.DirectionButton
import com.github.locxter.botvaccenter.gui.components.InfoDialog
import com.github.locxter.botvaccenter.gui.components.Label
import com.github.locxter.botvaccenter.gui.components.Navigation
import com.github.locxter.botvaccenter.gui.components.Title
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.model.EDirection
import com.github.locxter.botvaccenter.model.EStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

data class RemoteScreen(
    val botvacController: BotvacController
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var status by remember { mutableStateOf(botvacController.status) }
        var speedInput by remember { mutableStateOf(175) }
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
                Title("Remote")
            }
            Spacer(Modifier.weight(1f))
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(
                    EDirection.DIRECTION_UP,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Press) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlDriveForward(
                                                    speedInput
                                                )
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    } else if (it is PressInteraction.Release) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlStopDriving()
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(
                    EDirection.DIRECTION_LEFT,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Press) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlTurnLeft(speedInput)
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    } else if (it is PressInteraction.Release) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlStopDriving()
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
                Spacer(Modifier.size(115.dp, 75.dp))
                DirectionButton(
                    EDirection.DIRECTION_RIGHT,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Press) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlTurnRight(speedInput)
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    } else if (it is PressInteraction.Release) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlStopDriving()
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(
                    EDirection.DIRECTION_DOWN,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Press) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlDriveBackward(
                                                    speedInput
                                                )
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    } else if (it is PressInteraction.Release) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                botvacController.remoteControlStopDriving()
                                            } catch (exception: Exception) {
                                                showErrorPopup = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.weight(1f))
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
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    showLoadingPopup = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            if (status == EStatus.CONNECTED) {
                                botvacController.enableRemoteControl()
                            } else {
                                botvacController.disableRemoteControl()
                            }
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                        status = botvacController.status
                        showLoadingPopup = false
                    }
                },
                enabled = status == EStatus.CONNECTED || status == EStatus.REMOTE_CONTROLLED
            ) {
                if (status == EStatus.REMOTE_CONTROLLED) {
                    Label("Disable remote")
                } else {
                    Label("Enable remote")
                }
            }
            InfoDialog(showLoadingPopup, "Loading...") { showLoadingPopup = false }
            InfoDialog(showErrorPopup, "Failed to communicate with robot") {
                showErrorPopup = false
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return RemoteScreen(BotvacController())
        }
    }
}