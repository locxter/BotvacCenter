package com.github.locxter.botvaccenter.gui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun InfoDialog(visible: Boolean, message: String, onDismissRequest: () -> Unit) {
    if (visible) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Label(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(15.dp),
                )
            }
        }
    }
}