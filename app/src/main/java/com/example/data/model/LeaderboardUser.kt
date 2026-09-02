package com.example.data.model

data class LeaderboardUser(
    val rank: Int,
    val username: String,
    val schoolOrGrade: String,
    val fbsScore: Int, // Điểm BFS (Brain Focus Score)
    val totalHoursStudied: Double,
    val streakDays: Int,
    val completedSessions: Int,
    val rankPoints: Int, // Điểm xếp hạng rèn luyện
    val rankTitle: String,
    val isCurrentUser: Boolean = false
) {
    // Tổng điểm để lên bảng xếp hạng = Điểm xếp hạng + Điểm BFS
    val totalScore: Int
        get() = rankPoints + fbsScore

    val points: Int
        get() = totalScore
}

object SimulatedLeaderboard {
    private val baseCommunity = listOf(
        LeaderboardUser(1, "Hoàng Nam (Chuyên Toán)", "Lớp 12A1", 940, 142.5, 34, 95, 1910, "Bậc Thầy Tập Trung"),
        LeaderboardUser(2, "Minh Thư (IELTS 8.0)", "ĐH Ngoại Thương", 910, 128.0, 29, 85, 1650, "Bậc Thầy Tập Trung"),
        LeaderboardUser(3, "Đức Anh (Thủ khoa)", "Lớp 12 Chuyên", 885, 115.5, 24, 77, 1425, "Chiến Binh Sâu Sắc"),
        LeaderboardUser(4, "Khánh Linh", "Lớp 11", 840, 98.0, 19, 65, 1110, "Chiến Binh Sâu Sắc"),
        LeaderboardUser(5, "Bảo Long", "ĐH Bách Khoa", 815, 92.5, 15, 61, 1015, "Chiến Binh Sâu Sắc"),
        LeaderboardUser(6, "Phương Vy", "Lớp 12", 790, 84.0, 14, 56, 890, "Người Tiên Phong"),
        LeaderboardUser(7, "Tuấn Kiệt", "Lớp 10", 760, 75.5, 12, 50, 740, "Người Tiên Phong"),
        LeaderboardUser(8, "Thanh Hà", "ĐH Kinh Tế", 720, 68.0, 10, 45, 630, "Người Tiên Phong"),
        LeaderboardUser(9, "Gia Huy", "Lớp 12", 690, 58.5, 8, 39, 480, "Tập Sự Kiên Trì"),
        LeaderboardUser(10, "Ngọc Mai", "Lớp 11", 650, 49.0, 7, 33, 340, "Tập Sự Kiên Trì")
    )

    fun buildMergedLeaderboard(
        currentUserName: String,
        currentUserPoints: Int,
        currentUserFbs: Int,
        currentUserSessions: Int
    ): List<LeaderboardUser> {
        val userItem = LeaderboardUser(
            rank = 0,
            username = "$currentUserName (Bạn)",
            schoolOrGrade = "Đang rèn luyện",
            fbsScore = currentUserFbs,
            totalHoursStudied = (currentUserSessions * 90.0) / 60.0,
            streakDays = if (currentUserSessions > 0) (currentUserSessions / 2) + 1 else 1,
            completedSessions = currentUserSessions,
            rankPoints = currentUserPoints,
            rankTitle = when {
                (currentUserPoints + currentUserFbs) >= 2000 -> "Bậc Thầy Tập Trung"
                (currentUserPoints + currentUserFbs) >= 1200 -> "Chiến Binh Sâu Sắc"
                (currentUserPoints + currentUserFbs) >= 500 -> "Người Tiên Phong"
                else -> "Tập Sự Kiên Trì"
            },
            isCurrentUser = true
        )

        val combined = (baseCommunity + userItem).sortedByDescending { it.totalScore }
        return combined.mapIndexed { index, item ->
            item.copy(rank = index + 1)
        }
    }
}
