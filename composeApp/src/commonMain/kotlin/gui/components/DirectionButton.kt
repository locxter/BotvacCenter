package gui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.EDirection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DirectionButton(
    direction: EDirection,
    onClick: () -> Unit,
    size: Dp = 75.dp,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier.size(size)) {
        Icon(
            imageVector = when (direction) {
                EDirection.DIRECTION_UP -> Icons.Filled.KeyboardArrowUp
                EDirection.DIRECTION_RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                EDirection.DIRECTION_DOWN -> Icons.Filled.KeyboardArrowDown
                EDirection.DIRECTION_LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
            },
            contentDescription = "Arrow ${
                when (direction) {
                    EDirection.DIRECTION_UP -> "up"
                    EDirection.DIRECTION_RIGHT -> "right"
                    EDirection.DIRECTION_DOWN -> "down"
                    EDirection.DIRECTION_LEFT -> "left"
                }
            }",
            modifier = Modifier.size(size * 0.75f)
        )
    }
}

@Composable
@Preview
fun DirectionButtonPreview() {
    DirectionButton(EDirection.DIRECTION_UP, {})
}