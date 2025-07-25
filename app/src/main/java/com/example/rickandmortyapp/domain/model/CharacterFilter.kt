package com.example.rickandmortyapp.domain.model

data class CharacterFilter(
    /** Search query by character name (used as 'name' filter in API) */
    val name: String? = null,
    val status: CharacterStatus? = null,
    val species: CharacterSpecies? = null,
    val type: CharacterType? = null,
    val gender: CharacterGender? = null
)

fun CharacterFilter.isEmpty(): Boolean = listOf(name, status, species, type, gender).all { it == null }

enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN
}

enum class CharacterSpecies {
    HUMAN,
    ALIEN,
    ANIMAL,
    ROBOT
}

enum class CharacterType {
    GENETIC_EXPERIMENT,
    PARASITE,
    SUPERHUMAN,
    VAMPIRE
}

enum class CharacterGender {
    FEMALE,
    MALE,
    GENDERLESS,
    UNKNOWN
}