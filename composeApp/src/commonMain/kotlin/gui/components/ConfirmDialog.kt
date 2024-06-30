package gui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            title = {
                Label(text = title)
            },
            text = {
                Label(text = message)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    },
                    modifier = Modifier.padding(end = 10.dp, bottom = 10.dp)
                ) {
                    Label("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Label("Dismiss")
                }
            }
        )
    }
}