package com.nirwal.ignite.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nirwal.ignite.R
import com.nirwal.ignite.common.WindowInfo
import com.nirwal.ignite.common.rememberWindowInfo
import com.nirwal.ignite.domain.database.PhotoEntity
import com.nirwal.ignite.domain.model.Photo


@Composable
fun WallPaperScreen(
    photos: LazyPagingItems<Photo>,
    onFilterChange:(String)->Unit,
    onPhotoClick:(Photo)->Unit,
    onFavouriteClick: (PhotoEntity,Boolean) -> Unit
) {

    val screenType = rememberWindowInfo()


    Column {
        ImageFilterChipGroup(onSelectionChange = onFilterChange)

        Box(Modifier.fillMaxSize()) {
            when (photos.loadState.refresh) {
                is LoadState.Error -> {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Error loading data")
                }
                LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp))
                }
                is LoadState.NotLoading -> {

                    val adaptiveColumnWith = if(screenType.screenWidthInfo==WindowInfo.WindowType.Compact){
                        130.dp
                    }else{
                        200.dp
                    }
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        columns = GridCells.Adaptive(adaptiveColumnWith),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ){

                        items(
                            count = photos.itemCount,
                            key = photos.itemKey<Photo>(key = null),
                            contentType = photos.itemContentType<Photo>(
                            )
                        ) { index ->
                            val photo = photos[index]
                            WallpaperItem(
                                imageUrl = photo?.src?.medium.toString(),
                                title = photo?.photographer.toString(),
                                imageHeightoffset = adaptiveColumnWith,
                                onClick = { onPhotoClick.invoke(photo!!) },
                                onFavouriteClick = {
                                    onFavouriteClick(photo!!.toPhotoEntity(),it)
                                }
                            )
                        }
                        if(photos.itemCount<=0){
                            item {
                                Text(modifier = Modifier.fillMaxWidth()
                                    .align(Alignment.Center),
                                    text = "Unable to fetch image from server.\n" +
                                        "Make sure you are connected to Internet."
                                )
                            }
                        }
                    }

                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun WallpaperItemPreview() {
    WallpaperItem(imageUrl = "", title = "Akshay",10.dp,false,{},{b->})
}

@Composable
fun WallpaperItem(
    imageUrl:String,
    title: String,
    imageHeightoffset: Dp,
    isFavouritePhoto:Boolean = false,
    onClick:()->Unit,
    onFavouriteClick:(Boolean)->Unit
) {
    var iSFavourite by remember {
        mutableStateOf(isFavouritePhoto)
    }

    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build()

    Box {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .height(90.dp + imageHeightoffset)
                .clickable(onClick = onClick)
                .clip(RoundedCornerShape(4.dp))
            ,
            model = request,
            placeholder = painterResource(R.drawable.fire_skul),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp),
            onClick = {
                iSFavourite = !iSFavourite
                onFavouriteClick(iSFavourite)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = title,
                tint = if(iSFavourite) Color.Green else Color.White
            )
        }
    }
}


