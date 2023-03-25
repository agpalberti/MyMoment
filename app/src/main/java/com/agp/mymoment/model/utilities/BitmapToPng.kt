package com.agp.mymoment.model.utilities

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun bitmapToPNG(bitmap: Bitmap, context: Context): File?{

    try {
        val png = File(context.cacheDir, "filename.png")
        png.createNewFile()

        val outputStream = FileOutputStream(png)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        return png
    }catch (e:Exception){
        Log.e("User","El bitmap está vacío", e)
        return null
    }

}