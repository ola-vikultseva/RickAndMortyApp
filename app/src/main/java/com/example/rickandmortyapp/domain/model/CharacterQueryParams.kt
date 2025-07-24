package com.example.rickandmortyapp.domain.model

data class CharacterQueryParams(
    val searchQuery: String? = null,
    val characterFilter: CharacterFilter = CharacterFilter()
)

fun CharacterQueryParams.isEmpty(): Boolean = searchQuery == null && characterFilter.isEmpty()

fun CharacterQueryParams.toQueryMap(): Map<String, String> = buildMap {
    searchQuery?.let { put("name", it) }
    characterFilter.status?.let { put("status", it.name.lowercase()) }
    characterFilter.species?.let { put("species", it.name.lowercase()) }
    characterFilter.type?.let { put("type", it.name.lowercase()) }
    characterFilter.gender?.let { put("gender", it.name.lowercase()) }
}