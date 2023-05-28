package com.nirwal.ignite.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    searchHistory:List<String>,
    onFilterChange:(String)->Unit,
    onPhotoClick:(Photo)->Unit,
) {

    val screenType = rememberWindowInfo()
    val lazyGridState = rememberLazyGridState()
    val pOffset = remember { lazyGridState.firstVisibleItemScrollOffset }
    val direc = remember { derivedStateOf { lazyGridState.firstVisibleItemScrollOffset }}.value - pOffset
    val scrollDown /*or Down*/ = direc > 0 // Tad'aa

    Column {


        Box(Modifier.fillMaxSize()) {

            val paddinTop by animateDpAsState(
                targetValue = if (scrollDown) 0.dp else 80.dp,
                animationSpec = tween(durationMillis = 300)
            )

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
                        state = lazyGridState,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .padding(top = paddinTop),
                        columns = GridCells.Adaptive(adaptiveColumnWith),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ){

                        items(
                            count = photos.itemCount,
                            key = photos.itemKey(key = null),
                            contentType = photos.itemContentType<Photo>(
                            )
                        ) { index ->
                            val photo = photos[index]
                            WallpaperItem(
                                imageUrl = photo?.src?.medium.toString(),
                                //title = photo?.photographer.toString(),
                                imageHeightOffset = adaptiveColumnWith,
                                onClick = { onPhotoClick.invoke(photo!!) }
                            )
                        }
                        if(photos.itemCount<=0){
                            item {
                                Text(modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                                    text = "Unable to fetch image from server.\n" +
                                        "Make sure you are connected to Internet."
                                )
                            }
                        }
                    }

                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = lazyGridState.isScrollingUp(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                MySearchBar(history = searchHistory, onSearch = onFilterChange)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun WallpaperItemPreview() {
    WallpaperItem(imageUrl = "",10.dp,{})
}

@Composable
fun WallpaperItem(
    imageUrl:String,
    imageHeightOffset: Dp,
    onClick:()->Unit,

) {

    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build()

    AsyncImage(
        modifier = Modifier
            .height(90.dp + imageHeightOffset)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(4.dp))
        ,
        model = request,
        placeholder = painterResource(R.drawable.fire_skul),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
