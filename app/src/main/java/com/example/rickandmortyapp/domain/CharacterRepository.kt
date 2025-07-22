package com.example.rickandmortyapp.domain

import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.filter.CharacterFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CharacterRepository {
    val characterFilter: StateFlow<CharacterFilter>
    fun setFilter(filter: CharacterFilter)
    fun getCharacters(): Flow<PagingData<Character>>
}