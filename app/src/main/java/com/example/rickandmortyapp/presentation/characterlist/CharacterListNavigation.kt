package com.example.rickandmortyapp.presentation.characterlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.characterListScreen() {
    composable(CHARACTER_LIST_ROUTE) {
        CharacterListScreen()
    }
}

const val CHARACTER_LIST_ROUTE = "character_list"