package com.example.rickandmortyapp.domain.model.filter

data class CharacterFilter(
    val name: String? = null,
    val status: CharacterStatus? = null,
    val species: CharacterSpecies? = null,
    val type: CharacterType? = null,
    val gender: CharacterGender? = null
)

fun CharacterFilter.isEmpty(): Boolean =
    name == null && status == null && species == null && type == null && gender == null

fun CharacterFilter.toQueryMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    name?.let { map["name"] = it }
    status?.let { map["status"] = it.name.lowercase() }
    species?.let { map["species"] = it.name.lowercase() }
    type?.let { map["type"] = it.name.lowercase() }
    gender?.let { map["gender"] = it.name.lowercase() }
    return map
}
