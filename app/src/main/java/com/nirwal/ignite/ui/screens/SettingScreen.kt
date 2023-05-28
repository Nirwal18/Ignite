package com.nirwal.ignite.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.MutableBoolean
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToHomeScreen
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhoneLocked
import androidx.compose.material.icons.filled.PunchClock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpaper
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection
import com.nirwal.ignite.common.Divider
import com.nirwal.ignite.common.MyAppBar
import com.nirwal.ignite.ui.viewModel.SettingViewModel
import io.ktor.http.headers

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingScreenPreview() {
    MaterialTheme(){

        SettingScreen(
            uiState = SettingViewModel.SettingUiState(isWorkerStarted = false),
            onUiEvent ={},
            onBack = {}
        )
    }
}


@Composable
fun SettingScreen(
    uiState:SettingViewModel.SettingUiState,
    onUiEvent: (event:SettingViewModel.UiEvent)->Unit,
    onBack:()->Unit
) {


    Column {


        MyAppBar(title = "Settings", leadingIcon = Icons.Default.ArrowBack, onLeadingIconClick = onBack)

        Column(Modifier.verticalScroll(rememberScrollState())){
            SettingItem(
                title = "Auto change wallpapers",
                desc = "Change wallpapers periodically," +
                        " based on the conditions below."
            ) {
                Switch( checked = uiState.isWorkerStarted, onCheckedChange = {
                   onUiEvent(SettingViewModel.UiEvent.StartWorker(!uiState.isWorkerStarted))
                })
            }
            if (uiState.isWorkerStarted){
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color.Red.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20)
                        ).padding(16.dp),
                    text = "Note: you can change below setting after AUTO WALLPAPER switch off.",
                    color = Color.Red
                )
            }
            Divider()

            Text(modifier = Modifier.padding(start = 70.dp, top = 16.dp, bottom = 8.dp), text = "Conditions", style = MaterialTheme.typography.titleMedium)

            SettingItem(
                title = "On Wi-Fi",
                desc = "Device must be connected to Wi-Fi network."
            ) {
                Checkbox(checked = uiState.isWifiRequired, enabled = !uiState.isWorkerStarted, onCheckedChange = {
                    onUiEvent(SettingViewModel.UiEvent.SetWifiRequired(!uiState.isWifiRequired))
                })
            }
            SettingItem(
                title = "Charging",
                desc = "Device must be connected to power."
            ) {
                Checkbox( checked = uiState.isChargingRequired, enabled = !uiState.isWorkerStarted, onCheckedChange = {
                    onUiEvent(SettingViewModel.UiEvent.SetChargingRequired(!uiState.isChargingRequired))
                })
            }

            SettingItem(
                title = "Idle",
                desc = "Device must be inactive."
            ) {
                Checkbox( checked = uiState.isPhoneIdealRequired, enabled = !uiState.isWorkerStarted, onCheckedChange = {
                    onUiEvent(SettingViewModel.UiEvent.SetIdealRequired(!uiState.isPhoneIdealRequired))
                })
            }
            Divider()

            Text(modifier = Modifier.padding(start = 70.dp, top = 16.dp, bottom = 8.dp), text = "Options", style = MaterialTheme.typography.titleMedium)

            SettingItem(
                modifier = Modifier.clickable {
                    onUiEvent(SettingViewModel.UiEvent.ShowIntervalDialog(!uiState.isWorkerStarted))
                },
                title = "Interval",
                desc = "Each wallpaper will last a minimum of ${uiState.defaultInterval/60} minutes."
            ) {}

            SettingItem(
                modifier = Modifier.clickable {
                    onUiEvent(SettingViewModel.UiEvent.ShowWallpaperApplyOptionDialog(!uiState.isWorkerStarted))
                },
                title = "Screen",
                desc = when(uiState.defaultSelectedWallpaperOption){
                    0 -> "Home screen"
                    1 -> "Lock screen"
                    2 -> "Home and lock screen"
                    else -> "Chose correct option"
                }
            ) {}
        }


    }

    if(uiState.shouldShowIntervalDialog){
        HourMinuteInputDialog(
            onIntervalChange = {
                onUiEvent(SettingViewModel.UiEvent.OnIntervalChange(it))
                               },
            defaultValue = uiState.defaultInterval,
            onCloseRequest = {onUiEvent(SettingViewModel.UiEvent.ShowIntervalDialog(false))}
        )
    }

    if (uiState.shouldShowWallpaperApplyOptionDialog){
        WallpaperApplyOption(
            selectedIndex = uiState.defaultSelectedWallpaperOption,
            onSelectionChange = {
                onUiEvent(SettingViewModel.UiEvent.OnWallpaperApplyOptionChange(it))
                onUiEvent(SettingViewModel.UiEvent.ShowWallpaperApplyOptionDialog(false))
            },
            onCloseRequest = {onUiEvent(SettingViewModel.UiEvent.ShowWallpaperApplyOptionDialog(false))}
        )
    }



}



@Composable
fun SettingItem(modifier: Modifier = Modifier, title:String, desc:String, controlContent: @Composable ()-> Unit) {
    Row(modifier = modifier.padding(start = 70.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)){
       Column(Modifier.weight(1f)) {
           Text(text = title, style = MaterialTheme.typography.titleMedium)
           Text(text = desc)
       }
        Box(Modifier.width(50.dp)){
            controlContent()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperApplyOption(selectedIndex:Int = 0, onSelectionChange:(Int)->Unit, onCloseRequest:()->Unit) {

    val options = arrayListOf(
        Option(
            IconSource(imageVector = Icons.Default.AddToHomeScreen),
            titleText = "Home screen"
        ),
        Option(
            IconSource(imageVector = Icons.Default.PhoneLocked),
            titleText = "Lock screen"
        ),
        Option(
            IconSource(imageVector = Icons.Default.AllInclusive),
            titleText = "Lock and Home screen"
        )
    )
    val selectedItem = options.get(selectedIndex)
    options[selectedIndex] = selectedItem.copy(selected =true )

    OptionDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { onCloseRequest() }),
        selection = OptionSelection.Single(options) { index, option ->
            onSelectionChange(index)
        },
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourMinuteInputDialog(
    onIntervalChange:(Long)->Unit,
    defaultValue:Long,
    onCloseRequest:UseCaseState.()->Unit
) {
    DurationDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest =onCloseRequest),
        selection = DurationSelection { newTimeInSeconds ->
            onIntervalChange(newTimeInSeconds)
        },
        header = Header.Default(title = "Interval"),
        config = DurationConfig(
            timeFormat = DurationFormat.HH_MM,
            currentTime = defaultValue,
            minTime = 1200,
            maxTime = 86400
        )
    )
}
