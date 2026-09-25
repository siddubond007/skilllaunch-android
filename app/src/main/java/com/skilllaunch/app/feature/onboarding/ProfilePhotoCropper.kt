package com.skilllaunch.app.feature.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
internal fun ProfilePhotoCropper(
    sourceUri: Uri,
    colors: ProfileSetupColors,
    onDismiss: () -> Unit,
    onCropped: (Uri) -> Unit
) {
    val context = LocalContext.current
    val bitmapState = remember(sourceUri) { mutableStateOf<Bitmap?>(null) }
    var zoom by remember(sourceUri) { mutableFloatStateOf(1f) }
    var offset by remember(sourceUri) { mutableStateOf(Offset.Zero) }
    var error by remember(sourceUri) { mutableStateOf("") }

    LaunchedEffect(sourceUri) {
        bitmapState.value = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(sourceUri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }
        if (bitmapState.value == null) {
            error = "We couldn't read that image. Please choose another photo."
        }
    }

    val bitmap = bitmapState.value
    val viewportDp = 280.dp

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crop profile photo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Drag the photo to position it, then adjust the zoom.",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )

                if (bitmap != null) {
                    CropPreview(
                        bitmap = bitmap.asImageBitmap(),
                        bitmapWidth = bitmap.width,
                        bitmapHeight = bitmap.height,
                        viewportSizeDp = viewportDp,
                        zoom = zoom,
                        offset = offset,
                        onOffsetChange = { offset = it }
                    )

                    Text(
                        text = "Zoom",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )

                    Slider(
                        value = zoom,
                        onValueChange = { zoom = it },
                        valueRange = 1f..4f
                    )

                    Text(
                        text = "Square crop • 600 × 600",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                }

                if (error.isNotBlank()) {
                    Text(
                        text = error,
                        color = colors.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val current = bitmap ?: return@Button
                    val output = cropBitmapToCache(
                        context = context,
                        bitmap = current,
                        viewportPx = viewportDp.toPx(context),
                        zoom = zoom,
                        offset = offset
                    )
                    if (output != null) {
                        onCropped(output)
                    } else {
                        error = "We couldn't crop this photo. Please try another image."
                    }
                },
                enabled = bitmap != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.accentText
                )
            ) {
                Text("Crop & upload")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun CropPreview(
    bitmap: ImageBitmap,
    bitmapWidth: Int,
    bitmapHeight: Int,
    viewportSizeDp: androidx.compose.ui.unit.Dp,
    zoom: Float,
    offset: Offset,
    onOffsetChange: (Offset) -> Unit
) {
    val density = LocalContext.current.resources.displayMetrics.density
    val viewportPx = viewportSizeDp.value * density
    val baseScale = max(
        viewportPx / bitmapWidth.toFloat(),
        viewportPx / bitmapHeight.toFloat()
    )
    val totalScale = baseScale * zoom

    Box(
        modifier = Modifier
            .size(viewportSizeDp)
            .background(Color.Black, RoundedCornerShape(20.dp))
            .pointerInput(bitmap, zoom) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    var currentOffset = offset
                    val drawWidth = bitmapWidth * totalScale
                    val drawHeight = bitmapHeight * totalScale
                    val maxX = max(0f, (drawWidth - viewportPx) / 2f)
                    val maxY = max(0f, (drawHeight - viewportPx) / 2f)

                    currentOffset = Offset(
                        x = (currentOffset.x + dragAmount.x).coerceIn(-maxX, maxX),
                        y = (currentOffset.y + dragAmount.y).coerceIn(-maxY, maxY)
                    )
                    onOffsetChange(currentOffset)
                }
            }
    ) {
        Canvas(modifier = Modifier.size(viewportSizeDp)) {
            val drawWidth = bitmapWidth * totalScale
            val drawHeight = bitmapHeight * totalScale
            val maxX = max(0f, (drawWidth - viewportPx) / 2f)
            val maxY = max(0f, (drawHeight - viewportPx) / 2f)
            val safeOffsetX = offset.x.coerceIn(-maxX, maxX)
            val safeOffsetY = offset.y.coerceIn(-maxY, maxY)
            val left = (size.width - drawWidth) / 2f + safeOffsetX
            val top = (size.height - drawHeight) / 2f + safeOffsetY

            withTransform({
                translate(left = left, top = top)
                scale(scaleX = totalScale, scaleY = totalScale, pivot = Offset.Zero)
            }) {
                drawImage(bitmap)
            }

            drawRoundRect(
                color = Color.White.copy(alpha = 0.9f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    20.dp.toPx(),
                    20.dp.toPx()
                ),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

private fun cropBitmapToCache(
    context: Context,
    bitmap: Bitmap,
    viewportPx: Float,
    zoom: Float,
    offset: Offset
): Uri? {
    return runCatching {
        val baseScale = max(
            viewportPx / bitmap.width.toFloat(),
            viewportPx / bitmap.height.toFloat()
        )
        val totalScale = baseScale * zoom
        val drawWidth = bitmap.width * totalScale
        val drawHeight = bitmap.height * totalScale
        val maxX = max(0f, (drawWidth - viewportPx) / 2f)
        val maxY = max(0f, (drawHeight - viewportPx) / 2f)
        val safeOffsetX = offset.x.coerceIn(-maxX, maxX)
        val safeOffsetY = offset.y.coerceIn(-maxY, maxY)
        val left = (viewportPx - drawWidth) / 2f + safeOffsetX
        val top = (viewportPx - drawHeight) / 2f + safeOffsetY

        val sourceLeft = ((-left) / totalScale).roundToInt()
            .coerceIn(0, max(0, bitmap.width - 1))
        val sourceTop = ((-top) / totalScale).roundToInt()
            .coerceIn(0, max(0, bitmap.height - 1))
        val sourceWidth = (viewportPx / totalScale).roundToInt()
            .coerceIn(1, bitmap.width - sourceLeft)
        val sourceHeight = (viewportPx / totalScale).roundToInt()
            .coerceIn(1, bitmap.height - sourceTop)
        val cropSize = minOf(sourceWidth, sourceHeight)

        val src = android.graphics.Rect(
            sourceLeft,
            sourceTop,
            sourceLeft + cropSize,
            sourceTop + cropSize
        )
        val output = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888)

        android.graphics.Canvas(output).drawBitmap(
            bitmap,
            src,
            android.graphics.Rect(0, 0, 600, 600),
            android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
        )

        val file = File(
            context.cacheDir,
            "profile-${System.currentTimeMillis()}.jpg"
        )
        FileOutputStream(file).use { stream ->
            output.compress(Bitmap.CompressFormat.JPEG, 92, stream)
        }
        output.recycle()
        Uri.fromFile(file)
    }.getOrNull()
}

private fun androidx.compose.ui.unit.Dp.toPx(context: Context): Float =
    value * context.resources.displayMetrics.density
