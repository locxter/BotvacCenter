package gui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Navigation(
    onClick: () -> Unit,
    isBack: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            if (isBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow back",
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow forward"
                )
            }
            content()
        }
    }
}