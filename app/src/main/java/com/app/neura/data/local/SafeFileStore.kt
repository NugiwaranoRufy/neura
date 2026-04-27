package com.app.neura.data.local

import java.io.File

object SafeFileStore {
    fun readTextOrNull(file: File): String? {
        return try {
            if (!file.exists()) return null
            val content = file.readText()
            if (content.isBlank()) null else content
        } catch (_: Exception) {
            null
        }
    }

    fun writeTextWithBackup(file: File, content: String) {
        if (file.exists()) {
            val backupFile = File(file.parentFile, "${file.name}.bak")
            runCatching {
                file.copyTo(backupFile, overwrite = true)
            }
        }

        file.writeText(content)
    }
}