package com.app.neura.data.repository

import android.content.Context
import com.app.neura.data.local.NeuraDatabase
import com.app.neura.data.local.PackLocalDataSource
import com.app.neura.data.local.UserChallengeLocalDataSource
import com.app.neura.data.local.UserPreferencesDataSource
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import com.app.neura.data.local.FeaturedPackDataSource
import com.app.neura.data.local.SessionHistoryLocalDataSource
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.local.AccessibilitySettingsDataSource
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.data.local.AchievementProgressDataSource
import com.app.neura.data.model.AchievementProgress

class ChallengeRepository(
    context: Context
) {
    private val challengeDataSource = UserChallengeLocalDataSource(context)
    private val packDataSource = PackLocalDataSource(context)
    private val userPreferencesDataSource = UserPreferencesDataSource(context)
    private val database = NeuraDatabase.getInstance(context)
    private val roomRepository = RoomCatalogRepository(database)
    private val featuredPackDataSource = FeaturedPackDataSource(context)
    private val sessionHistoryDataSource = SessionHistoryLocalDataSource(context)
    private val accessibilitySettingsDataSource = AccessibilitySettingsDataSource(context)
    private val achievementProgressDataSource = AchievementProgressDataSource(context)

    // Legacy user challenges only
    fun getUserChallenges(): List<Challenge> {
        return challengeDataSource.getUserChallenges()
    }

    fun getChallengesByType(type: ChallengeType): List<Challenge> {
        return challengeDataSource.getUserChallenges().filter { it.type == type }
    }

    fun mergeUserChallenges(challenges: List<Challenge>) {
        challengeDataSource.saveUserChallenges(challenges)
    }

    fun addUserChallenge(challenge: Challenge) {
        challengeDataSource.addUserChallenge(challenge)
    }

    fun updateUserChallenge(challenge: Challenge) {
        challengeDataSource.updateUserChallenge(challenge)
    }

    fun deleteUserChallenge(challengeId: Int) {
        challengeDataSource.deleteUserChallenge(challengeId)
    }

    // Legacy packs only
    fun getPacks(): List<ChallengePack> {
        return packDataSource.getPacks()
    }

    fun addPack(pack: ChallengePack) {
        val exists = getPacks().any {
            it.title == pack.title && it.authorName == pack.authorName
        }
        if (!exists) {
            packDataSource.addPack(pack)
        }
    }

    fun deletePack(localId: Long) {
        packDataSource.deletePack(localId)
    }

    // Preferences / profile
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

    fun isRoomSeedCompleted(): Flow<Boolean> {
        return userPreferencesDataSource.roomSeedCompleted
    }

    suspend fun markRoomSeedCompleted() {
        userPreferencesDataSource.markRoomSeedCompleted()
    }

    // Room bridge for user-created challenges
    suspend fun getUserChallengesFromRoom(): List<Challenge> {
        return roomRepository.getUserChallenges()
    }

    suspend fun getAllRoomChallenges(): List<Challenge> {
        return roomRepository.getAllChallenges()
    }

    suspend fun insertUserChallengeToRoom(challenge: Challenge) {
        roomRepository.insertChallenge(challenge)
    }

    suspend fun replaceUserChallengesInRoom(challenges: List<Challenge>) {
        roomRepository.replaceUserChallenges(challenges)
    }

    suspend fun deleteUserChallengeFromRoom(challengeId: Int) {
        roomRepository.deleteChallenge(challengeId)
    }

    suspend fun seedUserChallengesToRoomIfEmpty() {
        val roomChallenges = roomRepository.getUserChallenges()
        if (roomChallenges.isNotEmpty()) return

        val legacyUserChallenges = challengeDataSource.getUserChallenges()
        if (legacyUserChallenges.isNotEmpty()) {
            roomRepository.insertChallenges(legacyUserChallenges)
        }
    }
    fun getAllChallenges(): List<Challenge> {
        return challengeDataSource.getUserChallenges()
    }

    fun getFeaturedPacks(): List<ChallengePack> {
        return featuredPackDataSource.getFeaturedPacks()
    }

    fun getSessionHistory(): List<GameSessionResult> {
        return sessionHistoryDataSource.getSessions()
    }

    fun addSessionResult(result: GameSessionResult) {
        sessionHistoryDataSource.addSession(result)
    }

    fun clearSessionHistory() {
        sessionHistoryDataSource.clearSessions()
    }

    fun getAccessibilitySettings(): AccessibilitySettings {
        return accessibilitySettingsDataSource.getSettings()
    }

    fun saveAccessibilitySettings(settings: AccessibilitySettings) {
        accessibilitySettingsDataSource.saveSettings(settings)
    }

    fun getAchievementProgress(): AchievementProgress {
        return achievementProgressDataSource.getProgress()
    }

    fun recordAchievementSession(
        result: GameSessionResult,
        currentDailyStreak: Int
    ) {
        achievementProgressDataSource.recordSession(
            result = result,
            currentDailyStreak = currentDailyStreak
        )
    }
}