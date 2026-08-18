package com.example.data.model

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val points: Int,
    val fbsScore: Int,
    val completedSessions: Int,
    val avatarColorHex: String,
    val isCurrentUser: Boolean = false
)

object SimulatedLeaderboard {
    val initialPeers = listOf(
        LeaderboardUser(0, "Minh Anh (Zenith)", 1850, 920, 38, "#8B5CF6"),
        LeaderboardUser(0, "Tuấn Kiệt Focus", 1680, 880, 32, "#3B82F6"),
        LeaderboardUser(0, "Bảo Ngọc Scholar", 1520, 850, 27, "#EC4899"),
        LeaderboardUser(0, "Đức Huy Math", 1410, 810, 24, "#10B981"),
        LeaderboardUser(0, "Hoàng Nam DeepWork", 1320, 780, 21, "#F59E0B"),
        LeaderboardUser(0, "Gia Hân Med", 1250, 760, 18, "#06B6D4"),
        LeaderboardUser(0, "Trần Văn Khánh", 1180, 720, 15, "#6366F1"),
        LeaderboardUser(0, "Thùy Linh Poly", 1120, 690, 12, "#F43F5E"),
        LeaderboardUser(0, "Phạm Quang Minh", 1060, 650, 9, "#84CC16"),
        LeaderboardUser(0, "Nguyễn Thị Phương", 1010, 620, 6, "#D97706"),
        LeaderboardUser(0, "Thành Trung Learner", 980, 580, 4, "#64748B"),
        LeaderboardUser(0, "Lê Hoàng Yến", 920, 540, 2, "#0EA5E9")
    )

    fun buildMergedLeaderboard(currentUserName: String, userPoints: Int, userFbs: Int, userSessions: Int): List<LeaderboardUser> {
        val currentUser = LeaderboardUser(
            rank = 0,
            name = if (currentUserName.isNotBlank()) "$currentUserName (Bạn)" else "Bạn",
            points = userPoints,
            fbsScore = userFbs,
            completedSessions = userSessions,
            avatarColorHex = "#6366F1",
            isCurrentUser = true
        )

        val combined = (initialPeers + currentUser).sortedByDescending { it.points }
        return combined.mapIndexed { index, user ->
            user.copy(rank = index + 1)
        }
    }
}
