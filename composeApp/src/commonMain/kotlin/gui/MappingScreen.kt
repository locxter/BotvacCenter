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
import botvaccenter.composeapp.generated.resources.Res
import botvaccenter.composeapp.generated.resources.map
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gui.components.Label
import gui.components.Navigation
import gui.components.Title
import org.jetbrains.compose.resources.painterResource


class MappingScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

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
