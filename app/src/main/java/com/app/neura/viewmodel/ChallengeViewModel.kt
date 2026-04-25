package com.app.neura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.app.neura.data.model.CreateChallengeForm
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import com.app.neura.data.model.ChallengeDifficulty
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.app.neura.data.model.UserProfile
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.SessionAnswer

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
    var sessionAnswers by mutableStateOf<List<SessionAnswer>>(emptyList())
        private set
    private var currentIndex = 0
    private var currentScore = 0
    private var sessionResultSaved = false
    private var roomUserChallengesCache by mutableStateOf<List<Challenge>>(emptyList())
    var sessionHistory by mutableStateOf<List<GameSessionResult>>(emptyList())
        private set
    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()
    val favoritePackIds = repository.getFavoritePackIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val favoriteChallengeIds = repository.getFavoriteChallengeIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val playLaterPackIds = repository.getPlayLaterPackIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())
    private val exportJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    val userProfile = repository.getUserProfile()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UserProfile()
        )
    fun startSession(config: GameSessionConfig) {
        val allPlayableChallenges = (
                roomUserChallengesCache +
                        repository.getFeaturedPacks().flatMap { it.challenges } +
                        repository.getPacks().flatMap { it.challenges }
                )
            .filter { it.type == config.type }
            .distinctBy { it.id }
            .shuffled()

        sessionChallenges = allPlayableChallenges.take(config.totalQuestions)

        currentIndex = 0
        currentScore = 0
        sessionResultSaved = false
        sessionAnswers = emptyList()

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

        sessionAnswers = sessionAnswers
            .filterNot { it.challenge.id == current.id }
            .plus(
                SessionAnswer(
                    challenge = current,
                    selectedOptionIndex = index,
                    isCorrect = correct
                )
            )

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
            val completedState = _uiState.value.copy(
                currentChallenge = null,
                sessionCompleted = true
            )

            _uiState.value = completedState

            repository.addSessionResult(
                GameSessionResult(
                    type = completedState.sessionType,
                    score = completedState.score,
                    totalQuestions = completedState.totalQuestions
                )
            )
            refreshSessionHistory()

            return
        }
    }

    fun continueSession(): Boolean {
        if (sessionChallenges.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                currentChallenge = null,
                sessionCompleted = true
            )
            return true
        }

        val nextIndex = currentIndex + 1

        if (nextIndex >= sessionChallenges.size) {
            _uiState.value = _uiState.value.copy(
                currentChallenge = null,
                selectedOptionIndex = null,
                hasAnswered = false,
                isCorrect = false,
                sessionCompleted = true
            )

            saveCurrentSessionResultIfNeeded()

            return true
        }

        currentIndex = nextIndex

        _uiState.value = _uiState.value.copy(
            currentChallenge = sessionChallenges[currentIndex],
            selectedOptionIndex = null,
            hasAnswered = false,
            isCorrect = false,
            currentQuestionNumber = currentIndex + 1,
            sessionCompleted = false
        )

        return false
    }

    fun resetSession() {
        sessionChallenges = emptyList()
        currentIndex = 0
        currentScore = 0
        sessionResultSaved = false
        sessionAnswers = emptyList()
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
                form.explanation.trim().isNotEmpty() &&
                form.authorName.trim().isNotEmpty()

        if (!isValid) {
            return false
        }

        val nextId = (roomUserChallengesCache.maxOfOrNull { it.id } ?: 0) + 1

        val challenge = Challenge(
            id = nextId,
            question = form.question.trim(),
            options = options,
            correctIndex = form.correctIndex,
            explanation = form.explanation.trim(),
            type = form.type,
            isUserCreated = true,
            difficulty = form.difficulty,
            authorName = if (form.authorName.trim().isNotEmpty()) {
                form.authorName.trim()
            } else {
                userProfile.value.displayName
            },
            tags = form.tags
        )

        viewModelScope.launch {
            repository.insertUserChallengeToRoom(challenge)
            refreshRoomUserChallenges()
        }
        return true
    }

    fun getUserChallengeCount(): Int {
        return roomUserChallengesCache.size
    }

    fun getUserChallenges(): List<Challenge> {
        return roomUserChallengesCache.sortedByDescending { it.createdAt }
    }

    fun deleteUserChallenge(challengeId: Int) {
        viewModelScope.launch {
            repository.deleteUserChallengeFromRoom(challengeId)
            refreshRoomUserChallenges()
        }
    }

    fun exportUserChallengesToJson(): String {
        val userChallenges = roomUserChallengesCache
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
            viewModelScope.launch {
                repository.replaceUserChallengesInRoom(imported)
                refreshRoomUserChallenges()
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getUserChallengeById(challengeId: Int): Challenge? {
        return roomUserChallengesCache.firstOrNull { it.id == challengeId }
    }

    fun updateChallenge(challengeId: Int, form: CreateChallengeForm): Boolean {
        val existing = getUserChallengeById(challengeId) ?: return false

        val options = listOf(
            form.option1.trim(),
            form.option2.trim(),
            form.option3.trim(),
            form.option4.trim()
        )

        val isValid = form.question.trim().isNotEmpty() &&
                options.all { it.isNotEmpty() } &&
                form.explanation.trim().isNotEmpty() &&
                form.authorName.trim().isNotEmpty()

        if (!isValid) {
            return false
        }

        val updated = existing.copy(
            question = form.question.trim(),
            options = options,
            correctIndex = form.correctIndex,
            explanation = form.explanation.trim(),
            type = form.type,
            difficulty = form.difficulty,
            authorName = if (form.authorName.trim().isNotEmpty()) {
                form.authorName.trim()
            } else {
                userProfile.value.displayName
            },
            tags = form.tags
        )

        viewModelScope.launch {
            repository.insertUserChallengeToRoom(updated)
            refreshRoomUserChallenges()
        }
        return true
    }

    fun exportChallengePackToJson(
        title: String,
        description: String,
        authorName: String,
        challengeIds: List<Int>,
        tags: List<String>
    ): String? {
        val selectedChallenges = roomUserChallengesCache
            .filter { it.id in challengeIds }

        if (
            title.trim().isEmpty() ||
            description.trim().isEmpty() ||
            authorName.trim().isEmpty() ||
            selectedChallenges.isEmpty()
        ) {
            return null
        }

        val pack = ChallengePack(
            title = title.trim(),
            description = description.trim(),
            authorName = if (authorName.trim().isNotEmpty()) {
                authorName.trim()
            } else {
                userProfile.value.displayName
            },
            challenges = selectedChallenges,
            tags = tags
        )

        return exportJson.encodeToString(
            ChallengePack.serializer(),
            pack
        )
    }

    fun previewChallengePack(jsonContent: String): ChallengePack? {
        return try {
            exportJson.decodeFromString(
                ChallengePack.serializer(),
                jsonContent
            )
        } catch (_: Exception) {
            null
        }
    }

    fun importChallengePack(pack: ChallengePack): Boolean {
        return try {
            viewModelScope.launch {
                repository.replaceUserChallengesInRoom(
                    (roomUserChallengesCache + pack.challenges).distinctBy { it.id }
                )
                refreshRoomUserChallenges()
            }
            repository.addPack(pack)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun savePackToLibrary(pack: ChallengePack) {
        repository.addPack(pack)
    }

    fun getPacks(): List<ChallengePack> {
        return repository.getPacks().sortedByDescending { it.createdAt }
    }

    fun getPackByCreatedAt(createdAt: Long): ChallengePack? {
        return repository.getPacks().firstOrNull { it.createdAt == createdAt }
    }

    fun getPackByLocalId(localId: Long): ChallengePack? {
        return repository.getPacks().firstOrNull { it.localId == localId }
    }

    fun deletePack(localId: Long) {
        repository.deletePack(localId)
    }

    fun startSessionFromPack(pack: ChallengePack, totalQuestions: Int? = null) {
        val all = pack.challenges.shuffled()
        sessionChallenges = if (totalQuestions != null) {
            all.take(totalQuestions)
        } else {
            all
        }

        currentIndex = 0
        currentScore = 0
        sessionResultSaved = false
        sessionAnswers = emptyList()

        _uiState.value = ChallengeUiState(
            currentChallenge = sessionChallenges.firstOrNull(),
            currentQuestionNumber = if (sessionChallenges.isNotEmpty()) 1 else 0,
            totalQuestions = sessionChallenges.size,
            score = 0,
            sessionCompleted = sessionChallenges.isEmpty(),
            sessionType = sessionChallenges.firstOrNull()?.type
        )
    }

    fun getFeaturedPacks(): List<ChallengePack> {
        return repository.getFeaturedPacks()
    }

    fun getFeaturedPackByCreatedAt(createdAt: Long): ChallengePack? {
        return repository.getFeaturedPacks().firstOrNull { it.createdAt == createdAt }
    }

    fun importFeaturedPack(pack: ChallengePack): Boolean {
        return try {
            repository.addPack(pack)
            viewModelScope.launch {
                repository.replaceUserChallengesInRoom(
                    (roomUserChallengesCache + pack.challenges).distinctBy { it.id }
                )
                refreshRoomUserChallenges()
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun toggleFavoritePack(localId: Long) {
        viewModelScope.launch {
            repository.toggleFavoritePack(localId)
        }
    }

    fun toggleFavoriteChallenge(challengeId: Int) {
        viewModelScope.launch {
            repository.toggleFavoriteChallenge(challengeId.toLong())
        }
    }

    fun togglePlayLaterPack(localId: Long) {
        viewModelScope.launch {
            repository.togglePlayLaterPack(localId)
        }
    }

    fun getFavoritePacks(): List<ChallengePack> {
        val ids = favoritePackIds.value
        return repository.getPacks().filter { it.localId in ids }
    }

    fun getPlayLaterPacks(): List<ChallengePack> {
        val ids = playLaterPackIds.value
        return repository.getPacks().filter { it.localId in ids }
    }

    fun saveUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun getCreatedChallengesCount(): Int {
        return roomUserChallengesCache.size
    }

    fun getSavedPacksCount(): Int {
        return repository.getPacks().size
    }
    fun getChallengesByAuthor(authorName: String): List<Challenge> {
        return roomUserChallengesCache
            .filter { it.authorName.equals(authorName, ignoreCase = true) }
            .sortedByDescending { it.createdAt }
    }

    fun getPacksByAuthor(authorName: String): List<ChallengePack> {
        val localPacks = repository.getPacks()
        val featuredPacks = repository.getFeaturedPacks()

        return (localPacks + featuredPacks)
            .filter { it.authorName.equals(authorName, ignoreCase = true) }
            .distinctBy { "${it.title}_${it.authorName}_${it.createdAt}" }
            .sortedByDescending { it.createdAt }
    }

    fun isLocalProfileAuthor(authorName: String): Boolean {
        return userProfile.value.displayName.equals(authorName, ignoreCase = true)
    }

    fun initializeUserChallengesRoomIfNeeded() {
        viewModelScope.launch {
            repository.seedUserChallengesToRoomIfEmpty()
            refreshRoomUserChallenges()
        }
    }

    private suspend fun refreshRoomUserChallenges() {
        roomUserChallengesCache = repository.getUserChallengesFromRoom()
    }

    fun publishChallenge(challengeId: Int) {
        val existing = roomUserChallengesCache.firstOrNull { it.id == challengeId } ?: return

        val updated = existing.copy(
            editorialStatus = com.app.neura.data.model.EditorialStatus.PUBLISHED,
            visibilityStatus = com.app.neura.data.model.VisibilityStatus.PUBLIC_READY,
            publishedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertUserChallengeToRoom(updated)
            refreshRoomUserChallenges()
        }
    }

    fun moveChallengeToDraft(challengeId: Int) {
        val existing = roomUserChallengesCache.firstOrNull { it.id == challengeId } ?: return

        val updated = existing.copy(
            editorialStatus = com.app.neura.data.model.EditorialStatus.DRAFT,
            visibilityStatus = com.app.neura.data.model.VisibilityStatus.PRIVATE,
            publishedAt = null,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertUserChallengeToRoom(updated)
            refreshRoomUserChallenges()
        }
    }

    fun getDraftChallenges(): List<Challenge> {
        return roomUserChallengesCache
            .filter { it.editorialStatus == com.app.neura.data.model.EditorialStatus.DRAFT }
            .sortedByDescending { it.createdAt }
    }

    fun getPublishedChallenges(): List<Challenge> {
        return roomUserChallengesCache
            .filter { it.editorialStatus == com.app.neura.data.model.EditorialStatus.PUBLISHED }
            .sortedByDescending { it.createdAt }
    }

    fun isPackAlreadyImported(pack: ChallengePack): Boolean {
        return repository.getPacks().any {
            it.title == pack.title && it.authorName == pack.authorName
        }
    }

    fun refreshSessionHistory() {
        sessionHistory = repository.getSessionHistory()
    }

    fun getBestScoreText(): String {
        val best = sessionHistory.maxByOrNull { result ->
            if (result.totalQuestions == 0) 0.0 else result.score.toDouble() / result.totalQuestions
        } ?: return "N/A"

        return "${best.score}/${best.totalQuestions}"
    }

    fun getAverageScoreText(): String {
        if (sessionHistory.isEmpty()) return "N/A"

        val average = sessionHistory.map {
            if (it.totalQuestions == 0) 0.0 else it.score.toDouble() / it.totalQuestions
        }.average()

        return "${(average * 100).toInt()}%"
    }

    fun clearSessionHistory() {
        repository.clearSessionHistory()
        refreshSessionHistory()
    }
    private fun saveCurrentSessionResultIfNeeded() {
        if (sessionResultSaved) return

        val state = _uiState.value

        if (state.totalQuestions <= 0) return

        repository.addSessionResult(
            GameSessionResult(
                type = state.sessionType,
                score = state.score,
                totalQuestions = state.totalQuestions
            )
        )

        sessionResultSaved = true
        refreshSessionHistory()
    }


}