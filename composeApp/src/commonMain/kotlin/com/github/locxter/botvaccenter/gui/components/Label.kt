package com.github.locxter.botvaccenter.gui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Label(
    text: String,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        textAlign = textAlign,
        modifier = modifier
    )
}

@Composable
@Preview
fun LabelPreview() {
    Label("Label")
}