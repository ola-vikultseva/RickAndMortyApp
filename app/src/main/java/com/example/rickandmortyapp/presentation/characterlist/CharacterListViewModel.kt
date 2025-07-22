package com.example.rickandmortyapp.presentation.characterlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickandmortyapp.domain.CharacterRepository
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.filter.CharacterFilter
import com.example.rickandmortyapp.presentation.characterlist.model.CharacterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters: Flow<PagingData<CharacterUiModel>> = repository.getCharacters()
        .map { pagingData -> pagingData.map { it.toUiModel() } }
        .cachedIn(viewModelScope)

    private val _filterUiState = MutableStateFlow(repository.characterFilter.value)
    val filterUiState = _filterUiState.asStateFlow()

    fun onFilterChange(filter: CharacterFilter) {
        Log.d("Filter", "Current filter: $filter")
        _filterUiState.value = filter
    }

    fun resetFilter() {
        _filterUiState.value = CharacterFilter()
    }

    fun applyFilter() {
        repository.setFilter(_filterUiState.value)
    }

    private fun Character.toUiModel(): CharacterUiModel =
        CharacterUiModel(
            id = id,
            name = name,
            species = species,
            status = status,
            type = type,
            gender = gender,
            image = image
        )
}