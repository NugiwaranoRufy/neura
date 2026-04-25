package com.app.neura.ui.util

import com.app.neura.data.model.GameSessionResult
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private fun GameSessionResult.completedDate(): LocalDate {
    return Instant.ofEpochMilli(completedAt)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun List<GameSessionResult>.dailyCompletedToday(): Boolean {
    val today = LocalDate.now()

    return any {
        it.source == "Daily challenge" && it.completedDate() == today
    }
}

fun List<GameSessionResult>.currentDailyStreak(): Int {
    val dailyDates = filter { it.source == "Daily challenge" }
        .map { it.completedDate() }
        .toSet()

    if (dailyDates.isEmpty()) return 0

    var streak = 0
    var date = LocalDate.now()

    while (dailyDates.contains(date)) {
        streak++
        date = date.minusDays(1)
    }

    return streak
}

fun List<GameSessionResult>.bestDailyStreak(): Int {
    val dates = filter { it.source == "Daily challenge" }
        .map { it.completedDate() }
        .toSet()
        .sorted()

    if (dates.isEmpty()) return 0

    var best = 1
    var current = 1

    for (i in 1 until dates.size) {
        val previous = dates[i - 1]
        val currentDate = dates[i]

        if (currentDate == previous.plusDays(1)) {
            current++
        } else {
            current = 1
        }

        if (current > best) best = current
    }

    return best
}