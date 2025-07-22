package com.example.rickandmortyapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyapp.data.db.entity.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE characterId = :id")
    suspend fun getRemoteKeysByCharacterId(id: Int): RemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}
