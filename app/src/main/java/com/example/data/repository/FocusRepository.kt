package com.example.data.repository

import com.example.data.db.FocusReminderDao
import com.example.data.db.FocusReminderEntity
import com.example.data.db.FocusTipDao
import com.example.data.db.FocusTipEntity
import com.example.data.db.StudySessionDao
import com.example.data.db.StudySessionEntity
import com.example.data.db.UserDao
import com.example.data.db.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FocusRepository(
    private val userDao: UserDao,
    private val studySessionDao: StudySessionDao,
    private val focusTipDao: FocusTipDao,
    private val focusReminderDao: FocusReminderDao
) {
    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val allSessions: Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()
    val allTips: Flow<List<FocusTipEntity>> = focusTipDao.getAllTips()
    val allReminders: Flow<List<FocusReminderEntity>> = focusReminderDao.getAllReminders()

    suspend fun initializeDefaultRemindersIfNeeded() {
        if (focusReminderDao.getReminderCount() == 0) {
            val defaultReminders = listOf(
                FocusReminderEntity(
                    title = "Phiên học tập trung tối chống não bỏng ngô",
                    category = "Phiên học",
                    time = "20:00",
                    frequency = "Hằng ngày",
                    isEnabled = true
                ),
                FocusReminderEntity(
                    title = "Nghỉ ngơi mắt 15 phút & thở chánh niệm",
                    category = "Nghỉ ngơi",
                    time = "15:00",
                    frequency = "Thứ 2 - Thứ 6",
                    isEnabled = true
                ),
                FocusReminderEntity(
                    title = "Khởi động não với game Brain Focus Gym",
                    category = "Game & Bài tập",
                    time = "09:00",
                    frequency = "Hằng ngày",
                    isEnabled = false
                )
            )
            defaultReminders.forEach { focusReminderDao.insertReminder(it) }
        }
    }

    suspend fun initializeDefaultTipsIfNeeded() {
        val defaultTips = listOf(
            FocusTipEntity(
                title = "Phương pháp Pomodoro 25/5 classic",
                category = "Phương pháp học",
                description = "Học tập trung 25 phút, nghỉ ngơi 5 phút. Sau 4 chu kỳ, nghỉ dài 15-30 phút để não bộ phục hồi năng lượng."
            ),
            FocusTipEntity(
                title = "Tập trung đơn nhiệm (monotasking)",
                category = "Phương pháp học",
                description = "Chỉ mở duy nhất 1 ứng dụng hoặc 1 cuốn sách trong suốt phiên học. Đóng toàn bộ các tab mạng xã hội và phần mềm nhắn tin."
            ),
            FocusTipEntity(
                title = "Môi trường học không điện thoại (out of sight)",
                category = "Môi trường",
                description = "Đặt điện thoại cách xa tầm tay ít nhất 3 mét hoặc ở phòng khác. Việc tăng rào cản hành động giúp giảm 80% cơn thèm lướt vô thức."
            ),
            FocusTipEntity(
                title = "Thiết lập bàn học thị giác tối giản",
                category = "Môi trường",
                description = "Dọn sạch mặt bàn, chỉ để lại cuốn sách và tài liệu của môn học hiện tại. Loại bỏ các vật thể lộn xộn trong tầm nhìn ngoại vi giúp tiết kiệm 25% năng lượng chú ý."
            ),
            FocusTipEntity(
                title = "Ánh sáng sinh học & nhiệt độ mát mẻ (22-25°C)",
                category = "Môi trường",
                description = "Học tập dưới ánh sáng trắng tự nhiên (5000K-6500K) giúp ức chế melatonin gây buồn ngủ. Không gian thoáng khí kích thích máu lưu thông lên vỏ não trước."
            ),
            FocusTipEntity(
                title = "Quy tắc 2 phút chánh niệm",
                category = "Cai nghiện số",
                description = "Mỗi khi cảm thấy bồn chồn muốn mở điện thoại, dừng lại thở sâu 3 nhịp và hỏi bản thân: 'Tôi tìm kiếm điều gì ngay lúc này?'"
            ),
            FocusTipEntity(
                title = "Chuyển màn hình điện thoại sang màu xám (Grayscale)",
                category = "Cai nghiện số",
                description = "Màu sắc sặc sỡ trên icon ứng dụng kích hoạt thụ thể dopamine thị giác. Chuyển màn hình sang đen trắng giúp giảm 50% cảm giác muốn vuốt chạm vô thức."
            ),
            FocusTipEntity(
                title = "Thiết lập 'Vùng cấm thiết bị' trong phòng ngủ",
                category = "Cai nghiện số",
                description = "Không mang điện thoại lên giường ngủ trước khi ngủ 1 tiếng. Ánh sáng xanh phá vỡ nhịp sinh học và làm suy giảm khả năng tập trung sâu vào ngày hôm sau."
            ),
            FocusTipEntity(
                title = "Nhạc sóng não binaural beats (40Hz gamma)",
                category = "Sức khỏe não bộ",
                description = "Nghe nhạc không lời tần số 40Hz hoặc tiếng ồn trắng (white noise / rain sounds) để tăng độ tập trung sóng não và giảm căng thẳng."
            ),
            FocusTipEntity(
                title = "Ghi chép chủ động (active recall & Feynman)",
                category = "Phương pháp học",
                description = "Tự giải thích lại bài học bằng ngôn ngữ đơn giản nhất của bạn mà không nhìn tài liệu sau mỗi phiên học 45 phút."
            )
        )

        val existingTips = focusTipDao.getAllTipsList()
        if (existingTips.isEmpty()) {
            focusTipDao.insertAll(defaultTips)
        } else {
            val existingTitles = existingTips.map { it.title }.toSet()
            val missingTips = defaultTips.filter { it.title !in existingTitles }
            if (missingTips.isNotEmpty()) {
                focusTipDao.insertAll(missingTips)
            }
        }
    }

    suspend fun saveUserProfile(name: String, email: String, initialFbs: Int) {
        val today = getTodayDateString()
        val existing = userDao.getUserProfileDirect()
        val updatedUser = existing?.copy(
            name = name,
            email = email,
            fbsScore = initialFbs,
            isAssessmentCompleted = true
        ) ?: UserProfileEntity(
            id = 1,
            name = name,
            email = email,
            avatarIcon = "brain",
            fbsScore = initialFbs,
            rankingPoints = 1000, // Starts with 1000 points
            isAssessmentCompleted = true,
            lastSessionDate = today
        )
        userDao.insertOrUpdateUserProfile(updatedUser)
    }

    suspend fun updateUserProfileInfo(newName: String, newAvatarIcon: String) {
        val user = userDao.getUserProfileDirect() ?: return
        userDao.updateUserProfile(
            user.copy(
                name = newName.ifBlank { user.name },
                avatarIcon = newAvatarIcon
            )
        )
    }

    suspend fun applyDistractionPenalty(reason: String): Pair<Int, Int> { // Returns Pair(deductedPoints, deductedFbs)
        val user = userDao.getUserProfileDirect() ?: return Pair(0, 0)
        val deductedPoints = 15
        val deductedFbs = 10
        val newRankingPoints = (user.rankingPoints - deductedPoints).coerceAtLeast(0)
        val newFbsScore = (user.fbsScore - deductedFbs).coerceAtLeast(100)

        userDao.updateUserProfile(
            user.copy(
                rankingPoints = newRankingPoints,
                fbsScore = newFbsScore
            )
        )
        return Pair(deductedPoints, deductedFbs)
    }

    suspend fun recordGameReward(gameName: String, pointsEarned: Int, fbsEarned: Int) {
        val user = userDao.getUserProfileDirect() ?: return
        val newRankingPoints = user.rankingPoints + pointsEarned
        val newFbsScore = (user.fbsScore + fbsEarned).coerceAtMost(1000)
        userDao.updateUserProfile(
            user.copy(
                rankingPoints = newRankingPoints,
                fbsScore = newFbsScore
            )
        )
    }

    suspend fun updateFbsScore(newFbs: Int) {
        val user = userDao.getUserProfileDirect() ?: return
        userDao.updateUserProfile(user.copy(fbsScore = newFbs.coerceIn(100, 1000)))
    }

    suspend fun completeStudySession(
        targetMinutes: Int,
        actualSecondsCompleted: Int,
        exitCount: Int
    ): SessionCompletionResult { // Returns SessionCompletionResult(pointsEarned, fbsBoost, updatedStreak)
        val today = getTodayDateString()
        val yesterday = getYesterdayDateString()
        var user = userDao.getUserProfileDirect() ?: UserProfileEntity(name = "Người dùng", email = "")

        // Check daily session limit (max 3 per day)
        val isNewDay = user.lastSessionDate != today
        val todayCount = if (isNewDay) 0 else user.dailySessionsToday

        if (todayCount >= 3) {
            // Reached daily limit
            return SessionCompletionResult(
                sessionId = 0L,
                pointsEarned = 0,
                fbsBoost = 0,
                currentStreak = user.getActiveStreak(today, yesterday),
                targetMinutes = targetMinutes
            )
        }

        val completedMinutes = actualSecondsCompleted / 60
        val targetSeconds = targetMinutes * 60
        val isFullCompletion = actualSecondsCompleted >= targetSeconds - 5

        // Reward calculation
        var pointsEarned = completedMinutes * 2 // 2 pts per completed minute
        if (isFullCompletion) {
            pointsEarned += when (targetMinutes) {
                90 -> 60 // Bonus for 90 min max session
                60 -> 35
                45 -> 25
                25 -> 15
                else -> 10
            }
        }

        // Deduct points if exits were detected
        if (exitCount > 0) {
            pointsEarned = (pointsEarned - exitCount * 10).coerceAtLeast(5)
        }

        // FBS Boost calculation
        var fbsBoost = when {
            targetMinutes >= 90 && exitCount == 0 -> 25
            targetMinutes >= 45 && exitCount == 0 -> 15
            targetMinutes >= 25 && exitCount == 0 -> 10
            else -> 5
        }
        if (exitCount > 2) {
            fbsBoost = 2 // Reduced boost if heavily distracted
        }

        // Calculate updated Streak
        val newStreak = when {
            user.lastStreakDate == today -> if (user.studyStreak == 0) 1 else user.studyStreak
            user.lastStreakDate == yesterday -> user.studyStreak + 1
            else -> 1
        }

        val newTotalMinutes = user.totalFocusMinutes + completedMinutes
        val newRankingPoints = user.rankingPoints + pointsEarned
        val newFbsScore = (user.fbsScore + fbsBoost).coerceAtMost(1000)
        val newCompletedSessionsCount = user.completedSessionsCount + 1
        val updatedDailyCount = todayCount + 1

        // Record session
        val sessionEntity = StudySessionEntity(
            targetMinutes = targetMinutes,
            actualSecondsCompleted = actualSecondsCompleted,
            wereExitsDetected = exitCount > 0,
            exitCount = exitCount,
            pointsEarned = pointsEarned,
            fbsBoostEarned = fbsBoost,
            dateString = today
        )
        val sessionId = studySessionDao.insertSession(sessionEntity)

        // Update User
        userDao.updateUserProfile(
            user.copy(
                fbsScore = newFbsScore,
                rankingPoints = newRankingPoints,
                totalFocusMinutes = newTotalMinutes,
                completedSessionsCount = newCompletedSessionsCount,
                dailySessionsToday = updatedDailyCount,
                lastSessionDate = today,
                studyStreak = newStreak,
                lastStreakDate = today
            )
        )

        return SessionCompletionResult(sessionId, pointsEarned, fbsBoost, newStreak, targetMinutes)
    }

    suspend fun saveSessionReflection(
        sessionId: Long,
        emotion: String?,
        reflectionNote: String?
    ): Pair<Int, Int> { // Returns Pair(bonusPoints, bonusFbs)
        val session = studySessionDao.getSessionById(sessionId.toInt()) ?: return Pair(0, 0)
        val hasReflection = !reflectionNote.isNullOrBlank()
        val bonusPoints = if (hasReflection) 10 else 0
        val bonusFbs = if (hasReflection) 5 else 0

        val updatedSession = session.copy(
            emotion = emotion,
            reflectionNote = if (hasReflection) reflectionNote?.trim() else null,
            reflectionBonusPoints = bonusPoints,
            reflectionBonusFbs = bonusFbs,
            pointsEarned = session.pointsEarned + bonusPoints,
            fbsBoostEarned = session.fbsBoostEarned + bonusFbs
        )
        studySessionDao.updateSession(updatedSession)

        if (hasReflection) {
            val user = userDao.getUserProfileDirect()
            if (user != null) {
                userDao.updateUserProfile(
                    user.copy(
                        rankingPoints = user.rankingPoints + bonusPoints,
                        fbsScore = (user.fbsScore + bonusFbs).coerceAtMost(1000)
                    )
                )
            }
        }
        return Pair(bonusPoints, bonusFbs)
    }

    suspend fun toggleDailyReminder() {
        val user = userDao.getUserProfileDirect() ?: return
        userDao.updateUserProfile(user.copy(dailyReminderEnabled = !user.dailyReminderEnabled))
    }

    suspend fun updateDailyReminderTime(newTime: String) {
        val user = userDao.getUserProfileDirect() ?: return
        userDao.updateUserProfile(user.copy(dailyReminderTime = newTime))
    }

    suspend fun updateGithubUrl(url: String) {
        val user = userDao.getUserProfileDirect() ?: return
        userDao.updateUserProfile(user.copy(githubUrl = url))
    }

    suspend fun addNewTip(title: String, category: String, description: String) {
        val tip = FocusTipEntity(
            title = title,
            category = category,
            description = description,
            isCustom = true
        )
        focusTipDao.insertTip(tip)
    }

    suspend fun toggleFavoriteTip(tip: FocusTipEntity) {
        focusTipDao.updateTip(tip.copy(isFavorite = !tip.isFavorite))
    }

    suspend fun toggleAppliedTip(tip: FocusTipEntity) {
        focusTipDao.updateTip(tip.copy(isApplied = !tip.isApplied))
    }

    suspend fun addReminder(title: String, category: String, time: String, frequency: String) {
        val reminder = FocusReminderEntity(
            title = title,
            category = category,
            time = time,
            frequency = frequency,
            isEnabled = true
        )
        focusReminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: FocusReminderEntity) {
        focusReminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: FocusReminderEntity) {
        focusReminderDao.deleteReminder(reminder)
    }

    suspend fun toggleReminderEnabled(id: Int, isEnabled: Boolean) {
        focusReminderDao.toggleReminder(id, isEnabled)
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getYesterdayDateString(): String {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(cal.time)
    }
}

data class SessionCompletionResult(
    val sessionId: Long = 0,
    val pointsEarned: Int,
    val fbsBoost: Int,
    val currentStreak: Int,
    val targetMinutes: Int = 25
)
