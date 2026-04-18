package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.ChallengePack
import kotlinx.serialization.json.Json

class FeaturedPackDataSource(
    private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val featuredFiles = listOf(
        "featured_packs/logic_starter_pack.json",
        "featured_packs/lateral_pack.json"
    )

    fun getFeaturedPacks(): List<ChallengePack> {
        return featuredFiles.mapNotNull { path ->
            runCatching {
                val content = context.assets.open(path)
                    .bufferedReader()
                    .use { it.readText() }

                json.decodeFromString<ChallengePack>(content)
            }.getOrNull()
        }
    }
}