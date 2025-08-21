package com.example.rickandmortyapp.presentation.characterlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rickandmortyapp.presentation.characterlist.components.CharacterItem
import com.example.rickandmortyapp.presentation.characterlist.components.FilterModalBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characterFilter by viewModel.characterFilter.collectAsState()
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val refreshState = characters.loadState.refresh
    val appendState = characters.loadState.append

    LaunchedEffect(refreshState) {
        Log.d("UiState", "Refresh state changed → $refreshState")
    }
    LaunchedEffect(appendState) {
        Log.d("UiState", "Append state changed → $appendState")
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus(true)
            }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            value = characterFilter.name ?: "",
            onValueChange = viewModel::onSearchQueryChange,
            placeholder = { Text("Enter a name") },
            leadingIcon = {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            },
            trailingIcon = {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    IconButton(
                        onClick = { scope.launch { sheetState.show() } }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        when (characters.loadState.refresh) {
            LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Something went wrong. Please try again later.")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { characters.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                if (characters.itemCount == 0 && characters.loadState.append.endOfPaginationReached) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No matching items found. Try adjusting your search or filters.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
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
                        when (characters.loadState.append) {
                            LoadState.Loading -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(Modifier.height(16.dp))
                                        CircularProgressIndicator()
                                        Spacer(Modifier.height(16.dp))
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(Modifier.height(16.dp))
                                        Text("Something went wrong. Please try again later.")
                                        Spacer(Modifier.height(8.dp))
                                        Button(onClick = { characters.retry() }) {
                                            Text("Retry")
                                        }
                                        Spacer(Modifier.height(16.dp))
                                    }
                                }
                            }
                            else -> Unit
                        }
                    }
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
                    characterFilter = characterFilter,
                    onFilterChange = viewModel::onFilterChange,
                    onApplyFilters = {
                        scope.launch {
                            viewModel.onFilterApply()
                            sheetState.hide()
                        }
                    },
                    onResetFilters = viewModel::onFilterReset,
                    sheetState = sheetState
                )
            }
        }
    }
}