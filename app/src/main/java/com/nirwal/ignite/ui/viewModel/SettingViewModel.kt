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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(private val context:Context, private val dataStore:MyDataStore):ViewModel() {
    companion object{
        const val IS_WORK_MANAGER_ON        = "IS_WORK_MANAGER_ON"
        const val IS_WIFI_REQUIRED          = "IS_WIFI_REQUIRED"
        const val IS_CHARGING_REQUIRED      = "IS_CHARGING_REQUIRED"
        const val IS_PHONE_IDEAL_REQUIRED   = "IS_PHONE_IDEAL_REQUIRED"
        const val INTERVAL_IN_SECONDS       = "INTERVAL_IN_SECONDS"
        const val WALLPAPER_APPLY_OPTION    = "WALLPAPER_APPLY_OPTION"
    }

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState = _uiState.asStateFlow()

init {

    viewModelScope.launch {
        _uiState.update {
            it.copy(
                defaultInterval =  dataStore.getLong(INTERVAL_IN_SECONDS,60*20).first(),
                isWorkerStarted = dataStore.getBoolean(IS_WORK_MANAGER_ON,false).first(),
                isWifiRequired = dataStore.getBoolean(IS_WIFI_REQUIRED,false).first(),
                isChargingRequired = dataStore.getBoolean(IS_CHARGING_REQUIRED,false).first(),
                isPhoneIdealRequired = dataStore.getBoolean(IS_PHONE_IDEAL_REQUIRED,false).first(),
                defaultSelectedWallpaperOption = dataStore.getInt(WALLPAPER_APPLY_OPTION,0).first()
            ) }
    }
}



    private fun stopServiceWorker(){
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag("imageWork")
    }

    private fun startServiceWorker(){
        val constraints = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.CONNECTED)
            setRequiresCharging(_uiState.value.isChargingRequired)
            setRequiresDeviceIdle(_uiState.value.isPhoneIdealRequired)
        }.build()

        val workManager = WorkManager.getInstance(context)
        val imageWorker = PeriodicWorkRequestBuilder<RandomWallpaperWorkerManager>(
            _uiState.value.defaultInterval, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .addTag("imageWork")
            .build()
        // 2
        workManager.enqueueUniquePeriodicWork(
            "imageWork",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            imageWorker
        )
    }


    fun onEvent(event: UiEvent){
        viewModelScope.launch {
            when (event) {
                is UiEvent.OnIntervalChange -> {
                    dataStore.saveLong(INTERVAL_IN_SECONDS,event.intervalInSecond)
                    _uiState.update { it.copy(defaultInterval =  event.intervalInSecond) }
                }

                is UiEvent.StartWorker -> {
                    if(event.start){
                        startServiceWorker()
                    } else{
                        stopServiceWorker()
                    }
                    _uiState.update { it.copy(isWorkerStarted = event.start) }
                    dataStore.saveBoolean(IS_WORK_MANAGER_ON,event.start)
                }

                is UiEvent.SetWifiRequired-> {
                    dataStore.saveBoolean(IS_WIFI_REQUIRED,event.required)
                    _uiState.update { it.copy(isWifiRequired =  event.required) }
                }

                is UiEvent.SetChargingRequired -> {
                    dataStore.saveBoolean(IS_CHARGING_REQUIRED,event.required)
                    _uiState.update { it.copy(isChargingRequired =  event.required) }
                }

                is UiEvent.SetIdealRequired -> {
                    dataStore.saveBoolean(IS_PHONE_IDEAL_REQUIRED,event.required)
                    _uiState.update { it.copy(isPhoneIdealRequired =  event.required) }
                }

                is UiEvent.ShowIntervalDialog -> {
                    _uiState.update { it.copy(shouldShowIntervalDialog =  event.show) }
                }

                is UiEvent.ShowWallpaperApplyOptionDialog -> {
                    _uiState.update { it.copy(shouldShowWallpaperApplyOptionDialog =  event.show) }
                }

                is UiEvent.OnWallpaperApplyOptionChange -> {
                    _uiState.update { it.copy(defaultSelectedWallpaperOption =  event.option) }
                    dataStore.saveInt(WALLPAPER_APPLY_OPTION,event.option)
                }
            }
        }
    }



    data class SettingUiState(
        val shouldShowIntervalDialog:Boolean = false,
        val shouldShowWallpaperApplyOptionDialog: Boolean = false,

        val defaultSelectedWallpaperOption:Int = 0,

        val defaultInterval:Long = 0,
        val isWifiRequired:Boolean = false,
        val isChargingRequired:Boolean = false,
        val isPhoneIdealRequired:Boolean = false,
        val isWorkerStarted:Boolean = false
    )

    sealed class UiEvent(){
        data class OnIntervalChange(val intervalInSecond:Long): UiEvent()
        data class SetWifiRequired(val required:Boolean):UiEvent()
        data class SetChargingRequired(val required:Boolean):UiEvent()
        data class SetIdealRequired(val required:Boolean):UiEvent()
        data class StartWorker(val start:Boolean):UiEvent()
        data class ShowIntervalDialog(val show:Boolean):UiEvent()
        data class ShowWallpaperApplyOptionDialog(val show:Boolean):UiEvent()
        data class OnWallpaperApplyOptionChange(val option:Int):UiEvent()
    }


}