package com.example.rickandmortyapp.presentation.characterlist

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
    private val _pendingFilter = MutableStateFlow(CharacterFilter())
    val pendingFilter = _pendingFilter.asStateFlow()

    private val appliedFilter = MutableStateFlow(CharacterFilter())

    init {
        observeSearchQueryChanges()
    }

    fun onSearchQueryChange(query: String) {
        _pendingFilter.value = _pendingFilter.value.copy(name = query.takeIf { it.isNotEmpty() })
        appliedFilter.value = appliedFilter.value.copy(name = query.takeIf { it.isNotEmpty() })
    }

    fun onFilterChange(characterFilter: CharacterFilter) {
        _pendingFilter.value = characterFilter
    }

    fun onFilterApply() {
        appliedFilter.value = _pendingFilter.value
        repository.setCharacterFilter(appliedFilter.value)
    }

    fun onFilterReset() {
        _pendingFilter.value = _pendingFilter.value.copy(
            status = null,
            species = null,
            type = null,
            gender = null
        )
    }

    fun resetPendingToApplied() {
        _pendingFilter.value = appliedFilter.value
    }

    private fun observeSearchQueryChanges() {
        appliedFilter
            .map { it.name }
            .debounce(500)
            .distinctUntilChanged()
            .onEach {
                repository.setCharacterFilter(appliedFilter.value)
            }
            .launchIn(viewModelScope)
    }
}