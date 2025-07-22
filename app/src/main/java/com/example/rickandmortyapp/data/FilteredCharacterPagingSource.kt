package com.example.rickandmortyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.remote.api.RickAndMortyApiService
import com.example.rickandmortyapp.data.remote.model.Character

class FilteredCharacterPagingSource(
    private val queryMap: Map<String, String>,
    private val apiService: RickAndMortyApiService
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        Log.d("Pagers", "PagingSource - load()")
        return try {
            val page = params.key ?: 1
            val response = apiService.getCharacters(page, queryMap)
            Log.d("Pagers", "PagingSource - result: LoadResult.Page")
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next != null) page + 1 else null
            )
        } catch (e: Exception) {
            Log.e("Pagers", "PagingSource - Exception in load(): ${e.message}")
            Log.d("Pagers", "PagingSource - result: LoadResult.Error")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int = 1
}