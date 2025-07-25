package com.example.rickandmortyapp.domain

import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.CharacterFilter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun setCharacterFilter(filter: CharacterFilter)
    fun getCharacters(): Flow<PagingData<Character>>
}