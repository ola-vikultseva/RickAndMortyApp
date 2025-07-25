package com.example.rickandmortyapp.presentation.characterlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.domain.CharacterRepository
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.CharacterFilter
import com.example.rickandmortyapp.domain.model.CharacterQueryParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters: Flow<PagingData<Character>> = repository.getCharacters().cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _characterFilter = MutableStateFlow(CharacterFilter())
    val characterFilter = _characterFilter.asStateFlow()

    init {
        observeSearchQueryChanges()
    }

    fun onSearchQueryChange(query: String) {
        Log.d("Test", "New search query: $query")
        _searchQuery.value = query
    }

    fun onFilterChange(characterFilter: CharacterFilter) {
        Log.d("Test", "New filter: $characterFilter")
        _characterFilter.value = characterFilter
    }

    fun onFilterApply() {
        val queryParams = CharacterQueryParams(
            searchQuery = searchQuery.value.takeIf { it.isNotEmpty() },
            characterFilter = characterFilter.value
        )
        repository.setQueryParams(queryParams)
    }

    fun onFilterReset() {
        _characterFilter.value = CharacterFilter()
    }

    private fun observeSearchQueryChanges() {
        viewModelScope.launch {
            searchQuery.debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val queryParams = CharacterQueryParams(
                        searchQuery = query.takeIf { it.isNotEmpty() },
                        characterFilter = characterFilter.value
                    )
                    repository.setQueryParams(queryParams)
                }
        }
    }
}