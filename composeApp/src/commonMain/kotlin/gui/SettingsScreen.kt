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
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(
    alias: String,
    address: String,
    username: String,
    password: String
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        var aliasInput by remember { mutableStateOf(alias) }
        var addressInput by remember { mutableStateOf(address) }
        var usernameInput by remember { mutableStateOf(address) }
        var passwordInput by remember { mutableStateOf(address) }
        Navigation(onClick = {}, modifier = Modifier.padding(bottom = 10.dp), isBack = true) {
            Title("Settings")
        }
        Label("Alias:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = aliasInput,
            onValueChange = { aliasInput = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Address:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Username:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Password:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.weight(1f))
        Label("2024 locxter", modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen("Robot", "http://btvcbrdg.local", "btvcbrdg", "btvcbrdg")
}