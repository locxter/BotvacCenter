package gui

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
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.DirectionButton
import gui.components.Dropdown
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import model.EDirection

data class RemoteScreen(
    var brushRpm: Int,
    var vacuumDutyCycle: Int,
    var isSideBrushEnabled: Boolean
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var brushRpmInput by remember { mutableStateOf(brushRpm) }
        var vacuumDutyCycleInput by remember { mutableStateOf(vacuumDutyCycle) }
        var sideBrushEnabledInput by remember { mutableStateOf(isSideBrushEnabled) }
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
                DirectionButton(EDirection.DIRECTION_UP, {})
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 20.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(EDirection.DIRECTION_LEFT, {})
                Spacer(Modifier.size(115.dp, 75.dp))
                DirectionButton(EDirection.DIRECTION_RIGHT, {})
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = Modifier.padding(bottom = 5.dp)) {
                Spacer(Modifier.weight(1f))
                DirectionButton(EDirection.DIRECTION_DOWN, {})
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.weight(1f))
            Label("Brush:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = brushRpmInput.toString(),
                onValueChange = {
                    brushRpmInput = try {
                        Integer.valueOf(it)
                    } catch (_: Exception) {
                        0
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Label("Vacuum:", modifier = Modifier.padding(bottom = 5.dp))
            OutlinedTextField(
                value = vacuumDutyCycleInput.toString(),
                onValueChange = {
                    brushRpmInput = try {
                        Integer.valueOf(it)
                    } catch (_: Exception) {
                        0
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Label("Side brush:", modifier = Modifier.padding(bottom = 5.dp))
            Dropdown(
                listOf("On", "Off"),
                selected = if (sideBrushEnabledInput) 0 else 1,
                onSelect = { index, item -> sideBrushEnabledInput = index > 0 }
            )
        }
    }

    companion object {
        fun Preview(): Screen {
            return RemoteScreen(0, 0, false)
        }
    }
}