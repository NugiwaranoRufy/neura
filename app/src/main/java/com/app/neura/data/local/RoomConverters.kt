package com.app.neura.data.local

import androidx.room.TypeConverter
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.EditorialStatus
import com.app.neura.data.model.VisibilityStatus

class RoomConverters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString("§")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split("§")
    }

    @TypeConverter
    fun fromChallengeType(value: ChallengeType): String = value.name

    @TypeConverter
    fun toChallengeType(value: String): ChallengeType = ChallengeType.valueOf(value)

    @TypeConverter
    fun fromChallengeDifficulty(value: ChallengeDifficulty): String = value.name

    @TypeConverter
    fun toChallengeDifficulty(value: String): ChallengeDifficulty =
        ChallengeDifficulty.valueOf(value)

    @TypeConverter
    fun fromEditorialStatus(value: EditorialStatus): String = value.name

    @TypeConverter
    fun toEditorialStatus(value: String): EditorialStatus =
        EditorialStatus.valueOf(value)

    @TypeConverter
    fun fromVisibilityStatus(value: VisibilityStatus): String = value.name

    @TypeConverter
    fun toVisibilityStatus(value: String): VisibilityStatus =
        VisibilityStatus.valueOf(value)
}