package com.nirwal.ignite.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nirwal.ignite.common.WindowInfo
import com.nirwal.ignite.common.rememberWindowInfo
import com.nirwal.ignite.domain.database.PhotoEntity
import com.nirwal.ignite.ui.navigation.MyNavGraph

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FavouriteScreenPreview() {
    FavouriteScreen(photos = listOf(), onBack = { /*TODO*/ }, onPhotoClick = {}, onFavouriteClick = {_,_->})
}


@Composable
fun FavouriteScreen(photos:List<PhotoEntity>, onBack:()->Unit, onPhotoClick:(Int)->Unit, onFavouriteClick:(PhotoEntity,Boolean)->Unit) {
    val screenType = rememberWindowInfo()
    Column(modifier = Modifier.fillMaxSize()) {
       Text(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
           text = "Favourites",
           style = MaterialTheme.typography.titleLarge
       )
        if(photos.isEmpty()){
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "No photos in favourites...",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }else {
            val adaptiveColumnWith = if(screenType.screenWidthInfo== WindowInfo.WindowType.Compact){
                130.dp
            }else{
                200.dp
            }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Adaptive(adaptiveColumnWith),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                itemsIndexed(photos) { index, photo ->
                    WallpaperItem(
                        imageUrl = photo.mediumUrl.toString(),
                        title = photo?.photographer.toString(),
                        onClick = { onPhotoClick(photo.remoteId!!) },
                       imageHeightoffset = adaptiveColumnWith,
                        isFavouritePhoto = true,
                        onFavouriteClick = {onFavouriteClick(photo,it)}
                    )
                }
            }

        }

    }
}
