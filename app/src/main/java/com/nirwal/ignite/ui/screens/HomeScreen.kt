package com.nirwal.ignite.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageFilterChipGroup(){}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageFilterChipGroup(onSelectionChange:(String)->Unit) {
    val filterList = listOf(
        "All",
        "Nature",
        "Landscape",
        "Space",
        "Technology"
    )

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterList.forEachIndexed{ index, filter ->
            FilterChip(
                selected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    onSelectionChange(filter)
                          },
                label =  { Text(filter) }
            )
        }

    }
}

