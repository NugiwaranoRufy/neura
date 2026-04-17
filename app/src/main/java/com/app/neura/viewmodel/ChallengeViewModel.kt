package com.app.neura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.neura.data.model.Challenge
import com.app.neura.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChallengeUiState(
    val currentChallenge: Challenge? = null,
    val selectedOptionIndex: Int? = null,
    val hasAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val challengeIndex: Int = 0,
    val totalChallenges: Int = 0
)

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChallengeRepository(application.applicationContext)
    private val challenges = repository.getChallenges().shuffled()
    private var currentIndex = 0

    private val _uiState = MutableStateFlow(
        ChallengeUiState(
            currentChallenge = challenges.firstOrNull(),
            challengeIndex = 1,
            totalChallenges = challenges.size
        )
    )
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    fun selectOption(index: Int) {
        val current = _uiState.value.currentChallenge ?: return
        if (_uiState.value.hasAnswered) return

        _uiState.value = _uiState.value.copy(
            selectedOptionIndex = index,
            hasAnswered = true,
            isCorrect = index == current.correctIndex
        )
    }

    fun nextChallenge() {
        currentIndex = (currentIndex + 1) % challenges.size

        _uiState.value = ChallengeUiState(
            currentChallenge = challenges[currentIndex],
            challengeIndex = currentIndex + 1,
            totalChallenges = challenges.size
        )
    }
}