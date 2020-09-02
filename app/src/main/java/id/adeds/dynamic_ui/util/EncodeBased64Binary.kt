package id.adeds.dynamic_ui.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object EncodeBased64Binary {
    fun bitmapToBase64(bitmap: Bitmap?): String {
        if (bitmap == null) return ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        return if (base64.isNotEmpty()) "Base64:$base64" else ""
    }

    fun getBase64FromPath(path: String): String {
        var base64 = ""
        if (path.isEmpty()) return ""
        try {
            val file = File(path)
            val buffer = ByteArray(file.length().toInt() + 100)
            val length = FileInputStream(file).read(buffer)
            base64 = Base64.encodeToString(
                buffer, 0, length,
                Base64.NO_WRAP
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (base64.isNotEmpty()) "Base64:$base64" else ""
    }

}