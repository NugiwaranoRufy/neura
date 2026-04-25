package com.app.neura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.neura.data.local.entity.ChallengeEntity

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenges ORDER BY createdAt DESC")
    suspend fun getAll(): List<ChallengeEntity>

    @Query("SELECT * FROM challenges WHERE isUserCreated = 1 ORDER BY createdAt DESC")
    suspend fun getUserChallenges(): List<ChallengeEntity>

    @Query("SELECT * FROM challenges WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(challenges: List<ChallengeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(challenge: ChallengeEntity)

    @Query("DELETE FROM challenges WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM challenges")
    suspend fun deleteAll()
}