package gui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.DisposableEffect
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
import gui.components.DirectionButton
import gui.components.InfoDialog
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lib.BotvacController
import model.EDirection
import model.EStatus
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
        val forwardInteractionSource = remember { MutableInteractionSource() }
        val isForwardPressed by forwardInteractionSource.collectIsPressedAsState()
        val leftInteractionSource = remember { MutableInteractionSource() }
        val isLeftPressed by leftInteractionSource.collectIsPressedAsState()
        val rightInteractionSource = remember { MutableInteractionSource() }
        val isRightPressed by rightInteractionSource.collectIsPressedAsState()
        val backwardInteractionSource = remember { MutableInteractionSource() }
        val isBackwardPressed by backwardInteractionSource.collectIsPressedAsState()
        var speedInput by remember { mutableStateOf(175) }
        var showLoadingPopup by remember { mutableStateOf(false) }
        var showErrorPopup by remember { mutableStateOf(false) }
        if (isForwardPressed) {
            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        botvacController.driveForwardRemoteControl(speedInput)
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            botvacController.stopDrivingRemoteControl()
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                    }
                }
            }
        }
        if (isLeftPressed) {
            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        botvacController.turnLeftRemoteControl(speedInput)
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            botvacController.stopDrivingRemoteControl()
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                    }
                }
            }
        }
        if (isRightPressed) {
            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        botvacController.turnRightRemoteControl(speedInput)
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            botvacController.stopDrivingRemoteControl()
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                    }
                }
            }
        }
        if (isBackwardPressed) {
            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        botvacController.driveBackwardRemoteControl(speedInput)
                    } catch (exception: Exception) {
                        showErrorPopup = true
                    }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            botvacController.stopDrivingRemoteControl()
                        } catch (exception: Exception) {
                            showErrorPopup = true
                        }
                    }
                }
            }
        }
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
                    interactionSource = forwardInteractionSource
                )
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(
                    EDirection.DIRECTION_LEFT,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = leftInteractionSource
                )
                Spacer(Modifier.size(115.dp, 75.dp))
                DirectionButton(
                    EDirection.DIRECTION_RIGHT,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = rightInteractionSource
                )
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(
                    EDirection.DIRECTION_DOWN,
                    {},
                    enabled = status == EStatus.REMOTE_CONTROLLED,
                    interactionSource = backwardInteractionSource
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
                        0
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