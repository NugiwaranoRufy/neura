package com.app.neura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.EditorialStatus
import com.app.neura.data.model.VisibilityStatus

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val type: ChallengeType,
    val isUserCreated: Boolean,
    val difficulty: ChallengeDifficulty,
    val createdAt: Long,
    val updatedAt: Long,
    val authorName: String,
    val tags: List<String>,
    val editorialStatus: EditorialStatus,
    val visibilityStatus: VisibilityStatus,
    val publishedAt: Long?
)