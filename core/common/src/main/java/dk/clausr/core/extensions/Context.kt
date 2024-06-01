package dk.clausr.core.extensions

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


fun Context.createTempPictureFile(
//    provider: String = "$packageName.provider",
    fileName: String = "picture_${System.currentTimeMillis()}",
    fileExtension: String = ".jpg",
): File {
    val tempFile = File.createTempFile(
        fileName,
        fileExtension,
        cacheDir,
    ).apply {
        createNewFile()
    }

    return tempFile
//    return FileProvider.getUriForFile(applicationContext, provider, tempFile)
}

fun ContentResolver.createFileFromUri(uri: Uri, cacheDir: File): File? {
    val tempFile = File.createTempFile("upload_file_", ".jpg", cacheDir)
    Timber.d("Temp file location: $tempFile")
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null

    try {
        inputStream = this.openInputStream(uri)
        outputStream = FileOutputStream(tempFile)

        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        return tempFile
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}


fun Context.openNotificationSettings() {
    val notificationSettingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    }

    startActivity(notificationSettingsIntent)
}