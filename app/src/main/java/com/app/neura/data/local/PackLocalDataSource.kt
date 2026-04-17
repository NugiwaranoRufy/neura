package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.ChallengePack
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class PackLocalDataSource(
    private val context: Context
) {
    private val fileName = "challenge_packs.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getPacks(): List<ChallengePack> {
        return try {
            val file = getFile()

            if (!file.exists()) return emptyList()

            val content = file.readText()
            if (content.isBlank()) return emptyList()

            json.decodeFromString(
                ListSerializer(ChallengePack.serializer()),
                content
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun savePacks(packs: List<ChallengePack>) {
        val file = getFile()
        file.writeText(
            json.encodeToString(
                ListSerializer(ChallengePack.serializer()),
                packs
            )
        )
    }

    fun addPack(pack: ChallengePack) {
        val current = getPacks().toMutableList()
        current.add(pack)
        savePacks(current)
    }

    fun deletePack(createdAt: Long) {
        val updated = getPacks().filterNot { it.createdAt == createdAt }
        savePacks(updated)
    }
}