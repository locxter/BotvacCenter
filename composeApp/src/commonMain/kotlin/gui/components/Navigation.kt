package gui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun Navigation(
    onClick: () -> Unit,
    isBack: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    TextButton(onClick = onClick, modifier = modifier.onGloballyPositioned { coordinates ->
        // This value is used to assign to
        // the DropDown the same width
        textFieldSize = coordinates.size.toSize()
    },) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            if (isBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow back",
                    modifier = Modifier.size(with(LocalDensity.current){(textFieldSize.height * 0.5f).toDp()})
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow forward",
                    modifier = Modifier.size(with(LocalDensity.current){(textFieldSize.height * 0.5f).toDp()})
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            content()
        }
    }
}