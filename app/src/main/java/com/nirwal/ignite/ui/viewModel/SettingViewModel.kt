package com.nirwal.ignite.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nirwal.ignite.common.MyDataStore
import com.nirwal.ignite.worker.RandomWallpaperWorkerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(private val context:Context, private val dataStore:MyDataStore):ViewModel() {
    companion object{
        const val IS_WORK_MANAGER_ON        = "IS_WORK_MANAGER_ON"
        const val IS_WIFI_REQUIRED          = "IS_WIFI_REQUIRED"
        const val IS_CHARGING_REQUIRED      = "IS_CHARGING_REQUIRED"
        const val IS_PHONE_IDEAL_REQUIRED   = "IS_PHONE_IDEAL_REQUIRED"
    }

    val isWorkManagerOnFlow = dataStore.getBoolean(IS_WORK_MANAGER_ON,false)
    val isWifiReqFlow = dataStore.getBoolean(IS_WIFI_REQUIRED,false)
    val isChargingReqFlow = dataStore.getBoolean(IS_CHARGING_REQUIRED,false)
    val isPhoneIdealReqFlow = dataStore.getBoolean(IS_PHONE_IDEAL_REQUIRED,false)



init {
    viewModelScope.launch {

        isWorkManagerOnFlow.collect{
            println( "saving gettin data, iswork = $it")
        }
    }
}

    fun onConfigurationChange(cofig:HashMap<String,Boolean>){
        val isWorkManagerOn = cofig[IS_WORK_MANAGER_ON] ?: false
        val isWifiReq= cofig[IS_WIFI_REQUIRED] ?: false
        val isChargingReq = cofig[IS_CHARGING_REQUIRED] ?: false
        val isPhoneIdeal = cofig[IS_PHONE_IDEAL_REQUIRED] ?: false
        CoroutineScope(Dispatchers.IO).launch{
            println("saving data, iswork = $isWorkManagerOn")
            dataStore.saveBoolean(IS_WORK_MANAGER_ON,isWorkManagerOn)
            dataStore.saveBoolean(IS_WIFI_REQUIRED,isWifiReq)
            dataStore.saveBoolean(IS_CHARGING_REQUIRED,isWifiReq)
            dataStore.saveBoolean(IS_PHONE_IDEAL_REQUIRED, isPhoneIdeal)
        }


        if(isWorkManagerOn){

            val constraints = Constraints.Builder().apply {
                setRequiredNetworkType(NetworkType.CONNECTED)
                if (isChargingReq==true) {setRequiresCharging(true)}
                if (isWifiReq==true) {setRequiredNetworkType(NetworkType.UNMETERED)}
                if (isPhoneIdeal==true) {setRequiresDeviceIdle(true)}
            }.build()
            startServiceWorker(constraints)
        }else{
            stopServiceWorker()
        }

    }


    private fun stopServiceWorker(){
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag("imageWork")
    }

    private fun startServiceWorker(constraints: Constraints){
        val workManager = WorkManager.getInstance(context)
        val imageWorker = PeriodicWorkRequestBuilder<RandomWallpaperWorkerManager>(
            20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("imageWork")
            .build()
        // 2
        workManager.enqueueUniquePeriodicWork(
            "oneTimeImageDownload",
            ExistingPeriodicWorkPolicy.UPDATE,
            imageWorker
        )
    }
}