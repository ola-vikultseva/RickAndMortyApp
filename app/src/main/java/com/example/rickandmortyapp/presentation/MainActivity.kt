package com.example.rickandmortyapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapp.presentation.characterlist.CHARACTER_LIST_ROUTE
import com.example.rickandmortyapp.presentation.characterlist.characterListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = CHARACTER_LIST_ROUTE
            ) {
                characterListScreen()
            }
        }
    }
}