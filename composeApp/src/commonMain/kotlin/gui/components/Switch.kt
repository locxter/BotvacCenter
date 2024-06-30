package gui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material.Switch(
        checked, onCheckedChange, modifier, enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.primary,
            checkedTrackColor = MaterialTheme.colors.primary,
        )

    )
}

@Composable
@Preview
fun SwitchPreview() {
    Switch(true, {})
}