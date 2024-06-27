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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gui.components.Dropdown
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import model.EStatus
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardScreen(
    alias: String,
    status: EStatus,
    charge: Int,
    cleanTime: Long
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        Title(text = alias, modifier = Modifier.padding(bottom = 10.dp))
        Label(text = "Status: ${status.displayName}", modifier = Modifier.padding(bottom = 5.dp))
        Label(text = "Status: $charge%", modifier = Modifier.padding(bottom = 5.dp))
        Label(
            text = "Clean time: ${cleanTime / 3600}:${(cleanTime % 3600) / 60}:${cleanTime % 60}",
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Spacer(Modifier.weight(1f))
        for (text in listOf("Remote", "Schedule", "Diagnostics", "Mapping", "Settings")) {
            Navigation(onClick = {}, modifier = Modifier.padding(bottom = 5.dp)) {
                Label(text)
            }
        }
        Spacer(Modifier.weight(1f))
        Label("Mode:", modifier = Modifier.padding(bottom = 5.dp))
        Dropdown(
            listOf("House", "Spot"),
            onSelect = { index, item -> },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
            Label("Start")
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen("Robot", EStatus.STATUS_CONNECTED, 85, 198694)
}