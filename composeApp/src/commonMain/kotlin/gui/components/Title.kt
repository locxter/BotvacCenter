package gui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Title(
    text: String,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 38.sp,
        lineHeight = (47.5).sp,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        modifier = modifier
    )
}

@Composable
@Preview
fun TitlePreview() {
    Title("Title")
}