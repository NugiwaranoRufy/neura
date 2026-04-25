package com.app.neura.data.repository

import com.app.neura.data.local.NeuraDatabase
import com.app.neura.data.local.toDomain
import com.app.neura.data.local.toEntity
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack
import kotlinx.coroutines.flow.SharingStarted

class RoomCatalogRepository(
    private val database: NeuraDatabase
) {
    suspend fun getAllChallenges(): List<Challenge> {
        return database.challengeDao().getAll().map { it.toDomain() }
    }

    suspend fun getUserChallenges(): List<Challenge> {
        return database.challengeDao().getUserChallenges().map { it.toDomain() }
    }

    suspend fun insertChallenge(challenge: Challenge) {
        database.challengeDao().insert(challenge.toEntity())
    }

    suspend fun insertChallenges(challenges: List<Challenge>) {
        database.challengeDao().insertAll(challenges.map { it.toEntity() })
    }

    suspend fun deleteChallenge(id: Int) {
        database.challengeDao().deleteById(id)
    }

    suspend fun getAllPacks(): List<ChallengePack> {
        val challengeMap = database.challengeDao().getAll().associateBy { it.id }
        return database.packDao().getAll().map { packEntity ->
            val challenges = packEntity.challengeIds.mapNotNull { id ->
                challengeMap[id.toIntOrNull()]?.toDomain()
            }
            packEntity.toDomain(challenges)
        }
    }

    suspend fun insertPack(pack: ChallengePack) {
        database.packDao().insert(pack.toEntity())
    }

    suspend fun deletePack(localId: Long) {
        database.packDao().deleteByLocalId(localId)
    }
    suspend fun getChallengeById(id: Int): Challenge? {
        return database.challengeDao().getById(id)?.toDomain()
    }

    suspend fun replaceUserChallenges(challenges: List<Challenge>) {
        val existingUserChallenges = database.challengeDao().getUserChallenges()
        existingUserChallenges.forEach { database.challengeDao().deleteById(it.id) }
        database.challengeDao().insertAll(challenges.map { it.toEntity() })
    }
}