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
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DiagnosticsScreen() {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        var data by remember { mutableStateOf("No data") }
        Navigation(onClick = {}, modifier = Modifier.padding(bottom = 10.dp), isBack = true) {
            Title("Diagnostics")
        }
        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            modifier = Modifier
                .fillMaxWidth().padding(bottom = 5.dp),
            maxLines = 256,
            enabled = false
        )
        Spacer(Modifier.weight(1f))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
            Label("Get data")
        }
    }
}

@Preview
@Composable
fun DiagnosticsScreenPreview() {
    DiagnosticsScreen()
}