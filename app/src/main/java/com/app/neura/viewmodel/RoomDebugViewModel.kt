package com.app.neura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.neura.data.local.NeuraDatabase
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.repository.RoomCatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomDebugViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NeuraDatabase.getInstance(application)
    private val roomRepository = RoomCatalogRepository(database)

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> = _challenges

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _challenges.value = roomRepository.getAllChallenges()
        }
    }

    fun insertDebugChallenge() {
        viewModelScope.launch {
            val current = roomRepository.getAllChallenges()
            val nextId = (current.maxOfOrNull { it.id } ?: 0) + 1

            val challenge = Challenge(
                id = nextId,
                question = "Room debug challenge #$nextId",
                options = listOf("A", "B", "C", "D"),
                correctIndex = 0,
                explanation = "Inserted in Room only",
                type = ChallengeType.LOGIC,
                isUserCreated = true,
                difficulty = ChallengeDifficulty.EASY,
                authorName = "Room Debug",
                tags = listOf("room", "debug")
            )

            roomRepository.insertChallenge(challenge)
            refresh()
        }
    }

    fun deleteChallenge(id: Int) {
        viewModelScope.launch {
            roomRepository.deleteChallenge(id)
            refresh()
        }
    }
}