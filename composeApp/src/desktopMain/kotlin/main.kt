import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import botvaccenter.composeapp.generated.resources.Res
import botvaccenter.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    val icon = painterResource(Res.drawable.icon)
    Window(
        onCloseRequest = ::exitApplication,
        title = "BotvacCenter",
        icon = icon
    ) {
        App()
    }
}