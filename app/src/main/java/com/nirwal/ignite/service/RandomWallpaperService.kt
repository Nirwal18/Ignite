package com.nirwal.ignite.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nirwal.ignite.MyNotification
import com.nirwal.ignite.R
import com.nirwal.ignite.common.Utils
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.repositry.PhotoRepositoryImp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class RandomWallpaperService: Service() {
    override fun onBind(p0: Intent?): IBinder? =null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("Random wallpaper Service Running")
        setForegroundMode()
        setWallpaper()
        stopSelf()
        return START_STICKY
    }

    fun setWallpaper(){
        CoroutineScope(Dispatchers.IO).launch {
            println("RandomWallpaperWorkerManager running")
            var photos = getPhotos()
            val imageCount = photos.size
            var retryCount = 0
            while(photos.isEmpty() && retryCount<=3){
                photos = getPhotos()
                delay(10_000)
                retryCount++
            }
            if (imageCount>0){
                val photo = photos.get(Random.nextInt(0,imageCount))
                Utils.setWallpaper(photo.src?.original.toString(), this@RandomWallpaperService)
                showToast("Applying wallpaper.\nPlease wait a little bit.")
            }else{
                showToast("Wallpaper download error .\nCheck network connectivity")
            }
        }
    }


    private suspend fun getPhotos(): List<Photo> {
        val randomPages = Random.nextInt(1,500)
        val repository = PhotoRepositoryImp().getCuratedPhotos(randomPages, 30)
        return repository?.photos?: listOf()
    }


    private fun showToast(msg:String){
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                this.applicationContext,
                msg,
                Toast.LENGTH_LONG
            ).show()
        })
    }


    private fun setForegroundMode(){
        startForeground(
            1101,
            NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_skull_small_icon)
                .setContentText("Downloading...")
                .setContentTitle("Downloading in progress")
                .build()
        )
    }

}