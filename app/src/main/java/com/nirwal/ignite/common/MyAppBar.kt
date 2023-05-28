package com.nirwal.ignite.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun MyAppBarPreview() {
    MyAppBar(title = "Title", leadingIcon = Icons.Default.ArrowBack, trailingIcon = Icons.Default.MoreVert)
}

@Composable
fun MyAppBar(
    title:String,
    leadingIcon:ImageVector?=null,
    trailingIcon:ImageVector?=null,
    onLeadingIconClick:()->Unit = {},
    onTrailingIconClick:()->Unit = {},
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if(leadingIcon!=null) {
            IconButton(onClick = onLeadingIconClick) {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }
        }

        Text(
            modifier = Modifier.weight(1f).padding(start = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        if (trailingIcon!=null) {
            IconButton(onClick = onTrailingIconClick) {
                Icon(imageVector = trailingIcon, contentDescription = null)
            }
        }
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color.Black.copy(alpha = 0.2f))
    )
}