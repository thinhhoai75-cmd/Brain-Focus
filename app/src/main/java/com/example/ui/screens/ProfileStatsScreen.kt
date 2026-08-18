package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.FocusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AvatarOption(val id: String, val emoji: String, val name: String)

val avatarOptions = listOf(
    AvatarOption("brain", "🧠", "Bộ não siêu việt"),
    AvatarOption("owl", "🦉", "Cú đêm focus"),
    AvatarOption("cat", "🐱", "Mèo lofi chill"),
    AvatarOption("monk", "🧘", "Thiền sư tập trung"),
    AvatarOption("astronaut", "👨‍🚀", "Phi hành gia"),
    AvatarOption("fox", "🦊", "Cáo thông thái")
)

@Composable
fun ProfileStatsScreen(
    viewModel: FocusViewModel,
    onRetakeAssessment: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val sessions by viewModel.allSessions.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showFbsDetailsDialog by remember { mutableStateOf(false) }
    var showGithubDialog by remember { mutableStateOf(false) }
    var showReminderManagerModal by remember { mutableStateOf(false) }

    val totalHours = ((userProfile?.totalFocusMinutes ?: 0) / 60.0)
    val fbsScore = userProfile?.fbsScore ?: 500
    val selectedAvatar = avatarOptions.find { it.id == (userProfile?.avatarIcon ?: "brain") } ?: avatarOptions[0]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // User Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(32.dp))
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MintContainer)
                            .clickable { showEditProfileDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedAvatar.emoji,
                            fontSize = 38.sp
                        )
                    }
                    IconButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier.testTag("edit_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Chỉnh sửa tài khoản",
                            tint = DeepTealPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userProfile?.name ?: "Học viên Focus",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${selectedAvatar.name} • ${userProfile?.email ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats 2x2 Grid with FBS, Ranking, Hours, and Streak
                val todayStr = viewModel.getTodayDateString()
                val yesterdayStr = viewModel.getYesterdayDateString()
                val activeStreak = userProfile?.getActiveStreak(todayStr, yesterdayStr) ?: 0

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // BFS Clickable Score Card
                        Card(
                            onClick = { showFbsDetailsDialog = true },
                            colors = CardDefaults.cardColors(containerColor = MintContainer.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$fbsScore",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = DeepTealPrimary
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Chi tiết BFS",
                                        tint = DeepTealPrimary,
                                        modifier = Modifier.size(16.dp).padding(start = 2.dp)
                                    )
                                }
                                Text(
                                    text = "Điểm BFS 🔍",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = OnMintContainer
                                )
                            }
                        }

                        // Ranking Points
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${userProfile?.rankingPoints ?: 1000}",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = DarkTealSecondary
                                )
                                Text(
                                    text = "Xếp hạng",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Total Hours
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1fh", totalHours),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = DeepTealLight
                                )
                                Text(
                                    text = "Tổng giờ học",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Daily Streak 🔥
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MintContainer.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🔥 $activeStreak",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = DeepTealPrimary
                                )
                                Text(
                                    text = "Chuỗi ngày học",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = OnMintContainer
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.retakeAssessment()
                            onRetakeAssessment()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("retake_quiz_button"),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Đánh giá lại BFS", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { showGithubDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("github_link_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("🐙 GitHub repo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reminders Management Section
        ReminderSectionCard(
            viewModel = viewModel,
            onOpenFullManager = { showReminderManagerModal = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Session History Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = DeepTealPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lịch sử phiên học tập trung",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có lịch sử phiên học.\nHãy hoàn thành phiên học 15-90 phút đầu tiên!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("session_history_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sessions) { session ->
                    val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = sdf.format(Date(session.timestamp))
                    val emotionEmoji = session.emotion?.split(" ")?.firstOrNull() ?: "⏱️"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = emotionEmoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Phiên học ${session.targetMinutes} phút",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = formattedDate,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "+${session.pointsEarned} đ",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = DeepTealPrimary
                                    )
                                    if (session.exitCount > 0) {
                                        Text(
                                            text = "${session.exitCount} lần xao nhãng",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PenaltyRed
                                        )
                                    } else {
                                        Text(
                                            text = "Tập trung tuyệt đối",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = DeepTealLight
                                        )
                                    }
                                }
                            }

                            // Emotion tag or reflection note if present
                            if (!session.emotion.isNullOrBlank() || !session.reflectionNote.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = SleekBorder.copy(alpha = 0.6f), thickness = 0.8.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (!session.emotion.isNullOrBlank()) {
                                        Surface(
                                            color = MintContainer.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "Cảm xúc: ${session.emotion}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = OnMintContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    if (session.reflectionBonusPoints > 0) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "✨ +${session.reflectionBonusPoints} đ tiến bộ",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = DeepTealPrimary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                if (!session.reflectionNote.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text("📝", fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = session.reflectionNote,
                                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Edit Profile (Avatar & Name)
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userProfile?.name ?: "",
            currentAvatarId = userProfile?.avatarIcon ?: "brain",
            onDismiss = { showEditProfileDialog = false },
            onSave = { newName, newAvatarId ->
                viewModel.updateProfileInfo(newName, newAvatarId)
                showEditProfileDialog = false
            }
        )
    }

    // Modal Dialog: Detailed FBS Breakdown
    if (showFbsDetailsDialog) {
        FbsBreakdownDialog(
            fbsScore = fbsScore,
            totalMinutes = userProfile?.totalFocusMinutes ?: 0,
            completedSessions = userProfile?.completedSessionsCount ?: 0,
            onDismiss = { showFbsDetailsDialog = false }
        )
    }

    // Modal Dialog: GitHub Repository Integration & Sync
    if (showGithubDialog) {
        GithubIntegrationDialog(
            currentGithubUrl = userProfile?.githubUrl ?: "",
            onDismiss = { showGithubDialog = false },
            onSaveGithubUrl = { url ->
                viewModel.updateGithubUrl(url)
                showGithubDialog = false
            }
        )
    }
    // Modal Dialog: Reminders Management
    if (showReminderManagerModal) {
        ReminderManagerModal(
            viewModel = viewModel,
            onDismiss = { showReminderManagerModal = false }
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentAvatarId: String,
    onDismiss: () -> Unit,
    onSave: (name: String, avatarId: String) -> Unit
) {
    var nameInput by remember { mutableStateOf(currentName) }
    var selectedAvatarId by remember { mutableStateOf(currentAvatarId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Cập nhật hồ sơ cá nhân",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Họ và tên hiển thị") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = "Chọn biểu tượng đại diện:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(180.dp)
                ) {
                    items(avatarOptions) { option ->
                        val isSelected = option.id == selectedAvatarId
                        Card(
                            onClick = { selectedAvatarId = option.id },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MintContainer else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DeepTealPrimary) else null,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(option.emoji, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    option.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(nameInput, selectedAvatarId) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu thay đổi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun FbsBreakdownDialog(
    fbsScore: Int,
    totalMinutes: Int,
    completedSessions: Int,
    onDismiss: () -> Unit
) {
    val levelTitle = when {
        fbsScore >= 700 -> "🟢 Level 1 – Khả năng tập trung ổn định"
        fbsScore >= 400 -> "🟡 Level 2 – Dễ bị phân tâm"
        else -> "🔴 Level 3 – Cần rèn luyện thêm"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Chi tiết điểm BFS",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DeepTealPrimary
                )
                Text(
                    text = levelTitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = DarkTealSecondary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    color = MintContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$fbsScore / 1000",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = DeepTealPrimary
                            )
                        )
                        Text(
                            text = "Thang điểm BFS từ 100 đến 1000 đánh giá mức độ sức khỏe não bộ và khả năng tập trung",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnMintContainer,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("• Điểm khởi đầu: Dựa trên bài kiểm tra 10 câu hỏi", style = MaterialTheme.typography.bodySmall)
                        Text("• Tích lũy tổng giờ học ($totalMinutes phút): +${totalMinutes / 5} BFS", style = MaterialTheme.typography.bodySmall)
                        Text("• Hoàn thành phiên học ($completedSessions phiên): +${completedSessions * 10} BFS", style = MaterialTheme.typography.bodySmall)
                        Text("• Phạt thoát ứng dụng / xao nhãng: -10 BFS / lần", style = MaterialTheme.typography.bodySmall, color = PenaltyRed)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đã hiểu")
            }
        }
    )
}

@Composable
fun GithubIntegrationDialog(
    currentGithubUrl: String,
    onDismiss: () -> Unit,
    onSaveGithubUrl: (url: String) -> Unit
) {
    var githubInput by remember { mutableStateOf(currentGithubUrl) }
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Text("🐙", fontSize = 36.sp) },
        title = {
            Text(
                text = "Liên kết GitHub & mã nguồn",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = DeepTealPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Lưu đường dẫn repository GitHub hoặc hồ sơ GitHub của bạn để dễ dàng truy cập mã nguồn ứng dụng.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = githubInput,
                    onValueChange = { githubInput = it },
                    label = { Text("Đường dẫn GitHub / hồ sơ") },
                    placeholder = { Text("https://github.com/username/project") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                if (githubInput.isNotBlank()) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val urlToOpen = if (!githubInput.startsWith("http://") && !githubInput.startsWith("https://")) {
                                    "https://$githubInput"
                                } else githubInput
                                uriHandler.openUri(urlToOpen)
                            } catch (e: Exception) {
                                // Ignore invalid URIs
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("🔗 Mở GitHub trên trình duyệt")
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MintContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Cách đẩy mã nguồn ứng dụng lên GitHub:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = DeepTealPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "1. Nhấp vào menu Cài đặt (góc phải giao diện AI Studio Build).\n2. Chọn 'Push to GitHub' hoặc 'Export ZIP'.\n3. Chọn repository GitHub của bạn để liên kết tự động!",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnMintContainer
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveGithubUrl(githubInput) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu đường dẫn")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
