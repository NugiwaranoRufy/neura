package com.app.neura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.neura.data.local.entity.PackEntity

@Dao
interface PackDao {

    @Query("SELECT * FROM packs ORDER BY createdAt DESC")
    suspend fun getAll(): List<PackEntity>

    @Query("SELECT * FROM packs WHERE localId = :localId LIMIT 1")
    suspend fun getByLocalId(localId: Long): PackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pack: PackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(packs: List<PackEntity>)

    @Query("DELETE FROM packs WHERE localId = :localId")
    suspend fun deleteByLocalId(localId: Long)

    @Query("DELETE FROM packs")
    suspend fun deleteAll()
}