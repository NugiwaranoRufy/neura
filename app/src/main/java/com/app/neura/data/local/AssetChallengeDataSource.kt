package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.Challenge
import kotlinx.serialization.json.Json

class AssetChallengeDataSource(
    private val context: Context
) {

    fun loadChallenges(): List<Challenge> {
        val jsonString = context.assets
            .open("challenges.json")
            .bufferedReader()
            .use { it.readText() }

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString(jsonString)
    }
}