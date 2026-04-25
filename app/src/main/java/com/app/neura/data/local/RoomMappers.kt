package com.app.neura.data.local

import com.app.neura.data.local.entity.ChallengeEntity
import com.app.neura.data.local.entity.PackEntity
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack

fun ChallengeEntity.toDomain(): Challenge {
    return Challenge(
        id = id,
        question = question,
        options = options,
        correctIndex = correctIndex,
        explanation = explanation,
        type = type,
        isUserCreated = isUserCreated,
        difficulty = difficulty,
        createdAt = createdAt,
        updatedAt = updatedAt,
        authorName = authorName,
        tags = tags,
        editorialStatus = editorialStatus,
        visibilityStatus = visibilityStatus,
        publishedAt = publishedAt
    )
}

fun Challenge.toEntity(): ChallengeEntity {
    return ChallengeEntity(
        id = id,
        question = question,
        options = options,
        correctIndex = correctIndex,
        explanation = explanation,
        type = type,
        isUserCreated = isUserCreated,
        difficulty = difficulty,
        createdAt = createdAt,
        updatedAt = updatedAt,
        authorName = authorName,
        tags = tags,
        editorialStatus = editorialStatus,
        visibilityStatus = visibilityStatus,
        publishedAt = publishedAt
    )
}

fun PackEntity.toDomain(challenges: List<Challenge>): ChallengePack {
    return ChallengePack(
        title = title,
        description = description,
        authorName = authorName,
        version = version,
        createdAt = createdAt,
        updatedAt = updatedAt,
        challenges = challenges,
        tags = tags,
        localId = localId,
        editorialStatus = editorialStatus,
        visibilityStatus = visibilityStatus,
        publishedAt = publishedAt
    )
}

fun ChallengePack.toEntity(): PackEntity {
    return PackEntity(
        localId = localId,
        title = title,
        description = description,
        authorName = authorName,
        version = version,
        createdAt = createdAt,
        updatedAt = updatedAt,
        tags = tags,
        editorialStatus = editorialStatus,
        visibilityStatus = visibilityStatus,
        publishedAt = publishedAt,
        challengeIds = challenges.map { it.id.toString() }
    )
}