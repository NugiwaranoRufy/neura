package com.app.neura.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataSource(
    private val context: Context
) {
    private val favoritePackIdsKey = stringSetPreferencesKey("favorite_pack_ids")
    private val favoriteChallengeIdsKey = stringSetPreferencesKey("favorite_challenge_ids")
    private val playLaterPackIdsKey = stringSetPreferencesKey("play_later_pack_ids")

    val favoritePackIds: Flow<Set<Long>> =
        context.userPrefsDataStore.data.map { prefs ->
            prefs[favoritePackIdsKey]
                ?.mapNotNull { it.toLongOrNull() }
                ?.toSet()
                ?: emptySet()
        }

    val favoriteChallengeIds: Flow<Set<Long>> =
        context.userPrefsDataStore.data.map { prefs ->
            prefs[favoriteChallengeIdsKey]
                ?.mapNotNull { it.toLongOrNull() }
                ?.toSet()
                ?: emptySet()
        }

    val playLaterPackIds: Flow<Set<Long>> =
        context.userPrefsDataStore.data.map { prefs ->
            prefs[playLaterPackIdsKey]
                ?.mapNotNull { it.toLongOrNull() }
                ?.toSet()
                ?: emptySet()
        }

    suspend fun toggleFavoritePack(localId: Long) {
        context.userPrefsDataStore.edit { prefs ->
            val current: MutableSet<String> =
                prefs[favoritePackIdsKey]?.toMutableSet() ?: mutableSetOf()

            val id = localId.toString()

            if (current.contains(id)) {
                current.remove(id)
            } else {
                current.add(id)
            }

            prefs[favoritePackIdsKey] = current
        }
    }

    suspend fun toggleFavoriteChallenge(challengeId: Long) {
        context.userPrefsDataStore.edit { prefs ->
            val current: MutableSet<String> =
                prefs[favoriteChallengeIdsKey]?.toMutableSet() ?: mutableSetOf()

            val id = challengeId.toString()

            if (current.contains(id)) {
                current.remove(id)
            } else {
                current.add(id)
            }

            prefs[favoriteChallengeIdsKey] = current
        }
    }

    suspend fun togglePlayLaterPack(localId: Long) {
        context.userPrefsDataStore.edit { prefs ->
            val current: MutableSet<String> =
                prefs[playLaterPackIdsKey]?.toMutableSet() ?: mutableSetOf()

            val id = localId.toString()

            if (current.contains(id)) {
                current.remove(id)
            } else {
                current.add(id)
            }

            prefs[playLaterPackIdsKey] = current
        }
    }
}