package com.nirwal.ignite.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchScreen() {
    Box {
        SearchScreen()
        Column() {
            
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(history:List<String>, onSearch:(String)->Unit) {
    var query by remember {
        mutableStateOf("")
    }
    var isActive by remember {
        mutableStateOf(false)
    }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        query = query,
        onQueryChange = {query=it},
        onSearch = {onSearch(it)
            isActive = false
        },
        active = isActive,
        onActiveChange = {isActive = it},
        leadingIcon = { IconButton(onClick = {
            onSearch(query)
            isActive =false
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search images")
        }
        },
        trailingIcon = {
            if(isActive){
                IconButton(onClick = {
                    if (query.isEmpty()) {
                        isActive = false
                    }else{
                        query = ""
                    }
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "clear search")
                }

            }
        },
        placeholder = { Text(text = "Search images")}

        ) 
    {
        history.forEach{
            Row(modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(start = 16.dp)
                .clickable {
                    query = it
                    isActive = false
                    onSearch(query)
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Icon(imageVector = Icons.Default.History, contentDescription = it)
                Text(text = it, style = MaterialTheme.typography.titleMedium)
            }
        }
            

    }
}
