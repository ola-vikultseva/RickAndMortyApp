package com.example.rickandmortyapp.presentation.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rickandmortyapp.presentation.characterlist.components.CharacterItem
import com.example.rickandmortyapp.presentation.characterlist.components.CharacterToolbar
import com.example.rickandmortyapp.presentation.characterlist.components.FilterModalBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val filterUiState by viewModel.filterUiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CharacterToolbar(
                onSearchClick = { },
                onFilterClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(characters.itemCount) { index ->
                    val character = characters[index]
                    if (character != null) {
                        CharacterItem(character) { }
                    }
                }
            }
            if (sheetState.isVisible) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        scope.launch {
                            sheetState.hide()
                        }
                    }
                ) {
                    FilterModalBottomSheet(
                        filterUiState = filterUiState,
                        onFilterChange = viewModel::onFilterChange,
                        onApplyFilters = {
                            scope.launch {
                                viewModel.applyFilter()
                                sheetState.hide()
                            }
                        },
                        onResetFilters = viewModel::resetFilter,
                        sheetState = sheetState
                    )
                }
            }
        }
    }
}