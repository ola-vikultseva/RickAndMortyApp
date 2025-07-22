package com.example.rickandmortyapp.presentation.characterlist.model

data class CharacterUiModel(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val gender: String,
    val image: String
)
