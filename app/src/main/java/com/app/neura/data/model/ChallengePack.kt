package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChallengePack(
    val title: String,
    val description: String,
    val authorName: String,
    val version: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val challenges: List<Challenge>,
    val tags: List<String> = emptyList(),
    val localId: Long = System.nanoTime(),
    val editorialStatus: EditorialStatus = EditorialStatus.DRAFT,
    val visibilityStatus: VisibilityStatus = VisibilityStatus.PRIVATE,
    val publishedAt: Long? = null
)