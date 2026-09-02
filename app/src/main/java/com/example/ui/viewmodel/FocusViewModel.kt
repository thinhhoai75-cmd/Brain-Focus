package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.FocusReminderEntity
import com.example.data.db.FocusTipEntity
import com.example.data.db.StudySessionEntity
import com.example.data.db.UserProfileEntity
import com.example.data.model.AssessmentData
import com.example.data.model.BfsDiagnosis
import com.example.data.model.LeaderboardUser
import com.example.data.model.SimulatedLeaderboard
import com.example.data.repository.FocusRepository
import com.example.util.LofiAudioPlayer
import com.example.util.LofiSoundType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SessionCompletionResult(
    val sessionId: Long,
    val pointsEarned: Int,
    val fbsBoost: Int,
    val actualMinutes: Int
)

class FocusViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = FocusRepository(
        database.userDao(),
        database.studySessionDao(),
        database.focusTipDao(),
        database.focusReminderDao()
    )
    val audioPlayer = LofiAudioPlayer(application)

    // User Profile
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Sessions & Statistics
    val allSessions: StateFlow<List<StudySessionEntity>> = repository.allSessionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalMinutesStudied: StateFlow<Long> = repository.totalMinutesStudiedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val completedSessionsCount: StateFlow<Int> = repository.completedSessionsCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Tips & Reminders
    val allTips: StateFlow<List<FocusTipEntity>> = repository.allTipsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReminders: StateFlow<List<FocusReminderEntity>> = repository.allRemindersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Study Session Timer State
    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _plannedMinutes = MutableStateFlow(90) // Default 90 minutes Ultradian
    val plannedMinutes: StateFlow<Int> = _plannedMinutes.asStateFlow()

    private val _secondsRemaining = MutableStateFlow(90 * 60)
    val secondsRemaining: StateFlow<Int> = _secondsRemaining.asStateFlow()

    private val _subjectName = MutableStateFlow("Toán - Luyện đề nâng cao")
    val subjectName: StateFlow<String> = _subjectName.asStateFlow()

    private val _selectedLofiSound = MutableStateFlow(LofiSoundType.NONE)
    val selectedLofiSound: StateFlow<LofiSoundType> = _selectedLofiSound.asStateFlow()

    private val _exitCount = MutableStateFlow(0)
    val exitCount: StateFlow<Int> = _exitCount.asStateFlow()

    private val _lastPenaltyMessage = MutableStateFlow<String?>(null)
    val lastPenaltyMessage: StateFlow<String?> = _lastPenaltyMessage.asStateFlow()

    private val _lastGameRewardMessage = MutableStateFlow<String?>(null)
    val lastGameRewardMessage: StateFlow<String?> = _lastGameRewardMessage.asStateFlow()

    // Assessment State
    private val _assessmentDiagnosis = MutableStateFlow<BfsDiagnosis?>(null)
    val assessmentDiagnosis: StateFlow<BfsDiagnosis?> = _assessmentDiagnosis.asStateFlow()

    private val _isRetakingAssessment = MutableStateFlow(false)
    val isRetakingAssessment: StateFlow<Boolean> = _isRetakingAssessment.asStateFlow()

    // Do Not Disturb (DND) / Notification Mute State
    private val _isDndActive = MutableStateFlow(com.example.util.NotificationMuteManager.isDndActive())
    val isDndActive: StateFlow<Boolean> = _isDndActive.asStateFlow()

    private val _autoDndOnSession = MutableStateFlow(true)
    val autoDndOnSession: StateFlow<Boolean> = _autoDndOnSession.asStateFlow()

    // Completion dialog state
    private val _lastSessionCompletionReward = MutableStateFlow<SessionCompletionResult?>(null)
    val lastSessionCompletionReward: StateFlow<SessionCompletionResult?> = _lastSessionCompletionReward.asStateFlow()

    private var timerJob: Job? = null

    // Combined Leaderboard
    val leaderboard: StateFlow<List<LeaderboardUser>> = combine(
        userProfile,
        completedSessionsCount
    ) { user, sessionsCount ->
        val name = user?.username ?: "Bạn"
        val points = user?.currentPoints ?: 150
        val fbs = user?.fbsScore ?: 500
        SimulatedLeaderboard.buildMergedLeaderboard(name, points, fbs, sessionsCount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeDefaultTipsIfNeeded()
            repository.initializeDefaultRemindersIfNeeded()
        }
    }

    fun setPlannedMinutes(minutes: Int) {
        if (!_isSessionActive.value) {
            _plannedMinutes.value = minutes
            _secondsRemaining.value = minutes * 60
        }
    }

    fun setSubjectName(name: String) {
        _subjectName.value = name
    }

    fun setLofiSound(sound: LofiSoundType) {
        _selectedLofiSound.value = sound
        if (_isSessionActive.value && !_isPaused.value) {
            audioPlayer.play(sound)
        } else {
            audioPlayer.stop()
        }
    }

    fun finishAssessment(name: String, school: String, goal: String, optionIndexList: List<Int>) {
        val calculatedScore = AssessmentData.calculateFbsScore(optionIndexList)
        val diagnosis = AssessmentData.getFbsDiagnosis(calculatedScore)
        _assessmentDiagnosis.value = diagnosis

        viewModelScope.launch {
            if (_isRetakingAssessment.value) {
                repository.updateAssessmentScore(calculatedScore)
                _isRetakingAssessment.value = false
            } else {
                repository.saveInitialUserProfile(
                    username = name,
                    schoolOrGrade = school,
                    targetGoal = goal,
                    initialFbs = calculatedScore
                )
            }
        }
    }

    fun startRetakeAssessment() {
        _isRetakingAssessment.value = true
        _assessmentDiagnosis.value = null
    }

    fun isDndPermissionGranted(): Boolean {
        return com.example.util.NotificationMuteManager.isPermissionGranted(getApplication())
    }

    fun openDndSettings() {
        com.example.util.NotificationMuteManager.openDndSettings(getApplication())
    }

    fun setAutoDndOnSession(enabled: Boolean) {
        _autoDndOnSession.value = enabled
    }

    fun toggleDndManual(enable: Boolean) {
        if (enable) {
            com.example.util.NotificationMuteManager.enableDoNotDisturb(getApplication())
        } else {
            com.example.util.NotificationMuteManager.disableDoNotDisturb(getApplication())
        }
        _isDndActive.value = com.example.util.NotificationMuteManager.isDndActive()
    }

    fun startSession() {
        _isSessionActive.value = true
        _isPaused.value = false
        _secondsRemaining.value = _plannedMinutes.value * 60
        _exitCount.value = 0
        _lastPenaltyMessage.value = null
        audioPlayer.play(_selectedLofiSound.value)

        // Automatically mute notifications if enabled
        if (_autoDndOnSession.value) {
            com.example.util.NotificationMuteManager.enableDoNotDisturb(getApplication())
            _isDndActive.value = true
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_secondsRemaining.value > 0 && _isSessionActive.value) {
                delay(1000L)
                if (!_isPaused.value) {
                    _secondsRemaining.value = _secondsRemaining.value - 1
                }
            }
            if (_secondsRemaining.value <= 0 && _isSessionActive.value) {
                completeSession()
            }
        }
    }

    fun pauseSession() {
        _isPaused.value = true
        audioPlayer.stop()
    }

    fun resumeSession() {
        _isPaused.value = false
        audioPlayer.play(_selectedLofiSound.value)
    }

    fun cancelSession() {
        timerJob?.cancel()
        timerJob = null
        _isSessionActive.value = false
        _isPaused.value = false
        audioPlayer.stop()

        if (_autoDndOnSession.value) {
            com.example.util.NotificationMuteManager.disableDoNotDisturb(getApplication())
            _isDndActive.value = false
        }
    }

    fun completeSession() {
        timerJob?.cancel()
        timerJob = null
        _isSessionActive.value = false
        _isPaused.value = false
        audioPlayer.stop()

        if (_autoDndOnSession.value) {
            com.example.util.NotificationMuteManager.disableDoNotDisturb(getApplication())
            _isDndActive.value = false
        }

        val actualMinutes = ((_plannedMinutes.value * 60 - _secondsRemaining.value) / 60).coerceAtLeast(1)
        val plannedMins = _plannedMinutes.value
        val exits = _exitCount.value
        val music = _selectedLofiSound.value.displayName

        viewModelScope.launch {
            val (sessionId, points, fbsBoost) = repository.recordCompletedSession(
                subjectName = _subjectName.value,
                plannedMinutes = plannedMins,
                actualMinutes = actualMinutes,
                exitAttempts = exits,
                bgMusic = music
            )
            _lastSessionCompletionReward.value = SessionCompletionResult(sessionId, points, fbsBoost, actualMinutes)
        }
    }

    fun triggerDistractionPenalty(appOrReason: String) {
        if (!_isSessionActive.value) return
        _exitCount.value = _exitCount.value + 1
        viewModelScope.launch {
            val (pointsDeducted, fbsDeducted) = repository.applyDistractionPenalty(appOrReason)
            _lastPenaltyMessage.value = "⚠️ Xao nhãng bởi $appOrReason! Bị trừ $pointsDeducted điểm xếp hạng và $fbsDeducted BFS."
        }
    }

    fun clearPenaltyMessage() {
        _lastPenaltyMessage.value = null
    }

    fun completeBrainGame(gameName: String, gameScore: Int, bonusPoints: Int, bonusFbs: Int) {
        viewModelScope.launch {
            repository.recordGameReward(gameName, bonusPoints, bonusFbs)
            _lastGameRewardMessage.value = "🎉 Xuất sắc! Hoàn thành $gameName ($gameScore đ). Nhận +$bonusPoints Đ xếp hạng & +$bonusFbs BFS!"
        }
    }

    fun clearGameRewardMessage() {
        _lastGameRewardMessage.value = null
    }

    fun toggleTipBookmark(tip: FocusTipEntity) {
        viewModelScope.launch {
            repository.toggleTipBookmark(tip)
        }
    }

    fun toggleReminder(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            repository.toggleReminder(reminder)
        }
    }

    fun addReminder(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            repository.addReminder(reminder)
        }
    }

    fun deleteReminder(reminder: FocusReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    fun updateProfile(name: String, school: String, goal: String) {
        viewModelScope.launch {
            repository.updateProfileInfo(name, school, goal)
        }
    }

    fun submitSessionReflection(sessionId: Long, emotion: String, reflectionNote: String) {
        viewModelScope.launch {
            if (sessionId > 0) {
                val (bonusPts, bonusFbs) = repository.saveSessionReflection(sessionId, emotion, reflectionNote)
                if (bonusPts > 0) {
                    _lastGameRewardMessage.value = "🌟 Tuyệt vời! Bạn nhận thêm +$bonusPts điểm & +$bonusFbs BFS nhờ đúc kết tiến bộ bản thân!"
                }
            }
            _lastSessionCompletionReward.value = null
        }
    }

    fun dismissSessionCompletion() {
        _lastSessionCompletionReward.value = null
    }

    fun handleAppBackgrounded() {
        if (_isSessionActive.value && !_isPaused.value) {
            _exitCount.value = _exitCount.value + 1
            viewModelScope.launch {
                val (pointsDeducted, fbsDeducted) = repository.applyDistractionPenalty("Thoát ứng dụng / Mở app giải trí")
                _lastPenaltyMessage.value = "⚠️ Phát hiện thoát app hoặc chuyển sang ứng dụng khác khi chưa xong phiên học! Bị trừ $pointsDeducted điểm xếp hạng và $fbsDeducted BFS."
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
        timerJob?.cancel()
    }
}
