package com.app.neura.data.security

import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack

object ImportSecurityValidator {
    private const val MAX_TEXT_LENGTH = 1_000
    private const val MAX_EXPLANATION_LENGTH = 2_000
    private const val MAX_OPTION_LENGTH = 300
    private const val MAX_TAGS = 12
    private const val MAX_TAG_LENGTH = 40
    private const val MAX_CHALLENGES_PER_IMPORT = 100
    private const val MAX_PACK_TITLE_LENGTH = 120
    private const val MAX_PACK_DESCRIPTION_LENGTH = 1_000
    private const val MAX_AUTHOR_LENGTH = 80

    fun isValidChallenge(challenge: Challenge): Boolean {
        if (challenge.id <= 0) return false
        if (challenge.question.isBlank() || challenge.question.length > MAX_TEXT_LENGTH) return false
        if (challenge.explanation.isBlank() || challenge.explanation.length > MAX_EXPLANATION_LENGTH) return false
        if (challenge.options.size != 4) return false
        if (challenge.correctIndex !in challenge.options.indices) return false
        if (challenge.options.any { it.isBlank() || it.length > MAX_OPTION_LENGTH }) return false
        if (challenge.authorName.isBlank() || challenge.authorName.length > MAX_AUTHOR_LENGTH) return false
        if (challenge.tags.size > MAX_TAGS) return false
        if (challenge.tags.any { it.isBlank() || it.length > MAX_TAG_LENGTH }) return false

        return true
    }

    fun sanitizeChallenges(challenges: List<Challenge>): List<Challenge> {
        return challenges
            .take(MAX_CHALLENGES_PER_IMPORT)
            .filter { isValidChallenge(it) }
            .distinctBy { it.id }
    }

    fun isValidPack(pack: ChallengePack): Boolean {
        if (pack.title.isBlank() || pack.title.length > MAX_PACK_TITLE_LENGTH) return false
        if (pack.description.isBlank() || pack.description.length > MAX_PACK_DESCRIPTION_LENGTH) return false
        if (pack.authorName.isBlank() || pack.authorName.length > MAX_AUTHOR_LENGTH) return false
        if (pack.tags.size > MAX_TAGS) return false
        if (pack.tags.any { it.isBlank() || it.length > MAX_TAG_LENGTH }) return false
        if (pack.challenges.isEmpty()) return false
        if (pack.challenges.size > MAX_CHALLENGES_PER_IMPORT) return false
        if (pack.challenges.any { !isValidChallenge(it) }) return false

        return true
    }
}