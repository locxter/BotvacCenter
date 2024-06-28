package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import botvaccontrol.composeapp.generated.resources.Res
import botvaccontrol.composeapp.generated.resources.map
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


class MappingScreen() : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Navigation(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(bottom = 10.dp),
                isBack = true
            ) {
                Title("Mapping")
            }
            Label("Not implemented yet", modifier = Modifier.padding(bottom = 5.dp))
            Spacer(Modifier.weight(1f))
            Image(
                painterResource(Res.drawable.map),
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "Map"
            )
            Spacer(Modifier.weight(1f))
        }
    }

    companion object {
        fun Preview(): Screen {
            return MappingScreen()
        }
    }
}
