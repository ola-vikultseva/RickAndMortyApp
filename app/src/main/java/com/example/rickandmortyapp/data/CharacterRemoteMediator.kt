package com.example.rickandmortyapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmortyapp.data.db.RickAndMortyDatabase
import com.example.rickandmortyapp.data.db.entity.CharacterEntity
import com.example.rickandmortyapp.data.db.entity.RemoteKeys
import com.example.rickandmortyapp.data.remote.api.RickAndMortyApiService
import com.example.rickandmortyapp.data.remote.model.Character
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    private var failedOnce = false

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                val remoteKey = lastItem?.let { database.remoteKeysDao().getRemoteKeysByCharacterId(it.id) }
                remoteKey?.nextKey ?: return MediatorResult.Success(true)
            }
        }
        Log.d("Pagers", "RemoteMediator - load(), page: $page")
        return try {
            if (page == 6 && !failedOnce) {
                failedOnce = true
                throw Exception()
            }
            delay(3000)
            val response = apiService.getCharacters(page)
            val characters = response.results
            val endOfPaginationReached = characters.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.characterDao().clearAll()
                    database.remoteKeysDao().clearAll()
                }
                val keys = characters.map {
                    RemoteKeys(
                        characterId = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }
                database.characterDao().insertAll(characters.map { it.toEntity() })
                database.remoteKeysDao().insertAll(keys)
            }
            Log.d("Pagers", "RemoteMediator - result: MediatorResult.Success")
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("Pagers", "RemoteMediator - Exception in load(): ${e.message}")
            Log.d("Pagers", "RemoteMediator - result: MediatorResult.Error")
            MediatorResult.Error(e)
        }
    }

    private fun Character.toEntity(): CharacterEntity =
        CharacterEntity(
            id = id,
            name = name,
            species = species,
            status = status,
            type = type,
            gender = gender,
            image = image
        )
}