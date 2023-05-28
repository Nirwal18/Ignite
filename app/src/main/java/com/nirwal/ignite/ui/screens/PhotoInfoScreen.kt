package com.nirwal.ignite.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nirwal.ignite.R
import com.nirwal.ignite.domain.model.Photo


@Composable
fun PhotoInfoScreen(photo:Photo, setWallPaper:(String)->Unit, setLockScreen:(String)->Unit) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(photo.src?.original)
        .crossfade(true)
        .build()

    var isLoading by remember {
        mutableStateOf(true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(.5f),
            model = request,
            placeholder = painterResource(R.drawable.fire_skul),
            contentDescription = null,
            onSuccess = {isLoading=false},
            contentScale = ContentScale.FillBounds
        )
        if(isLoading){
            LinearProgressIndicator(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
        }

        if (photo.photographer != null) {
            Text(
                text = photo.alt.toString(),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Resolution : ${photo.width.toString()} X ${photo.height.toString()}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Photo by ${photo.photographer.toString()}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { setWallPaper(photo.src?.original!!)}) {
                Text(text = "Set Wallpaper")
            }
            OutlinedButton(onClick = {setLockScreen(photo.src?.original!!)}) {
                Text(text = "Set Lock Screen")
            }

        }
    }
}