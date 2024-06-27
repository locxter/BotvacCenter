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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gui.components.Dropdown
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import model.EStatus
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
        Navigation(onClick = {}, modifier = Modifier.padding(bottom = 10.dp), isBack = true) {
            Title("Settings")
        }
        Label("Alias:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = alias,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Address:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = address,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Username:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Label("Password:", modifier = Modifier.padding(bottom = 5.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { },
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