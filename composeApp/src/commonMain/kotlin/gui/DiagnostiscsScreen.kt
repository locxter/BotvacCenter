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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Label
import gui.components.Navigation
import gui.components.Title

class DiagnosticsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var data by remember { mutableStateOf("No data") }
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
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Label("Get data")
            }
        }
    }

    companion object {
        fun Preview(): Screen {
            return DiagnosticsScreen()
        }
    }
}