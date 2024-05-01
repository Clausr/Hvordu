package dk.clausr.koncert.ui.camera

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dk.clausr.koncert.ui.compose.theme.KoncertTheme
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreviewScreen(
    enableTakeImageButton: Boolean,
    modifier: Modifier = Modifier,
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    var takePictureInProgress by remember {
        mutableStateOf(false)
    }
    val colors = MaterialTheme.colorScheme
    val takePictureEnabled = !takePictureInProgress && enableTakeImageButton

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .padding(bottom = KoncertTheme.dimensions.padding16)
                .clip(CircleShape)
                .size(80.dp)
                .drawWithCache {
                    onDrawBehind {
                        drawCircle(colors.surface)

                        drawOval(
                            color = Color.Black,
                            topLeft = Offset(x = size.width / 10, y = size.height / 10),
                            size = size.times(0.8f),
                            style = Stroke(width = 10f),
                            blendMode = BlendMode.Clear,
                        )
                    }
                }
                .clickable(enabled = takePictureEnabled) {
                    takePictureInProgress = true
                    captureImage(
                        imageCapture = imageCapture,
                        context = context,
                        pictureResult = {
                            takePictureInProgress = false

                            Timber.d("${it.getOrNull()?.savedUri}")
                        }
                    )
                },
        )
    }
}

private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    pictureResult: (Result<ImageCapture.OutputFileResults>) -> Unit,
) {
    val name = "CameraxImage.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println("Successs")
                pictureResult(Result.success(outputFileResults))
            }

            override fun onError(exception: ImageCaptureException) {
                pictureResult(Result.failure(exception))
                println("Failed $exception")
            }

        })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }