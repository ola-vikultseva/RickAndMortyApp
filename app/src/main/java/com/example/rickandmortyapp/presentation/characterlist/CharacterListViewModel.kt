package com.example.rickandmortyapp.presentation.characterlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.domain.CharacterRepository
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.CharacterFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters: Flow<PagingData<Character>> = repository.getCharacters().cachedIn(viewModelScope)

    // Holds both filters and search query (in 'name' field)
    private val _characterFilter = MutableStateFlow(CharacterFilter())
    val characterFilter = _characterFilter.asStateFlow()

    init {
        observeSearchQueryChanges()
    }

    fun onSearchQueryChange(query: String) {
        Log.d("CharacterFilter", "New search query: $query")
        val updatedFilter = _characterFilter.value.copy(name = query.takeIf { it.isNotEmpty() })
        Log.d("CharacterFilter", "New filter: $updatedFilter")
        _characterFilter.value = updatedFilter
    }

    fun onFilterChange(characterFilter: CharacterFilter) {
        Log.d("CharacterFilter", "New filter: $characterFilter")
        _characterFilter.value = characterFilter
    }

    fun onFilterApply() {
        repository.setCharacterFilter(_characterFilter.value)
    }

    fun onFilterReset() {
        val updatedFilter = _characterFilter.value.copy(
            status = null,
            species = null,
            type = null,
            gender = null
        )
        Log.d("CharacterFilter", "New filter: $updatedFilter")
        _characterFilter.value = updatedFilter
    }

    private fun observeSearchQueryChanges() {
        _characterFilter
            .map { it.name }
            .debounce(500)
            .distinctUntilChanged()
            .onEach {
                repository.setCharacterFilter(_characterFilter.value)
            }
            .launchIn(viewModelScope)
    }
}