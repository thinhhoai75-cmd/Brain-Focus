package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.FocusReminderEntity
import com.example.data.db.FocusTipEntity
import com.example.data.db.StudySessionEntity
import com.example.data.db.UserProfileEntity
import com.example.data.model.ArticleData
import com.example.data.model.AssessmentData
import com.example.data.model.FbsDiagnosis
import com.example.data.model.LeaderboardUser
import com.example.data.model.PopcornArticle
import com.example.data.model.SimulatedLeaderboard
import com.example.data.repository.FocusRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.util.LofiAudioPlayer
import com.example.util.LofiSoundMode

import com.example.data.repository.SessionCompletionResult

enum class TimerState {
    IDLE, RUNNING, PAUSED, COMPLETED
}

class FocusViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repository = FocusRepository(
        db.userDao(),
        db.studySessionDao(),
        db.focusTipDao(),
        db.focusReminderDao()
    )

    // --- Reminders Notice Banner ---
    private val _reminderMessage = MutableStateFlow<String?>(null)
    val reminderMessage: StateFlow<String?> = _reminderMessage.asStateFlow()

    val allReminders: StateFlow<List<FocusReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Audio Player Instance ---
    private val audioPlayer = LofiAudioPlayer()

    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private val _soundMode = MutableStateFlow(LofiSoundMode.LOFI_CHILL)
    val soundMode: StateFlow<LofiSoundMode> = _soundMode.asStateFlow()

    private val _audioVolume = MutableStateFlow(0.6f)
    val audioVolume: StateFlow<Float> = _audioVolume.asStateFlow()

    private val _autoPlayLofiOnStart = MutableStateFlow(true)
    val autoPlayLofiOnStart: StateFlow<Boolean> = _autoPlayLofiOnStart.asStateFlow()

    // --- Distraction Penalty Warning ---
    private val _lastPenaltyMessage = MutableStateFlow<String?>(null)
    val lastPenaltyMessage: StateFlow<String?> = _lastPenaltyMessage.asStateFlow()

    // --- Brain Game Rewards Message ---
    private val _lastGameRewardMessage = MutableStateFlow<String?>(null)
    val lastGameRewardMessage: StateFlow<String?> = _lastGameRewardMessage.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allSessions: StateFlow<List<StudySessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTips: StateFlow<List<FocusTipEntity>> = repository.allTips
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val articlesList: List<PopcornArticle> = ArticleData.articles

    // --- Onboarding / Assessment State ---
    private val _assessmentAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val assessmentAnswers: StateFlow<Map<Int, Int>> = _assessmentAnswers.asStateFlow()

    private val _onboardingName = MutableStateFlow("")
    val onboardingName: StateFlow<String> = _onboardingName.asStateFlow()

    private val _onboardingEmail = MutableStateFlow("")
    val onboardingEmail: StateFlow<String> = _onboardingEmail.asStateFlow()

    private val _assessmentDiagnosis = MutableStateFlow<FbsDiagnosis?>(null)
    val assessmentDiagnosis: StateFlow<FbsDiagnosis?> = _assessmentDiagnosis.asStateFlow()

    private val _assessmentScore = MutableStateFlow<Int?>(null)
    val assessmentScore: StateFlow<Int?> = _assessmentScore.asStateFlow()

    private val _isRetakingAssessment = MutableStateFlow(false)
    val isRetakingAssessment: StateFlow<Boolean> = _isRetakingAssessment.asStateFlow()

    // --- Focus Session Timer State ---
    private val _selectedMinutes = MutableStateFlow(25) // 15, 25, 45, 60, 90
    val selectedMinutes: StateFlow<Int> = _selectedMinutes.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(25 * 60)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _exitCount = MutableStateFlow(0)
    val exitCount: StateFlow<Int> = _exitCount.asStateFlow()

    private val _wasAppPausedDuringSession = MutableStateFlow(false)
    val wasAppPausedDuringSession: StateFlow<Boolean> = _wasAppPausedDuringSession.asStateFlow()

    private val _lastSessionCompletionReward = MutableStateFlow<SessionCompletionResult?>(null) // SessionCompletionResult(points, fbsBoost, streak)
    val lastSessionCompletionReward: StateFlow<SessionCompletionResult?> = _lastSessionCompletionReward.asStateFlow()

    private var timerJob: Job? = null

    // --- Leaderboard State ---
    val leaderboardList: StateFlow<List<LeaderboardUser>> = combine(
        userProfile,
        allSessions
    ) { user, _ ->
        val name = user?.name ?: "Bạn"
        val points = user?.rankingPoints ?: 1000
        val fbs = user?.fbsScore ?: 500
        val sessionsCount = user?.completedSessionsCount ?: 0
        SimulatedLeaderboard.buildMergedLeaderboard(name, points, fbs, sessionsCount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeDefaultTipsIfNeeded()
            repository.initializeDefaultRemindersIfNeeded()
        }
    }

    // --- Assessment Actions ---
    fun setOnboardingName(name: String) {
        _onboardingName.value = name
    }

    fun setOnboardingEmail(email: String) {
        _onboardingEmail.value = email
    }

    fun selectAnswer(questionIndex: Int, optionIndex: Int) {
        val currentMap = _assessmentAnswers.value.toMutableMap()
        currentMap[questionIndex] = optionIndex
        _assessmentAnswers.value = currentMap
    }

    fun calculateAndSubmitAssessment() {
        val answersMap = _assessmentAnswers.value
        val optionIndexList = (0 until AssessmentData.questions.size).map { idx ->
            answersMap[idx] ?: 0
        }
        val calculatedScore = AssessmentData.calculateFbsScore(optionIndexList)
        val diagnosis = AssessmentData.getFbsDiagnosis(calculatedScore)

        _assessmentScore.value = calculatedScore
        _assessmentDiagnosis.value = diagnosis
    }

    fun completeAssessmentAndSave() {
        val calculatedScore = _assessmentScore.value ?: 500
        viewModelScope.launch {
            repository.saveUserProfile(
                name = _onboardingName.value.ifBlank { userProfile.value?.name ?: "Học viên Focus" },
                email = _onboardingEmail.value.ifBlank { userProfile.value?.email ?: "focus@brain.app" },
                initialFbs = calculatedScore
            )
            _isRetakingAssessment.value = false
        }
    }

    fun retakeAssessment() {
        _assessmentAnswers.value = emptyMap()
        _assessmentScore.value = null
        _assessmentDiagnosis.value = null
        userProfile.value?.let { profile ->
            _onboardingName.value = profile.name
            _onboardingEmail.value = profile.email
        }
        _isRetakingAssessment.value = true
    }

    fun finishRetakingAssessment() {
        _isRetakingAssessment.value = false
    }

    // --- Audio Player Controls ---
    fun toggleAudioPlaying() {
        if (_isAudioPlaying.value) {
            audioPlayer.stopSound()
            _isAudioPlaying.value = false
        } else {
            audioPlayer.startSound(_soundMode.value)
            _isAudioPlaying.value = true
        }
    }

    fun selectSoundMode(mode: LofiSoundMode) {
        _soundMode.value = mode
        if (_isAudioPlaying.value) {
            audioPlayer.startSound(mode)
        }
    }

    fun setAudioVolume(volume: Float) {
        _audioVolume.value = volume
        audioPlayer.setVolume(volume)
    }

    fun toggleAutoPlayLofi() {
        _autoPlayLofiOnStart.value = !_autoPlayLofiOnStart.value
    }

    // --- Profile Actions ---
    fun updateProfileInfo(newName: String, newAvatarIcon: String) {
        viewModelScope.launch {
            repository.updateUserProfileInfo(newName, newAvatarIcon)
        }
    }

    // --- Distraction Penalty Action ---
    fun triggerDistractionPenalty(appOrReason: String) {
        _exitCount.value = _exitCount.value + 1
        _wasAppPausedDuringSession.value = true
        if (_timerState.value == TimerState.RUNNING) {
            timerJob?.cancel()
            _timerState.value = TimerState.PAUSED
        }
        viewModelScope.launch {
            val (pointsDeducted, fbsDeducted) = repository.applyDistractionPenalty(appOrReason)
            _lastPenaltyMessage.value = "⚠️ Xao nhãng bởi $appOrReason! Bị trừ $pointsDeducted điểm xếp hạng và $fbsDeducted BFS."
        }
    }

    fun dismissPenaltyMessage() {
        _lastPenaltyMessage.value = null
    }

    // --- Brain Game Actions ---
    fun completeBrainGame(gameName: String, gameScore: Int, bonusPoints: Int, bonusFbs: Int) {
        viewModelScope.launch {
            repository.recordGameReward(gameName, bonusPoints, bonusFbs)
            _lastGameRewardMessage.value = "🎉 Xuất sắc! Hoàn thành $gameName ($gameScore đ). Nhận +$bonusPoints Đ xếp hạng & +$bonusFbs BFS!"
        }
    }

    fun dismissGameRewardMessage() {
        _lastGameRewardMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stopSound()
    }

    // --- Timer Actions ---
    fun selectDuration(minutes: Int) {
        if (_timerState.value == TimerState.IDLE) {
            _selectedMinutes.value = minutes
            _remainingSeconds.value = minutes * 60
        }
    }

    fun startSession() {
        if (_remainingSeconds.value <= 0) {
            _remainingSeconds.value = _selectedMinutes.value * 60
        }
        _timerState.value = TimerState.RUNNING
        _wasAppPausedDuringSession.value = false
        _exitCount.value = 0

        if (_autoPlayLofiOnStart.value && !_isAudioPlaying.value) {
            audioPlayer.startSound(_soundMode.value)
            _isAudioPlaying.value = true
        }

        runTimerLoop()
    }

    fun pauseSession() {
        if (_timerState.value == TimerState.RUNNING) {
            timerJob?.cancel()
            _timerState.value = TimerState.PAUSED
        }
    }

    fun resumeSession() {
        _timerState.value = TimerState.RUNNING
        runTimerLoop()
    }

    fun resetSession() {
        timerJob?.cancel()
        _timerState.value = TimerState.IDLE
        _remainingSeconds.value = _selectedMinutes.value * 60
        _exitCount.value = 0
        _wasAppPausedDuringSession.value = false
    }

    private fun runTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _timerState.value == TimerState.RUNNING) {
                delay(1000L)
                _remainingSeconds.value = _remainingSeconds.value - 1
            }
            if (_remainingSeconds.value <= 0 && _timerState.value == TimerState.RUNNING) {
                onSessionCompleted()
            }
        }
    }

    private suspend fun onSessionCompleted() {
        _timerState.value = TimerState.COMPLETED
        val targetMins = _selectedMinutes.value
        val actualSecs = targetMins * 60 - _remainingSeconds.value
        val exits = _exitCount.value

        val reward = repository.completeStudySession(
            targetMinutes = targetMins,
            actualSecondsCompleted = actualSecs,
            exitCount = exits
        )
        _lastSessionCompletionReward.value = reward
    }

    fun dismissCompletionDialog() {
        _lastSessionCompletionReward.value = null
        resetSession()
    }

    fun submitSessionFeedback(
        emotion: String?,
        reflectionNote: String?
    ) {
        val reward = _lastSessionCompletionReward.value
        val sessionId = reward?.sessionId ?: 0L
        viewModelScope.launch {
            if (sessionId > 0) {
                val (bonusPts, bonusFbs) = repository.saveSessionReflection(sessionId, emotion, reflectionNote)
                if (bonusPts > 0) {
                    _lastGameRewardMessage.value = "🌟 Tuyệt vời! Bạn nhận thêm +$bonusPts điểm & +$bonusFbs BFS nhờ đúc kết tiến bộ bản thân!"
                }
            }
            _lastSessionCompletionReward.value = null
            resetSession()
        }
    }

    // Called when App goes to Background during active running or paused timer
    fun onAppPaused() {
        if (_timerState.value == TimerState.RUNNING || _timerState.value == TimerState.PAUSED) {
            timerJob?.cancel()
            _timerState.value = TimerState.PAUSED
            _wasAppPausedDuringSession.value = true
            _exitCount.value = _exitCount.value + 1
            viewModelScope.launch {
                val (pointsDeducted, fbsDeducted) = repository.applyDistractionPenalty("Thoát ứng dụng / Mở app giải trí")
                _lastPenaltyMessage.value = "⚠️ Phát hiện thoát app hoặc chuyển sang ứng dụng khác khi chưa xong phiên học! Bị trừ $pointsDeducted điểm xếp hạng và $fbsDeducted BFS."
            }
        }
    }

    fun dismissPauseWarning() {
        _wasAppPausedDuringSession.value = false
    }

    // --- Tips Actions ---
    fun addNewCustomTip(title: String, category: String, description: String) {
        viewModelScope.launch {
            repository.addNewTip(title, category, description)
        }
    }

    fun toggleDailyReminder() {
        viewModelScope.launch {
            repository.toggleDailyReminder()
        }
    }

    fun updateDailyReminderTime(time: String) {
        viewModelScope.launch {
            repository.updateDailyReminderTime(time)
        }
    }

    fun updateGithubUrl(url: String) {
        viewModelScope.launch {
            repository.updateGithubUrl(url)
        }
    }

    fun getTodayDateString(): String = repository.getTodayDateString()

    fun getYesterdayDateString(): String = repository.getYesterdayDateString()

    fun toggleFavoriteTip(tip: FocusTipEntity) {
        viewModelScope.launch {
            repository.toggleFavoriteTip(tip)
        }
    }

    fun toggleAppliedTip(tip: FocusTipEntity) {
        viewModelScope.launch {
            repository.toggleAppliedTip(tip)
        }
    }

    // --- Custom Reminders Actions ---
    fun addReminder(title: String, category: String, time: String, frequency: String) {
        viewModelScope.launch {
            repository.addReminder(title, category, time, frequency)
            _reminderMessage.value = "⏰ Đã tạo lời nhắc: \"$title\" lúc $time ($frequency)"
        }
    }

    fun updateReminder(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
            _reminderMessage.value = "✏️ Đã cập nhật lời nhắc \"${reminder.title}\" lúc ${reminder.time}"
        }
    }

    fun deleteReminder(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            _reminderMessage.value = "🗑️ Đã xóa lời nhắc \"${reminder.title}\""
        }
    }

    fun toggleReminderEnabled(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            val updatedState = !reminder.isEnabled
            repository.toggleReminderEnabled(reminder.id, updatedState)
            val stateText = if (updatedState) "Đã bật" else "Đã tắt"
            _reminderMessage.value = "🔔 $stateText lời nhắc \"${reminder.title}\" (${reminder.time})"
        }
    }

    fun triggerReminderAlert(reminder: FocusReminderEntity) {
        _reminderMessage.value = "🔔 LỜI NHẮC ĐẾN GIỜ: ${reminder.title}! (${reminder.category} - ${reminder.time})"
    }

    fun dismissReminderMessage() {
        _reminderMessage.value = null
    }
}
