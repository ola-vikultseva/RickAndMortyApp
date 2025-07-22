package com.example.rickandmortyapp.presentation.utils

import com.example.rickandmortyapp.domain.model.filter.CharacterGender
import com.example.rickandmortyapp.domain.model.filter.CharacterSpecies
import com.example.rickandmortyapp.domain.model.filter.CharacterStatus
import com.example.rickandmortyapp.domain.model.filter.CharacterType

fun <T : Enum<T>> Class<T>.displayFilterTitle(): String = when (this) {
    CharacterGender::class.java -> "Gender"
    CharacterSpecies::class.java -> "Species"
    CharacterStatus::class.java -> "Status"
    CharacterType::class.java -> "Type"
    else -> "Title"
}

fun CharacterGender.displayName(): String = when (this) {
    CharacterGender.FEMALE -> "Female"
    CharacterGender.MALE -> "Male"
    CharacterGender.GENDERLESS -> "Genderless"
    CharacterGender.UNKNOWN -> "Unknown"
}

fun CharacterSpecies.displayName(): String = when (this) {
    CharacterSpecies.HUMAN -> "Human"
    CharacterSpecies.ALIEN -> "Alien"
    CharacterSpecies.ANIMAL -> "Animal"
    CharacterSpecies.ROBOT -> "Robot"
}

fun CharacterStatus.displayName(): String = when (this) {
    CharacterStatus.ALIVE -> "Alive"
    CharacterStatus.DEAD -> "Dead"
    CharacterStatus.UNKNOWN -> "Unknown"
}

fun CharacterType.displayName(): String = when (this) {
    CharacterType.GENETIC_EXPERIMENT -> "Genetic experiment"
    CharacterType.PARASITE -> "Parasite"
    CharacterType.SUPERHUMAN -> "Superhuman"
    CharacterType.VAMPIRE -> "Vampire"
}