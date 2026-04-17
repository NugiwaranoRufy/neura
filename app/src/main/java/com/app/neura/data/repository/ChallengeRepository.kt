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
}