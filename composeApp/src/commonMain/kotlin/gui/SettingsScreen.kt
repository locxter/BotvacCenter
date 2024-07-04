package gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
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
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import lib.SettingsController
import model.Settings

data class SettingsScreen(val settings: Settings) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var robotNameInput by remember { mutableStateOf(settings.robotName) }
        var addressInput by remember { mutableStateOf(settings.address) }
        var usernameInput by remember { mutableStateOf(settings.username) }
        var passwordInput by remember { mutableStateOf(settings.password) }
        LifecycleEffect(onDisposed = {
            SettingsController().writeSettings(settings)
        })
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Settings")
            }
            Label("Robot name:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = robotNameInput,
                onValueChange = { robotNameInput = it; settings.robotName = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Label("Address:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = addressInput,
                onValueChange = { addressInput = it; settings.address = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Label("Username:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { usernameInput = it; settings.username = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Label("Password:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it; settings.password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Spacer(Modifier.weight(1f))
            Label("2024 locxter", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }

    companion object {
        fun Preview(): Screen {
            return SettingsScreen(Settings())
        }
    }
}