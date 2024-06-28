package gui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Composable
@Preview
fun TitlePreview() {
    Title("Title")
}