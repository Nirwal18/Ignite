package com.nirwal.ignite.ui.screens

import android.util.MutableBoolean
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpaper
import androidx.compose.ui.unit.dp
import com.nirwal.ignite.common.Divider
import com.nirwal.ignite.common.MyAppBar
import com.nirwal.ignite.ui.viewModel.SettingViewModel

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
//    SettingScreen(
//        false,
//        false,
//        false,
//        false,
//        onBack = {},
//        {}
//    )
}


@Composable
fun SettingScreen(
    isAutoWallpaperOn: Boolean,
    isWifiReq:Boolean,
    isChargingReq:Boolean,
    isIdealReq:Boolean,
    onBack:()->Unit,
    onConfigurationChange:(HashMap<String,Boolean>
    )->Unit) {
    Column {

        var isAutoChangeEnable by remember {
            mutableStateOf(false)
        }

        var isWiFiSelected by remember {
            mutableStateOf(false)
        }
        var isChargingSelected by remember {
            mutableStateOf(false)
        }
        var isIdleSelected by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(
            key1 = isAutoWallpaperOn || isWifiReq || isChargingReq || isIdealReq
        ){
            isAutoChangeEnable = isAutoWallpaperOn
            isChargingSelected = isChargingReq
            isWiFiSelected = isWifiReq
            isIdleSelected = isIdealReq
        }

        MyAppBar(title = "Settings", leadingIcon = Icons.Default.ArrowBack, onLeadingIconClick = onBack)

        Column(Modifier.verticalScroll(rememberScrollState())){
            SettingItem(
                title = "Auto change wallpapers",
                desc = "Change wallpapers periodically," +
                        " based on the conditions below."
            ) {
                Switch( checked = isAutoChangeEnable, onCheckedChange = {
                    isAutoChangeEnable = !isAutoChangeEnable
                    val configuration = hashMapOf<String, Boolean>().also {
                        it.put(SettingViewModel.IS_WORK_MANAGER_ON,isAutoChangeEnable)
                        it.put(SettingViewModel.IS_WIFI_REQUIRED,isWiFiSelected)
                        it.put(SettingViewModel.IS_CHARGING_REQUIRED,isChargingSelected)
                        it.put(SettingViewModel.IS_PHONE_IDEAL_REQUIRED, isIdleSelected)
                    }
                    onConfigurationChange(configuration)
                })
            }
            Divider()

            Text(modifier = Modifier.padding(start = 70.dp, top = 16.dp, bottom = 8.dp), text = "Conditions", style = MaterialTheme.typography.titleMedium)

            SettingItem(
                title = "On Wi-Fi",
                desc = "Device must be connected to Wi-Fi network."
            ) {
                Checkbox(checked = isWiFiSelected, enabled = isAutoChangeEnable, onCheckedChange = {
                    isWiFiSelected = !isWiFiSelected
                    val configuration = hashMapOf<String, Boolean>().also {
                        it.put(SettingViewModel.IS_WORK_MANAGER_ON,isAutoChangeEnable)
                        it.put(SettingViewModel.IS_WIFI_REQUIRED,isWiFiSelected)
                        it.put(SettingViewModel.IS_CHARGING_REQUIRED,isChargingSelected)
                        it.put(SettingViewModel.IS_PHONE_IDEAL_REQUIRED, isIdleSelected)
                    }
                    onConfigurationChange(configuration)
                })
            }
            SettingItem(
                title = "Charging",
                desc = "Device must be connected to power."
            ) {
                Checkbox( checked = isChargingSelected, enabled = isAutoChangeEnable, onCheckedChange = {
                    isChargingSelected = !isChargingSelected
                    val configuration = hashMapOf<String, Boolean>().also {
                        it.put(SettingViewModel.IS_WORK_MANAGER_ON,isAutoChangeEnable)
                        it.put(SettingViewModel.IS_WIFI_REQUIRED,isWiFiSelected)
                        it.put(SettingViewModel.IS_CHARGING_REQUIRED,isChargingSelected)
                        it.put(SettingViewModel.IS_PHONE_IDEAL_REQUIRED, isIdleSelected)
                    }
                    onConfigurationChange(configuration)
                })
            }

            SettingItem(
                title = "Idle",
                desc = "Device must be inactive."
            ) {
                Checkbox( checked = isIdleSelected, enabled = isAutoChangeEnable, onCheckedChange = {
                    isIdleSelected = !isIdleSelected
                    val configuration = hashMapOf<String, Boolean>().also {
                        it.put(SettingViewModel.IS_WORK_MANAGER_ON,isAutoChangeEnable)
                        it.put(SettingViewModel.IS_WIFI_REQUIRED,isWiFiSelected)
                        it.put(SettingViewModel.IS_CHARGING_REQUIRED,isChargingSelected)
                        it.put(SettingViewModel.IS_PHONE_IDEAL_REQUIRED, isIdleSelected)
                    }
                    onConfigurationChange(configuration)
                })
            }
            Divider()

            Text(modifier = Modifier.padding(start = 70.dp, top = 16.dp, bottom = 8.dp), text = "Options", style = MaterialTheme.typography.titleMedium)

            SettingItem(
                title = "Interval",
                desc = "Each wallpaper will last a minimum of 3 hours."
            ) {}
            SettingItem(
                title = "Screen",
                desc = "Home and lock screen"
            ) {}
        }


    }
}



@Composable
fun SettingItem(title:String, desc:String, controlContent: @Composable ()-> Unit) {
    Row(modifier = Modifier.padding(start = 70.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)){
       Column(Modifier.weight(1f)) {
           Text(text = title, style = MaterialTheme.typography.titleMedium)
           Text(text = desc)
       }
        Box(Modifier.width(50.dp)){
            controlContent()
        }
    }
}