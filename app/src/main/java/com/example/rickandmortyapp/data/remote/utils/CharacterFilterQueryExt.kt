package com.example.rickandmortyapp.data.remote.utils

import com.example.rickandmortyapp.domain.model.CharacterFilter

fun CharacterFilter.toQueryMap(): Map<String, String> = buildMap {
    name?.let { put("name", it) }
    status?.let { put("status", it.name.lowercase()) }
    species?.let { put("species", it.name.lowercase()) }
    type?.let { put("type", it.name.lowercase()) }
    gender?.let { put("gender", it.name.lowercase()) }
}