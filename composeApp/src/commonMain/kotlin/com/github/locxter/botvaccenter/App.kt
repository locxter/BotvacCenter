package com.github.locxter.botvaccenter

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.github.locxter.botvaccenter.gui.HomeScreen
import com.github.locxter.botvaccenter.lib.BotvacController
import com.github.locxter.botvaccenter.lib.SettingsController
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    val settings = SettingsController().readSettings()
    val botvacController = BotvacController(50, 0.01)
    MaterialTheme(colors = darkColors()) {
        Surface {
            Navigator(HomeScreen(settings, botvacController)) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}