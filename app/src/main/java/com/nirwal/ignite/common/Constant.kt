package com.nirwal.ignite.common

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object Constant {
    const val TAG= "Ignite_debug"
    const val PIXEL_API_KEY = "oiYSeCNigY5RxZV8fFwwdIdqP3jeVw1P5kOyHhejM1hTJONWUQKFl31n"
}

object Utils{
    fun setWallpaper(imageUr:String, context:Context){
        val manager = WallpaperManager.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = getBitmap(imageUr, context)
            if(bitmap!=null){
                manager.setBitmap(bitmap,null, true,WallpaperManager.FLAG_SYSTEM)
            }
        }
    }

    suspend fun getBitmap(url: String, context: Context): Bitmap? {
        println(url)
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            bitmap
        } catch (e: Exception) {
            println("Download fail: ${e.message}")
            null
        }
    }

    fun setLockScreen(imageUr:String, context:Context){
        val manager = WallpaperManager.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = getBitmap(imageUr, context)
            if(bitmap!=null){
                manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            }
        }
    }

}