package com.app.neura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.neura.data.model.EditorialStatus
import com.app.neura.data.model.VisibilityStatus

@Entity(tableName = "packs")
data class PackEntity(
    @PrimaryKey val localId: Long,
    val title: String,
    val description: String,
    val authorName: String,
    val version: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val tags: List<String>,
    val editorialStatus: EditorialStatus,
    val visibilityStatus: VisibilityStatus,
    val publishedAt: Long?,
    val challengeIds: List<String>
)