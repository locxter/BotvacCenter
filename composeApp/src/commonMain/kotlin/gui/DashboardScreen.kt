package gui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.ConfirmDialog
import gui.components.Dropdown
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import model.EStatus

data class DashboardScreen(
    val alias: String,
    val status: EStatus,
    val charge: Int,
    val cleanTime: Long
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Title(text = alias, modifier = Modifier.padding(bottom = 10.dp))
            Label(
                text = "Status: ${status.displayName}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Label(text = "Status: $charge%", modifier = Modifier.padding(bottom = 5.dp))
            Label(
                text = "Clean time: ${cleanTime / 3600}:${(cleanTime % 3600) / 60}:${cleanTime % 60}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Spacer(Modifier.weight(1f))
            Navigation(
                onClick = { navigator.push(RemoteScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Remote")
            }
            Navigation(
                onClick = { navigator.push(ScheduleScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Schedule")
            }
            Navigation(
                onClick = { navigator.push(DiagnosticsScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Diagnostics")
            }
            Navigation(
                onClick = { navigator.push(MappingScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Mapping")
            }
            Navigation(
                onClick = { navigator.push(SettingsScreen.Preview()) },
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Label("Settings")
            }
            Spacer(Modifier.weight(1f))
            Label("Mode:", modifier = Modifier.padding(bottom = 5.dp))
            Dropdown(
                listOf("House", "Spot"),
                onSelect = { index, item -> },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            var visible by remember { mutableStateOf(false) }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { visible = true }) {
                Label("Start")
            }
            ConfirmDialog(
                visible,
                "Lazy dog",
                "The quick brown fox jumps over the lazy dog",
                { println("Dismiss"); visible = false },
                { println("Confirm"); visible = false }
            )
        }
    }

    companion object {
        fun Preview(): Screen {
            return DashboardScreen("Robot", EStatus.CONNECTED, 85, 198694)
        }
    }
}
