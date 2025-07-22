package com.example.rickandmortyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyapp.data.db.RickAndMortyDatabase
import com.example.rickandmortyapp.data.db.entity.CharacterEntity
import com.example.rickandmortyapp.data.remote.api.RickAndMortyApiService
import com.example.rickandmortyapp.domain.CharacterRepository
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.filter.CharacterFilter
import com.example.rickandmortyapp.domain.model.filter.isEmpty
import com.example.rickandmortyapp.domain.model.filter.toQueryMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    private val _characterFilter = MutableStateFlow(CharacterFilter())
    override val characterFilter = _characterFilter.asStateFlow()

    override fun setFilter(filter: CharacterFilter) {
        _characterFilter.value = filter
    }

    override fun getCharacters(): Flow<PagingData<Character>> = characterFilter.flatMapLatest { filter ->
        if (filter.isEmpty()) {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                remoteMediator = CharacterRemoteMediator(apiService, database),
                pagingSourceFactory = { database.characterDao().getCharactersPagingSource() }
            ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
        } else {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    FilteredCharacterPagingSource(
                        queryMap = filter.toQueryMap(),
                        apiService = apiService
                    )
                }
            ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
        }
    }

    private fun CharacterEntity.toDomain(): Character =
        Character(
            id = id,
            name = name,
            species = species,
            status = status,
            type = type,
            gender = gender,
            image = image
        )

    private fun com.example.rickandmortyapp.data.remote.model.Character.toDomain(): Character =
        Character(
            id = id,
            name = name,
            species = species,
            status = status,
            type = type,
            gender = gender,
            image = image
        )
}