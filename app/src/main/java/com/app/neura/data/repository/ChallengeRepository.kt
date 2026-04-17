package com.app.neura.data.repository

import android.content.Context
import com.app.neura.data.local.AssetChallengeDataSource
import com.app.neura.data.local.UserChallengeLocalDataSource
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeType

class ChallengeRepository(
    context: Context
) {
    private val assetDataSource = AssetChallengeDataSource(context)
    private val userDataSource = UserChallengeLocalDataSource(context)

    fun getBuiltInChallenges(): List<Challenge> {
        return assetDataSource.loadChallenges()
    }

    fun getUserChallenges(): List<Challenge> {
        return userDataSource.getUserChallenges()
    }

    fun getAllChallenges(): List<Challenge> {
        return getBuiltInChallenges() + getUserChallenges()
    }

    fun getChallengesByType(type: ChallengeType): List<Challenge> {
        return getAllChallenges().filter { it.type == type }
    }

    fun addUserChallenge(challenge: Challenge) {
        userDataSource.addUserChallenge(challenge)
    }

    fun deleteUserChallenge(challengeId: Int) {
        userDataSource.deleteUserChallenge(challengeId)
    }

    fun replaceUserChallenges(challenges: List<Challenge>) {
        userDataSource.saveUserChallenges(challenges)
    }

    fun mergeUserChallenges(imported: List<Challenge>) {
        val current = getUserChallenges()
        val maxId = (getAllChallenges().maxOfOrNull { it.id } ?: 0)

        var nextId = maxId + 1

        val normalizedImported = imported.map { challenge ->
            challenge.copy(
                id = nextId++,
                isUserCreated = true
            )
        }

        userDataSource.saveUserChallenges(current + normalizedImported)
    }

    fun updateUserChallenge(challenge: Challenge) {
        userDataSource.updateUserChallenge(challenge)
    }
}