import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import gui.DashboardScreen
import lib.BotvacController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val botvacController = BotvacController(50, 0.01)
    botvacController.connect("http://btvcbrdg.local", "btvcbrdg", "btvcbrdg")
    botvacController.cleanHouse()
    Thread.sleep(5000)
    botvacController.stopCleaning()
    botvacController.disconnect()
    MaterialTheme {
        Navigator(DashboardScreen.Preview()) { navigator ->
            SlideTransition(navigator)
        }
    }
}
