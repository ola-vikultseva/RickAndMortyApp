package com.example.rickandmortyapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmortyapp.data.db.RickAndMortyDatabase.Companion.DATABASE_VERSION
import com.example.rickandmortyapp.data.db.dao.CharacterDao
import com.example.rickandmortyapp.data.db.dao.RemoteKeysDao
import com.example.rickandmortyapp.data.db.entity.CharacterEntity
import com.example.rickandmortyapp.data.db.entity.RemoteKeys

@Database(
    entities = [CharacterEntity::class, RemoteKeys::class],
    version = DATABASE_VERSION
)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private const val DATABASE_NAME = "rick_and_morty_database.db"
        const val DATABASE_VERSION = 1

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RickAndMortyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}