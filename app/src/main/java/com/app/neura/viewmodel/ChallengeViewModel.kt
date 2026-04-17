package com.app.neura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.app.neura.data.model.CreateChallengeForm
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

data class ChallengeUiState(
    val currentChallenge: Challenge? = null,
    val selectedOptionIndex: Int? = null,
    val hasAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val currentQuestionNumber: Int = 0,
    val totalQuestions: Int = 0,
    val score: Int = 0,
    val sessionCompleted: Boolean = false,
    val sessionType: ChallengeType? = null
)

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChallengeRepository(application.applicationContext)

    private var sessionChallenges: List<Challenge> = emptyList()
    private var currentIndex = 0
    private var currentScore = 0

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    private val exportJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    fun startSession(config: GameSessionConfig) {
        val all = repository.getChallengesByType(config.type).shuffled()
        sessionChallenges = all.take(config.totalQuestions)

        currentIndex = 0
        currentScore = 0

        _uiState.value = ChallengeUiState(
            currentChallenge = sessionChallenges.firstOrNull(),
            currentQuestionNumber = if (sessionChallenges.isNotEmpty()) 1 else 0,
            totalQuestions = sessionChallenges.size,
            score = 0,
            sessionCompleted = sessionChallenges.isEmpty(),
            sessionType = config.type
        )
    }

    fun selectOption(index: Int) {
        val current = _uiState.value.currentChallenge ?: return
        if (_uiState.value.hasAnswered) return

        val correct = index == current.correctIndex
        if (correct) currentScore++

        _uiState.value = _uiState.value.copy(
            selectedOptionIndex = index,
            hasAnswered = true,
            isCorrect = correct,
            score = currentScore
        )
    }

    fun nextChallenge() {
        if (sessionChallenges.isEmpty()) return

        if (currentIndex >= sessionChallenges.lastIndex) {
            _uiState.value = _uiState.value.copy(
                currentChallenge = null,
                sessionCompleted = true
            )
            return
        }

        currentIndex++

        _uiState.value = _uiState.value.copy(
            currentChallenge = sessionChallenges[currentIndex],
            selectedOptionIndex = null,
            hasAnswered = false,
            isCorrect = false,
            currentQuestionNumber = currentIndex + 1
        )
    }

    fun resetSession() {
        sessionChallenges = emptyList()
        currentIndex = 0
        currentScore = 0
        _uiState.value = ChallengeUiState()
    }

    fun createChallenge(form: CreateChallengeForm): Boolean {
        val options = listOf(
            form.option1.trim(),
            form.option2.trim(),
            form.option3.trim(),
            form.option4.trim()
        )

        val isValid = form.question.trim().isNotEmpty() &&
                options.all { it.isNotEmpty() } &&
                form.explanation.trim().isNotEmpty()

        if (!isValid) {
            return false
        }

        val allChallenges = repository.getAllChallenges()
        val nextId = (allChallenges.maxOfOrNull { it.id } ?: 0) + 1

        val challenge = Challenge(
            id = nextId,
            question = form.question.trim(),
            options = options,
            correctIndex = form.correctIndex,
            explanation = form.explanation.trim(),
            type = form.type,
            isUserCreated = true
        )

        repository.addUserChallenge(challenge)
        return true
    }

    fun getUserChallengeCount(): Int {
        return repository.getUserChallenges().size
    }

    fun getUserChallenges(): List<Challenge> {
        return repository.getUserChallenges().sortedByDescending { it.id }
    }

    fun deleteUserChallenge(challengeId: Int) {
        repository.deleteUserChallenge(challengeId)
    }

    fun exportUserChallengesToJson(): String {
        val userChallenges = repository.getUserChallenges()
        return exportJson.encodeToString(
            ListSerializer(Challenge.serializer()),
            userChallenges
        )
    }

    fun importUserChallengesFromJson(jsonContent: String): Boolean {
        return try {
            val imported = exportJson.decodeFromString(
                ListSerializer(Challenge.serializer()),
                jsonContent
            )
            repository.mergeUserChallenges(imported)
            true
        } catch (_: Exception) {
            false
        }
    }
}