package com.app.neura.data.repository

import android.content.Context
import com.app.neura.data.local.AssetChallengeDataSource
import com.app.neura.data.local.UserChallengeLocalDataSource
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.local.PackLocalDataSource
import com.app.neura.data.model.ChallengePack
import com.app.neura.data.local.FeaturedPackDataSource
import com.app.neura.data.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import com.app.neura.data.model.UserProfile

class ChallengeRepository(
    context: Context
) {
    private val assetDataSource = AssetChallengeDataSource(context)
    private val userDataSource = UserChallengeLocalDataSource(context)

    private val packDataSource = PackLocalDataSource(context)
    private val featuredPackDataSource = FeaturedPackDataSource(context)
    private val userPreferencesDataSource = UserPreferencesDataSource(context)

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

    fun getPacks(): List<ChallengePack> {
        return packDataSource.getPacks()
    }

    fun addPack(pack: ChallengePack) {
        packDataSource.addPack(pack)
    }

    fun deletePack(localId: Long) {
        packDataSource.deletePack(localId)
    }

    fun getFeaturedPacks(): List<ChallengePack> {
        return featuredPackDataSource.getFeaturedPacks()
    }

    fun getFavoritePackIds(): Flow<Set<Long>> {
        return userPreferencesDataSource.favoritePackIds
    }

    fun getFavoriteChallengeIds(): Flow<Set<Long>> {
        return userPreferencesDataSource.favoriteChallengeIds
    }

    fun getPlayLaterPackIds(): Flow<Set<Long>> {
        return userPreferencesDataSource.playLaterPackIds
    }

    suspend fun toggleFavoritePack(localId: Long) {
        userPreferencesDataSource.toggleFavoritePack(localId)
    }

    suspend fun toggleFavoriteChallenge(challengeId: Long) {
        userPreferencesDataSource.toggleFavoriteChallenge(challengeId)
    }

    suspend fun togglePlayLaterPack(localId: Long) {
        userPreferencesDataSource.togglePlayLaterPack(localId)
    }
    fun getUserProfile(): Flow<UserProfile> {
        return userPreferencesDataSource.userProfile
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userPreferencesDataSource.saveUserProfile(profile)
    }

}