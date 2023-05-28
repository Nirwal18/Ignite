package com.nirwal.ignite.worker

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.nirwal.ignite.MyNotification
import com.nirwal.ignite.R
import com.nirwal.ignite.common.Utils
import com.nirwal.ignite.domain.repositry.PhotoRepositoryImp
import com.nirwal.ignite.service.RandomWallpaperService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class RandomWallpaperWorkerManager(
    private val context:Context,
    private val workerParams:WorkerParameters
):CoroutineWorker(context,workerParams) {
    override suspend fun doWork(): Result {
        //startForegroundService()
//        CoroutineScope(Dispatchers.IO).launch {
//
//            println("RandomWallpaperWorkerManager running")
//            val repository = PhotoRepositoryImp().getCuratedPhotos(1, 100)
//            val imageCount = repository?.photos?.size ?: 0
//            if (imageCount>0){
//                val photo = repository?.photos?.get(Random.nextInt(0,imageCount))
//                Utils.setWallpaper(photo?.src?.original.toString(), context)
//            }
//        }
        val serviceIntent = Intent(context,RandomWallpaperService::class.java)
        context.startService(serviceIntent)
        return Result.success()
    }

    private suspend fun startForegroundService(){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context,MyNotification.CHANNEL_ID)
                    .setSmallIcon(R.drawable.fire_skul)
                    .setContentText("Downloading...")
                    .setContentTitle("Downloading in progress")
                    .build()
            )
        )
    }
}