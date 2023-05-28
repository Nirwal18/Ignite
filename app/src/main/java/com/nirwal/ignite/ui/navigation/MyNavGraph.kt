package com.nirwal.ignite.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MyNavGraph(val title: String, val route:String, val icon:ImageVector? = null) {
    object BottomNavGraph:MyNavGraph(title = "Bottom navigation graph", route = "bottomNavRoot"){
        object WallpaperScreen:MyNavGraph(title = "WallPapers", route = "wallpapers", icon = Icons.Default.Wallpaper)
        object FavouriteScreen:MyNavGraph(title = "Favorites", route = "favorite", icon = Icons.Default.Favorite)
        object SettingScreen:MyNavGraph(title = "Settings", route = "settings", icon = Icons.Default.Settings)

    }
}