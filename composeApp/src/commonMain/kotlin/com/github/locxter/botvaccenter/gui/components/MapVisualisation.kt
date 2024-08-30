package com.github.locxter.botvaccenter.gui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import com.github.locxter.botvaccenter.model.Botvac
import com.github.locxter.botvaccenter.model.Path
import com.github.locxter.botvaccenter.model.Point
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MapVisualization(
    botvac: Botvac,
    path: Path,
    onClick: (point: Point) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = Color(18, 18, 18)
    val mapColor = Color(255, 255, 255)
    val pathColor = Color(255, 0, 0)
    val robotColor = Color(0, 255, 0)
    val tempBotvac = botvac.getDeepCopy()
    val tempPath = path.getDeepCopy()
    val textMeasurer = rememberTextMeasurer()
    var scalingRatio by remember { mutableStateOf(0.0f) }
    var xOffset by remember { mutableStateOf(0.0f) }
    var yOffset by remember { mutableStateOf(0.0f) }
    var xMin by remember { mutableStateOf(0) }
    var xMax by remember { mutableStateOf(0) }
    var yMin by remember { mutableStateOf(0) }
    var yMax by remember { mutableStateOf(0) }
    var xRange by remember { mutableStateOf(0) }
    var yRange by remember { mutableStateOf(0) }
    var maxRange by remember { mutableStateOf(0) }
    if (tempBotvac.map.points.isNotEmpty()) {
        var tempXMin = 0
        var tempXMax = 0
        var tempYMin = 0
        var tempYMax = 0
        // Determine value ranges
        for (point in tempBotvac.map.points) {
            if (point.x < tempXMin) {
                tempXMin = point.x
            } else if (point.x > tempXMax) {
                tempXMax = point.x
            }
            if (point.y < tempYMin) {
                tempYMin = point.y
            } else if (point.y > tempYMax) {
                tempYMax = point.y
            }
        }
        xMin = tempXMin
        xMax = tempXMax
        yMin = tempYMin
        yMax = tempYMax
        xRange = abs(xMin) + abs(xMax)
        yRange = abs(yMin) + abs(yMax)
        maxRange = max(xRange, yRange)
        // Move all the data to positive values
        tempBotvac.map.points.replaceAll {
            Point(it.x - xMin, it.y - yMin)
        }
        tempBotvac.location = Point(tempBotvac.location.x - xMin, tempBotvac.location.y - yMin)
        tempPath.points.replaceAll {
            Point(it.x - xMin, it.y - yMin)
        }
        tempPath.points.add(0, Point(tempBotvac.location.x, tempBotvac.location.y))
    }
    Canvas(
        modifier = modifier.clip(RectangleShape).pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if (scalingRatio > 0.0f && tapOffset.x > xOffset && tapOffset.x < size.width - xOffset &&
                        tapOffset.y > yOffset && tapOffset.y < size.height - yOffset
                    ) {
                        onClick(
                            Point(
                                (((tapOffset.x - xOffset) * (1 / scalingRatio)) + xMin).roundToInt(),
                                (((size.height - (yOffset + tapOffset.y)) * (1 / scalingRatio)) + yMin).roundToInt()
                            )
                        )
                    }
                }
            )
        }
    ) {
        val width = size.width
        val height = size.height
        drawRect(color = backgroundColor, size = size)
        if (tempBotvac.map.points.isEmpty()) {
            val text = "Enable mapping to see the visualization."
            val textLayoutResult = textMeasurer.measure(text = AnnotatedString(text))
            val textSize = textLayoutResult.size
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    (width - textSize.width) / 2f,
                    (height - textSize.height) / 2f
                ),
                style = TextStyle(color = mapColor, textAlign = TextAlign.Center)
            )
            scalingRatio = 0.0f
            xOffset = 0.0f
            yOffset = 0.0f
        } else {
            // Calculate the scaling ratio and center the canvas
            if (width / height > xRange / yRange.toFloat()) {
                scalingRatio = height / yRange
                xOffset = (width - (xRange * scalingRatio)) / 2.0f
                yOffset = 0.0f
            } else {
                scalingRatio = width / xRange
                xOffset = 0.0f
                yOffset = (height - (yRange * scalingRatio)) / 2.0f
            }
            translate(xOffset, yOffset) {
                // Draw the map and path
                val mapPoints = tempBotvac.map.points.map {
                    Offset((it.x * scalingRatio), ((yRange - it.y) * scalingRatio))
                }
                val pathPoints = tempPath.points.map {
                    Offset((it.x * scalingRatio), ((yRange - it.y) * scalingRatio))
                }
                drawPoints(
                    points = mapPoints,
                    pointMode = PointMode.Points,
                    color = mapColor,
                    strokeWidth = max(maxRange * 0.01f * scalingRatio, 4.0f),
                    cap = StrokeCap.Round
                )
                drawPoints(
                    points = pathPoints,
                    pointMode = PointMode.Polygon,
                    color = pathColor,
                    strokeWidth = max(maxRange * 0.0025f * scalingRatio, 1.0f),
                    cap = StrokeCap.Round
                )
                if (pathPoints.isNotEmpty()) {
                    drawCircle(
                        color = pathColor,
                        radius = max(maxRange * 0.0075f * scalingRatio, 3.0f),
                        center = pathPoints.last()
                    )
                }
                // Draw the robot and its movement direction
                drawCircle(
                    color = robotColor,
                    radius = max(maxRange * 0.01f * scalingRatio, 4.0f),
                    center = Offset(
                        (tempBotvac.location.x * scalingRatio),
                        ((yRange - tempBotvac.location.y) * scalingRatio)
                    )
                )
                drawCircle(
                    color = robotColor,
                    radius = 250 * scalingRatio,
                    center = Offset(
                        (tempBotvac.location.x * scalingRatio),
                        ((yRange - tempBotvac.location.y) * scalingRatio)
                    ),
                    style = Stroke(width = max(maxRange * 0.0025f * scalingRatio, 1.0f))
                )
                drawLine(
                    color = robotColor,
                    start = Offset(
                        (tempBotvac.location.x * scalingRatio),
                        ((yRange - tempBotvac.location.y) * scalingRatio)
                    ),
                    end = Offset(
                        (tempBotvac.location.x * scalingRatio) +
                                (max(maxRange * 0.05f * scalingRatio, 20.0f)
                                        * sin(tempBotvac.angle * (PI / 180.0)).toFloat()),
                        ((yRange - tempBotvac.location.y) * scalingRatio) +
                                (max(maxRange * 0.05f * scalingRatio, 20.0f)
                                        * cos((180 - tempBotvac.angle) * (PI / 180.0)).toFloat())
                    ),
                    strokeWidth = max(maxRange * 0.005f * scalingRatio, 2.0f)
                )
            }
        }
    }
}

@Composable
@Preview
fun MapVisualizationPreview() {
    MapVisualization(Botvac(), Path(), {})
}