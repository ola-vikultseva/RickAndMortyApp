package com.example.rickandmortyapp.presentation.characterlist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterToolbar(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Characters") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
            }
        }
    )
}