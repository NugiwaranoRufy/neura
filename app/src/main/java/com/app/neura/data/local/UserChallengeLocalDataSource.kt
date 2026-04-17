package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.Challenge
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class UserChallengeLocalDataSource(
    private val context: Context
) {
    private val fileName = "user_challenges.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getUserChallenges(): List<Challenge> {
        val file = getFile()

        if (!file.exists()) {
            return emptyList()
        }

        val content = file.readText()
        if (content.isBlank()) {
            return emptyList()
        }

        return json.decodeFromString(
            ListSerializer(Challenge.serializer()),
            content
        )
    }

    fun saveUserChallenges(challenges: List<Challenge>) {
        val file = getFile()
        file.writeText(
            json.encodeToString(
                ListSerializer(Challenge.serializer()),
                challenges
            )
        )
    }

    fun addUserChallenge(challenge: Challenge) {
        val current = getUserChallenges().toMutableList()
        current.add(challenge)
        saveUserChallenges(current)
    }

    fun deleteUserChallenge(challengeId: Int) {
        val updated = getUserChallenges().filterNot { it.id == challengeId }
        saveUserChallenges(updated)
    }

    fun updateUserChallenge(updatedChallenge: Challenge) {
        val updatedList = getUserChallenges().map { challenge ->
            if (challenge.id == updatedChallenge.id) updatedChallenge else challenge
        }
        saveUserChallenges(updatedList)
    }
}