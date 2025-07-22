package com.example.rickandmortyapp.domain.model

data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val gender: String,
    val image: String
)
