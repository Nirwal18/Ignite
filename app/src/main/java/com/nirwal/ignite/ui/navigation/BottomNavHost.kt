package com.nirwal.ignite.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.ui.viewModel.MainViewModel
import com.nirwal.ignite.ui.screens.FavouriteScreen
import com.nirwal.ignite.ui.screens.PhotoInfoScreen
import com.nirwal.ignite.ui.screens.SearchScreen
import com.nirwal.ignite.ui.screens.SettingScreen
import com.nirwal.ignite.ui.screens.WallPaperScreen
import com.nirwal.ignite.ui.viewModel.SettingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BottomNavHost(){
    val navController = rememberNavController()
    val vm: MainViewModel = koinViewModel()
    val settingVm:SettingViewModel = koinViewModel()
    val photos = vm.imageSource.collectAsLazyPagingItems()


    val bottomNavigationList = listOf(
        MyNavGraph.BottomNavGraph.WallpaperScreen,
        MyNavGraph.BottomNavGraph.FavouriteScreen,
        MyNavGraph.BottomNavGraph.SettingScreen
    )

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(modifier = Modifier.weight(1f), navController = navController, startDestination = MyNavGraph.BottomNavGraph.WallpaperScreen.route){
            composable(MyNavGraph.BottomNavGraph.WallpaperScreen.route){
                WallPaperScreen(
                    photos = photos,
                    searchHistory = vm.searchHistory.collectAsState(listOf()).value.map { it.query },
                    onFilterChange = vm::onSearch,
                    onPhotoClick = {
                                   navController.navigate("photo_info/{id}"
                                       .replace(
                                           oldValue = "{id}",
                                           newValue = it.id.toString()
                                       ))
                    }
                )
            }

            composable(MyNavGraph.BottomNavGraph.SearchScreen.route){
                SearchScreen()
            }

            composable(MyNavGraph.BottomNavGraph.FavouriteScreen .route){
                FavouriteScreen(
                    photos = vm.favoritePhotos.collectAsState(listOf()).value,
                    onBack = {navController.popBackStack()},
                    onPhotoClick = {
                        navController.navigate("photo_info/{id}"
                            .replace(
                                oldValue = "{id}",
                                newValue = it.toString()
                            ))
                    },
                    onFavouriteClick = vm::updateFavourites
                )
            }
            composable(MyNavGraph.BottomNavGraph.SettingScreen.route){
                SettingScreen(
                    uiState = settingVm.uiState.collectAsState().value,
                    onUiEvent = settingVm::onEvent,
                    onBack = {navController.popBackStack()}

                )
            }
            composable("photo_info/{id}", arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )){
                val id = it.arguments?.getInt("id")!!
                PhotoInfoScreen(
                    photo = vm.getPhoto(id).collectAsState(Photo()).value,
                    setWallPaper = vm::setWallpaper,
                    setLockScreen = vm::setLockScreen,
                    setFavourites = vm::updateFavourites
                )
            }

        }

        NavigationBar(modifier = Modifier.height(82.dp)) {
            var selectedItem by remember{ mutableStateOf(0) }
            bottomNavigationList.forEachIndexed{index, navItem ->
                NavigationBarItem(
                    selected = selectedItem==index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(navItem.route)
                              },
                    icon = { navItem.icon?.let { Icon(imageVector = it, contentDescription = navItem.title) } },
                    label = { Text(text = navItem.title)}
                )
            }
        }

    }


}