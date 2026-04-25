package com.app.neura.ui.util

import com.app.neura.data.model.ChallengePack

fun ChallengePack.averageDifficultyText(): String {
    if (challenges.isEmpty()) return "N/A"

    val average = challenges.map { it.difficulty.ordinal }.average()

    return when {
        average < 0.75 -> "Easy"
        average < 1.5 -> "Medium"
        else -> "Hard"
    }
}

fun ChallengePack.estimatedTimeText(): String {
    val minutes = when {
        challenges.isEmpty() -> 0
        challenges.size <= 3 -> 3
        challenges.size <= 5 -> 5
        challenges.size <= 10 -> 10
        else -> 15
    }

    return if (minutes == 0) "N/A" else "$minutes min"
}

fun ChallengePack.mainCategoryText(): String {
    return when {
        tags.any { it.equals("logic", ignoreCase = true) } -> "Logic"
        tags.any { it.equals("lateral", ignoreCase = true) } -> "Lateral"
        tags.any { it.equals("starter", ignoreCase = true) } -> "Starter"
        tags.any { it.equals("creative", ignoreCase = true) } -> "Creative"
        else -> "Featured"
    }
}

fun ChallengePack.categoryBadgeText(): String {
    return when (mainCategoryText()) {
        "Logic" -> "🧠 Logic"
        "Lateral" -> "🎯 Lateral"
        "Starter" -> "🌱 Starter"
        "Creative" -> "✨ Creative"
        else -> "★ Featured"
    }
}