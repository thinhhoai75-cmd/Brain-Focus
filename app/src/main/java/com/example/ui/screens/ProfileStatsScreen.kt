package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.FocusReminderEntity
import com.example.data.db.StudySessionEntity
import com.example.data.db.UserProfileEntity
import com.example.ui.components.AddReminderDialog
import com.example.ui.components.ReminderCardItem
import com.example.ui.theme.DeepTealPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.OrangeContainer
import com.example.ui.theme.PenaltyRed
import com.example.ui.theme.SunsetOrangeAccent
import com.example.ui.viewmodel.FocusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileStatsScreen(
    viewModel: FocusViewModel,
    onRetakeAssessment: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allSessions by viewModel.allSessions.collectAsStateWithLifecycle()
    val totalMinutes by viewModel.totalMinutesStudied.collectAsStateWithLifecycle()
    val completedSessionsCount by viewModel.completedSessionsCount.collectAsStateWithLifecycle()
    val allReminders by viewModel.allReminders.collectAsStateWithLifecycle()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showFbsDetailsDialog by remember { mutableStateOf(false) }

    val totalHours = String.format(Locale.US, "%.1f", totalMinutes / 60.0)
    val streak = userProfile?.streakDays ?: 1
    val points = userProfile?.currentPoints ?: 150
    val fbsScore = userProfile?.fbsScore ?: 500

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepTealPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(MintContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = DeepTealPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = userProfile?.username ?: "Bạn học tập",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "${userProfile?.schoolOrGrade ?: "Lớp 12"} • ${userProfile?.rankTitle ?: "Tập sự"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MintContainer
                            )
                            Text(
                                text = "🎯 ${userProfile?.targetGoal ?: "Ôn thi"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    IconButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Chỉnh sửa",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Stats 2x2 Grid with BFS, Ranking, Hours, and Streak
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: BFS Score
                    StatItemCard(
                        title = "Chỉ số BFS",
                        value = "$fbsScore",
                        subtitle = "Chạm xem chẩn đoán",
                        icon = Icons.Default.Psychology,
                        iconTint = DeepTealPrimary,
                        containerColor = MintContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { showFbsDetailsDialog = true }
                    )

                    // Card 2: Current Points
                    StatItemCard(
                        title = "Điểm xếp hạng",
                        value = "$points đ",
                        subtitle = userProfile?.rankTitle ?: "Tập sự",
                        icon = Icons.Default.EmojiEvents,
                        iconTint = SunsetOrangeAccent,
                        containerColor = OrangeContainer,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 3: Total Hours
                    StatItemCard(
                        title = "Tổng giờ học",
                        value = "${totalHours}h",
                        subtitle = "$completedSessionsCount phiên hoàn thành",
                        icon = Icons.Default.AccessTime,
                        iconTint = Color(0xFF0284C7),
                        containerColor = Color(0xFFE0F2FE),
                        modifier = Modifier.weight(1f)
                    )

                    // Card 4: Streak Days
                    StatItemCard(
                        title = "Chuỗi ngày học (Streak)",
                        value = "$streak ngày",
                        subtitle = "Bền bỉ hằng ngày",
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = PenaltyRed,
                        containerColor = Color(0xFFFEE2E2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Retake Diagnostic Button
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Đánh giá lại Não Bỏng Ngô (BFS)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kiểm tra lại độ nhạy xao nhãng sau 1 tuần rèn luyện",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onRetakeAssessment,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("retake_assessment_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Làm lại")
                    }
                }
            }
        }

        // Focus Reminders Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = DeepTealPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nhắc nhở rèn luyện kỷ luật",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { showAddReminderDialog = true },
                    modifier = Modifier.testTag("add_reminder_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm nhắc nhở",
                        tint = DeepTealPrimary
                    )
                }
            }
        }

        if (allReminders.isEmpty()) {
            item {
                Text(
                    text = "Chưa có lịch nhắc nhở nào. Bấm nút '+' để thêm.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(allReminders, key = { it.id }) { reminder ->
                ReminderCardItem(
                    reminder = reminder,
                    onToggle = { viewModel.toggleReminder(reminder) },
                    onDelete = { viewModel.deleteReminder(reminder) }
                )
            }
        }

        // Recent Study Sessions History Log
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = DeepTealPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lịch sử phiên học gần đây",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (allSessions.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có phiên học nào được ghi lại. Hãy bắt đầu phiên 90 phút đầu tiên!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(allSessions.take(10), key = { it.id }) { session ->
                SessionHistoryItem(session = session)
            }
        }
    }

    // Modal Dialog: Edit Profile
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(userProfile?.username ?: "") }
        var editSchool by remember { mutableStateOf(userProfile?.schoolOrGrade ?: "") }
        var editGoal by remember { mutableStateOf(userProfile?.targetGoal ?: "") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Chỉnh sửa thông tin", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Tên học viên") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editSchool,
                        onValueChange = { editSchool = it },
                        label = { Text("Trường / Lớp") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editGoal,
                        onValueChange = { editGoal = it },
                        label = { Text("Mục tiêu rèn luyện") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editName, editSchool, editGoal)
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Lưu thay đổi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Modal Dialog: Add Reminder
    if (showAddReminderDialog) {
        AddReminderDialog(
            onDismiss = { showAddReminderDialog = false },
            onConfirm = { title, category, time, frequency ->
                viewModel.addReminder(
                    FocusReminderEntity(
                        title = title,
                        category = category,
                        time = time,
                        frequency = frequency,
                        isEnabled = true
                    )
                )
                showAddReminderDialog = false
            }
        )
    }

    // Modal Dialog: Detailed BFS Breakdown
    if (showFbsDetailsDialog) {
        FbsBreakdownDialog(
            fbsScore = fbsScore,
            totalMinutes = totalMinutes,
            completedSessions = completedSessionsCount,
            onDismiss = { showFbsDetailsDialog = false }
        )
    }
}

@Composable
fun StatItemCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SessionHistoryItem(session: StudySessionEntity) {
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(session.startTimestamp))

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = session.subjectName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (session.isCompletedWithoutExit) MintContainer else Color(0xFFFEE2E2)
                    )
                ) {
                    Text(
                        text = if (session.isCompletedWithoutExit) "Kỷ luật 100%" else "${session.exitAttemptCount} lần xao nhãng",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (session.isCompletedWithoutExit) DeepTealPrimary else PenaltyRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$dateStr • ${session.actualDurationMinutes}/${session.plannedDurationMinutes} phút • Âm thanh: ${session.backgroundMusicUsed}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (session.note.isNotBlank() || session.postSessionEmotion.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💭 ${session.postSessionEmotion}: \"${session.note}\"",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FbsBreakdownDialog(
    fbsScore: Int,
    totalMinutes: Long,
    completedSessions: Int,
    onDismiss: () -> Unit
) {
    val levelDesc = when {
        fbsScore >= 700 -> "🟢 Level 1 – Khả năng tập trung ổn định"
        fbsScore >= 400 -> "🟡 Level 2 – Dễ bị phân tâm"
        else -> "🔴 Level 3 – Não Bỏng Ngô Mức Cao"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Chỉ Số Brain Focus Score (BFS)", fontWeight = FontWeight.Bold, color = DeepTealPrimary)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MintContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$fbsScore / 1000",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = DeepTealPrimary
                        )
                        Text(levelDesc, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Text(
                    text = "• BFS đo lường sức bền của vùng thùy trán trước (Prefrontal Cortex) và khả năng chống lại cơn thèm lướt mạng xã hội.",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Mỗi phiên học 90 phút trọn vẹn: +15 BFS\n• Ghi chép đúc kết sau buổi học: +5 BFS\n• Thoát app / lướt mạng xã hội khi đang học: -10 BFS\n• Khởi động não bộ với bài tập Gym: +5 ~ +25 BFS",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Đã hiểu")
            }
        }
    )
}
