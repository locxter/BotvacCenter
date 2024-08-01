package com.github.locxter.botvaccenter.gui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.github.locxter.botvaccenter.model.EDirection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DirectionButton(
    direction: EDirection,
    onClick: () -> Unit,
    enabled: Boolean = true,
    size: Dp = 75.dp,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(size),
        enabled = enabled,
        interactionSource = interactionSource
    ) {
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