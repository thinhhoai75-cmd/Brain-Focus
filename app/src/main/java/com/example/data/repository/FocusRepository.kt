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

class FocusRepository(
    private val userDao: UserDao,
    private val studySessionDao: StudySessionDao,
    private val focusTipDao: FocusTipDao,
    private val focusReminderDao: FocusReminderDao
) {
    val userProfileFlow: Flow<UserProfileEntity?> = userDao.getUserProfileFlow()
    val allSessionsFlow: Flow<List<StudySessionEntity>> = studySessionDao.getAllSessionsFlow()
    val recentSessionsFlow: Flow<List<StudySessionEntity>> = studySessionDao.getRecentSessionsFlow(10)
    val completedSessionsCountFlow: Flow<Int> = studySessionDao.getCompletedSessionsCountFlow()
    val totalMinutesStudiedFlow: Flow<Long> = studySessionDao.getTotalMinutesStudiedFlow()
    val allTipsFlow: Flow<List<FocusTipEntity>> = focusTipDao.getAllTipsFlow()
    val allRemindersFlow: Flow<List<FocusReminderEntity>> = focusReminderDao.getAllRemindersFlow()

    suspend fun initializeDefaultRemindersIfNeeded() {
        if (focusReminderDao.getRemindersCount() == 0) {
            val defaultReminders = listOf(
                FocusReminderEntity(
                    title = "Bắt đầu phiên học Ultradian 90 phút",
                    category = "Học tập 90p",
                    time = "19:30",
                    frequency = "Hằng ngày",
                    isEnabled = true
                ),
                FocusReminderEntity(
                    title = "Khởi động não với game Brain Focus Gym",
                    category = "Game & Bài tập",
                    time = "09:00",
                    frequency = "Hằng ngày",
                    isEnabled = true
                ),
                FocusReminderEntity(
                    title = "Dopamine Reset: Cất điện thoại trước khi ngủ",
                    category = "Cai mạng xã hội",
                    time = "22:30",
                    frequency = "Hằng ngày",
                    isEnabled = true
                )
            )
            focusReminderDao.insertAll(defaultReminders)
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

    suspend fun saveInitialUserProfile(
        username: String,
        schoolOrGrade: String,
        targetGoal: String,
        initialFbs: Int
    ) {
        val user = UserProfileEntity(
            id = 1,
            username = username.ifBlank { "Bạn học tập" },
            schoolOrGrade = schoolOrGrade.ifBlank { "Lớp 12" },
            targetGoal = targetGoal.ifBlank { "Ôn thi Đại học" },
            fbsScore = initialFbs,
            currentPoints = 150,
            streakDays = 1,
            isAssessmentCompleted = true
        )
        userDao.insertOrUpdateProfile(user)
    }

    suspend fun updateAssessmentScore(newFbs: Int) {
        userDao.updateFbsScore(newFbs)
    }

    suspend fun recordCompletedSession(
        subjectName: String,
        plannedMinutes: Int,
        actualMinutes: Int,
        exitAttempts: Int,
        bgMusic: String
    ): Triple<Long, Int, Int> { // (sessionId, pointsEarned, fbsBoost)
        val isFlawless = exitAttempts == 0
        val basePoints = when (plannedMinutes) {
            90 -> 100
            45 -> 50
            25 -> 30
            else -> plannedMinutes
        }
        val bonus = if (isFlawless) 25 else 0
        val totalPoints = (basePoints + bonus).coerceAtLeast(10)
        val fbsBoost = if (isFlawless) 15 else 5

        val session = StudySessionEntity(
            subjectName = subjectName.ifBlank { "Môn học tập trung" },
            plannedDurationMinutes = plannedMinutes,
            actualDurationMinutes = actualMinutes,
            startTimestamp = System.currentTimeMillis() - (actualMinutes * 60 * 1000L),
            endTimestamp = System.currentTimeMillis(),
            isCompletedWithoutExit = isFlawless,
            exitAttemptCount = exitAttempts,
            backgroundMusicUsed = bgMusic,
            pointsEarned = totalPoints,
            fbsScoreImpact = fbsBoost
        )
        val sessionId = studySessionDao.insertSession(session)

        // Update User Profile
        val user = userDao.getUserProfileOnce()
        if (user != null) {
            val now = System.currentTimeMillis()
            val isNewDay = (now - user.lastStudyDateTimestamp) > (20 * 3600 * 1000L)
            val newStreak = if (isNewDay) user.streakDays + 1 else user.streakDays
            val updatedUser = user.copy(
                totalFocusMinutes = user.totalFocusMinutes + actualMinutes,
                totalSessionsCompleted = user.totalSessionsCompleted + 1,
                streakDays = newStreak,
                lastStudyDateTimestamp = now,
                currentPoints = user.currentPoints + totalPoints,
                fbsScore = (user.fbsScore + fbsBoost).coerceIn(100, 1000),
                rankTitle = calculateRankTitle(user.currentPoints + totalPoints)
            )
            userDao.updateProfile(updatedUser)
        }

        return Triple(sessionId, totalPoints, fbsBoost)
    }

    suspend fun saveSessionReflection(sessionId: Long, emotion: String, note: String): Pair<Int, Int> {
        studySessionDao.updateSessionReflection(sessionId, emotion, note)
        val bonusPts = if (note.isNotBlank()) 10 else 0
        val bonusFbs = if (note.isNotBlank()) 5 else 0
        if (bonusPts > 0) {
            userDao.adjustPointsAndFbs(bonusPts, bonusFbs)
        }
        return Pair(bonusPts, bonusFbs)
    }

    suspend fun applyDistractionPenalty(reason: String): Pair<Int, Int> {
        val pointsDeducted = 15
        val fbsDeducted = 10
        userDao.adjustPointsAndFbs(-pointsDeducted, -fbsDeducted)
        return Pair(pointsDeducted, fbsDeducted)
    }

    suspend fun recordGameReward(gameName: String, bonusPoints: Int, bonusFbs: Int) {
        userDao.adjustPointsAndFbs(bonusPoints, bonusFbs)
    }

    suspend fun toggleTipBookmark(tip: FocusTipEntity) {
        focusTipDao.updateTip(tip.copy(isBookmarked = !tip.isBookmarked))
    }

    suspend fun toggleReminder(reminder: FocusReminderEntity) {
        focusReminderDao.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
    }

    suspend fun addReminder(reminder: FocusReminderEntity): Long {
        return focusReminderDao.insertReminder(reminder)
    }

    suspend fun deleteReminder(reminder: FocusReminderEntity) {
        focusReminderDao.deleteReminder(reminder)
    }

    suspend fun updateProfileInfo(name: String, school: String, goal: String) {
        val user = userDao.getUserProfileOnce()
        if (user != null) {
            userDao.updateProfile(user.copy(username = name, schoolOrGrade = school, targetGoal = goal))
        }
    }

    private fun calculateRankTitle(points: Int): String {
        return when {
            points >= 3000 -> "Huyền Thoại Tập Trung (Focus Legend)"
            points >= 2000 -> "Bậc Thầy Tập Trung (Master)"
            points >= 1200 -> "Chiến Binh Sâu Sắc (Deep Warrior)"
            points >= 500 -> "Người Tiên Phong (Pioneer)"
            else -> "Tập Sự Kiên Trì (Novice)"
        }
    }
}
