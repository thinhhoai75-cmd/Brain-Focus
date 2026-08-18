package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.FocusViewModel
import com.example.ui.viewmodel.TimerState
import com.example.util.LofiSoundMode
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionScreen(
    viewModel: FocusViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedMinutes by viewModel.selectedMinutes.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val exitCount by viewModel.exitCount.collectAsState()
    val wasPaused by viewModel.wasAppPausedDuringSession.collectAsState()
    val reward by viewModel.lastSessionCompletionReward.collectAsState()

    // Audio & Penalty States
    val isAudioPlaying by viewModel.isAudioPlaying.collectAsState()
    val currentSoundMode by viewModel.soundMode.collectAsState()
    val audioVolume by viewModel.audioVolume.collectAsState()
    val autoPlayLofi by viewModel.autoPlayLofiOnStart.collectAsState()
    val lastPenaltyMsg by viewModel.lastPenaltyMessage.collectAsState()
    val reminderMsg by viewModel.reminderMessage.collectAsState()

    var showReminderManagerModal by remember { mutableStateOf(false) }

    val todayDate = viewModel.getTodayDateString()
    val isNewDay = userProfile?.lastSessionDate != todayDate
    val dailyCountToday = if (isNewDay) 0 else (userProfile?.dailySessionsToday ?: 0)
    val maxDailyReached = dailyCountToday >= 3

    val totalSeconds = selectedMinutes * 60
    val progress = if (totalSeconds > 0) remainingSeconds / totalSeconds.toFloat() else 0f

    val formattedMinutes = remainingSeconds / 60
    val formattedSecs = remainingSeconds % 60
    val timeDisplay = String.format(Locale.getDefault(), "%02d:%02d", formattedMinutes, formattedSecs)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reminder Notification Banner
            ReminderBannerAlert(
                message = reminderMsg,
                onDismiss = { viewModel.dismissReminderMessage() }
            )

            // Modern Hero Visual Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("focus_hero_banner"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DarkTealSecondary)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_focus_hero),
                        contentDescription = "Focus Hero Artwork",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        DarkTealSecondary.copy(alpha = 0.92f),
                                        DarkTealSecondary.copy(alpha = 0.55f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = MintContainer.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✨", fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Rèn luyện khả năng chú ý sâu",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OnMintContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Vùng tập trung tuyệt đối",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Mỗi phút tập trung giúp tái cấu trúc não bộ vững vàng",
                            style = MaterialTheme.typography.bodySmall,
                            color = MintContainer.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Header Stats Bar Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_stats_summary_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = userProfile?.name ?: "Học viên Focus",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Điểm BFS: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${userProfile?.fbsScore ?: 500}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = DeepTealPrimary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(MintContainer)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = DeepTealPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userProfile?.rankingPoints ?: 1000} đ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnMintContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Streak Status Banner Card
            val todayStr = viewModel.getTodayDateString()
            val yesterdayStr = viewModel.getYesterdayDateString()
            val activeStreak = userProfile?.getActiveStreak(todayStr, yesterdayStr) ?: 0
            val studiedToday = userProfile?.lastSessionDate == todayStr

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("streak_banner_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (studiedToday) MintContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (studiedToday) DeepTealPrimary else SleekBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🔥", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Chuỗi ngày học tập trung: $activeStreak ngày",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (studiedToday) DeepTealPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (studiedToday) "✅ Đã giữ chuỗi thành công hôm nay!" else "⚡ Hôm nay chưa học! Hãy hoàn thành 1 phiên để giữ chuỗi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (studiedToday) OnMintContainer else PenaltyRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Reminders Section Card
            ReminderSectionCard(
                viewModel = viewModel,
                onOpenFullManager = { showReminderManagerModal = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Last Penalty Message Banner (Clickable for full alert)
            lastPenaltyMsg?.let { msg ->
                Surface(
                    color = PenaltyRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PenaltyRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PenaltyRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = PenaltyRed,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.dismissPenaltyMessage() }) {
                            Icon(Icons.Default.Close, contentDescription = "Đóng", tint = PenaltyRed)
                        }
                    }
                }
            }

            // Daily session quota card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_limit_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (maxDailyReached) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = null,
                            tint = if (maxDailyReached) PenaltyRed else DeepTealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hạn ngạch ngày: $dailyCountToday/3 phiên",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = if (maxDailyReached) "Đã đạt tối đa hôm nay" else "Tối đa 90p/phiên",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration selector chips (15p, 25p, 45p, 60p, 90p)
            Text(
                text = "Chọn thời gian phiên học",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(15, 25, 45, 60, 90).forEach { mins ->
                    val isSelected = selectedMinutes == mins
                    val isEnabled = timerState == TimerState.IDLE && !maxDailyReached

                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectDuration(mins) },
                        label = {
                            Text(
                                text = "${mins}p",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        enabled = isEnabled,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepTealPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("duration_chip_$mins")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BIG CIRCULAR TIMER DISPLAY WITH PULSING GLOW
            val primaryColor = DeepTealPrimary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant

            val infiniteTransition = rememberInfiniteTransition(label = "pulse_halo")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.15f,
                targetValue = 0.45f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1400),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_alpha"
            )
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 0.95f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1400),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_scale"
            )

            Box(
                modifier = Modifier
                    .size(260.dp)
                    .testTag("timer_circle_display"),
                contentAlignment = Alignment.Center
            ) {
                if (timerState == TimerState.RUNNING) {
                    Box(
                        modifier = Modifier
                            .size(255.dp * pulseScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        AccentTealGlow.copy(alpha = pulseAlpha),
                                        MintContainer.copy(alpha = pulseAlpha * 0.35f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Canvas(modifier = Modifier.size(230.dp)) {
                    drawCircle(
                        color = trackColor,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                DeepTealLight,
                                DeepTealPrimary,
                                AccentTealGlow,
                                DeepTealPrimary
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeDisplay,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 46.sp,
                            letterSpacing = 2.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val statusText = when (timerState) {
                        TimerState.IDLE -> "Sẵn sàng học"
                        TimerState.RUNNING -> "Đang tập trung..."
                        TimerState.PAUSED -> "Đang tạm dừng"
                        TimerState.COMPLETED -> "Hoàn thành!"
                    }
                    Surface(
                        color = when (timerState) {
                            TimerState.RUNNING -> MintContainer
                            TimerState.PAUSED -> MaterialTheme.colorScheme.surfaceVariant
                            TimerState.COMPLETED -> MintContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (timerState == TimerState.RUNNING) DeepTealPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (exitCount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Xao nhãng: $exitCount lần",
                            style = MaterialTheme.typography.labelSmall,
                            color = PenaltyRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TIMER CONTROLS
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
                    IconButton(
                        onClick = { viewModel.resetSession() },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("reset_timer_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.width(20.dp))
                }

                Button(
                    onClick = {
                        when (timerState) {
                            TimerState.IDLE -> viewModel.startSession()
                            TimerState.RUNNING -> viewModel.pauseSession()
                            TimerState.PAUSED -> viewModel.resumeSession()
                            TimerState.COMPLETED -> viewModel.resetSession()
                        }
                    },
                    enabled = !maxDailyReached,
                    modifier = Modifier
                        .height(56.dp)
                        .width(180.dp)
                        .testTag("main_timer_action_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary)
                ) {
                    Icon(
                        imageVector = when (timerState) {
                            TimerState.RUNNING -> Icons.Default.Pause
                            else -> Icons.Default.PlayArrow
                        },
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (timerState) {
                            TimerState.IDLE -> "Bắt đầu"
                            TimerState.RUNNING -> "Tạm dừng"
                            TimerState.PAUSED -> "Tiếp tục"
                            TimerState.COMPLETED -> "Làm mới"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -----------------------------------------------------------
            // LOFI CHILL AUDIO PLAYER CARD
            // -----------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lofi_audio_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MintContainer,
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = DarkTealSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Nhạc lofi chill tập trung",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isAudioPlaying) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        MusicEqualizerAnimation()
                                    }
                                }
                                Text(
                                    text = currentSoundMode.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepTealPrimary
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.toggleAudioPlaying() },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isAudioPlaying) DeepTealPrimary else MintContainer)
                        ) {
                            Icon(
                                imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Phát/dừng nhạc",
                                tint = if (isAudioPlaying) Color.White else DarkTealSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sound Modes Scroll
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LofiSoundMode.entries.forEach { mode ->
                            val isSelected = mode == currentSoundMode
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectSoundMode(mode) },
                                label = {
                                    Text(
                                        text = mode.title.split(" ").take(2).joinToString(" "),
                                        fontSize = 11.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MintContainer,
                                    selectedLabelColor = OnMintContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Slider(
                            value = audioVolume,
                            onValueChange = { viewModel.setAudioVolume(it) },
                            valueRange = 0f..1f,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = DeepTealPrimary,
                                activeTrackColor = DeepTealPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // -----------------------------------------------------------
            // FOCUS REMINDERS CARD
            // -----------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("focus_reminders_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MintContainer,
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = DarkTealSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Lời nhắc & lịch tập trung",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Tùy chỉnh thời gian & tần suất nhắc học/nghỉ ngơi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { showReminderManagerModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Cài đặt", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            // -----------------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("distraction_simulation_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = PenaltyRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mô phỏng xao nhãng mạng xã hội / game",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Thoát ứng dụng hoặc mở mạng xã hội lúc đang học sẽ bị trừ 15 đ xếp hạng và 10 BFS!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("TikTok", "Instagram", "Facebook", "Game Online", "YouTube").forEach { appName ->
                            OutlinedButton(
                                onClick = {
                                    viewModel.triggerDistractionPenalty(appName)
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PenaltyRed.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = PenaltyRed),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = appName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Completion Dialog
        reward?.let { res ->
            SessionCompletionEvaluationDialog(
                reward = res,
                selectedMinutes = selectedMinutes,
                onDismiss = { viewModel.dismissCompletionDialog() },
                onSubmitFeedback = { emotion, reflection ->
                    viewModel.submitSessionFeedback(emotion, reflection)
                }
            )
        }
        if (showReminderManagerModal) {
            ReminderManagerModal(
                viewModel = viewModel,
                onDismiss = { showReminderManagerModal = false }
            )
        }
    }
}

data class EmotionOption(val emoji: String, val label: String)

val sessionEmotionOptions = listOf(
    EmotionOption("🤩", "Tuyệt vời"),
    EmotionOption("😊", "Hài lòng"),
    EmotionOption("😌", "Thư thái"),
    EmotionOption("😐", "Bình thường"),
    EmotionOption("😫", "Mệt mỏi")
)

@Composable
fun SessionCompletionEvaluationDialog(
    reward: com.example.data.repository.SessionCompletionResult,
    selectedMinutes: Int,
    onDismiss: () -> Unit,
    onSubmitFeedback: (emotion: String?, reflection: String?) -> Unit
) {
    var selectedEmotion by remember { mutableStateOf<String?>("🤩 Tuyệt vời") }
    var reflectionText by remember { mutableStateOf("") }
    val hasReflection = reflectionText.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎉 Hoàn thành phiên học!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = DeepTealPrimary
                )
                Text(
                    text = "Bạn đã giữ vững sự tập trung trong $selectedMinutes phút",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Reward summary cards
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MintContainer),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+${reward.pointsEarned + if (hasReflection) 10 else 0}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = DeepTealPrimary
                            )
                            Text("Điểm xếp hạng", style = MaterialTheme.typography.labelSmall, color = OnMintContainer)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+${reward.fbsBoost + if (hasReflection) 5 else 0}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = DeepTealLight
                            )
                            Text("Chỉ số BFS", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MintContainer),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔥 ${reward.currentStreak}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = DeepTealPrimary)
                            Text("Chuỗi ngày", style = MaterialTheme.typography.labelSmall, color = OnMintContainer)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Emotion Rating Section
                Text(
                    text = "Cảm xúc sau phiên học:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    sessionEmotionOptions.forEach { option ->
                        val optionValue = "${option.emoji} ${option.label}"
                        val isSelected = selectedEmotion == optionValue
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MintContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) DeepTealPrimary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedEmotion = if (isSelected) null else optionValue
                                }
                                .padding(vertical = 8.dp, horizontal = 2.dp)
                        ) {
                            Text(text = option.emoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DeepTealPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                        if (option != sessionEmotionOptions.last()) {
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reflection Note Section (Optional)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Đúc kết sự tiến bộ",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Tùy chọn",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = MintContainer.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎁", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ghi chú tiến bộ để nhận thêm +10 điểm & +5 BFS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = OnMintContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reflectionText,
                    onValueChange = { reflectionText = it },
                    placeholder = {
                        Text(
                            text = "Hôm nay bạn đã hoàn thành hoặc tiến bộ điều gì?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reflection_note_input"),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmitFeedback(
                        selectedEmotion,
                        if (hasReflection) reflectionText.trim() else null
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("dismiss_completion_dialog_button")
            ) {
                if (hasReflection) {
                    Text("Lưu đúc kết & nhận thưởng (+10 đ)")
                } else {
                    Text("Nhận thưởng & tiếp tục")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Bỏ qua", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@Composable
fun MusicEqualizerAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer")
    val heights = listOf(
        infiniteTransition.animateFloat(
            initialValue = 4f,
            targetValue = 14f,
            animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
            label = "bar1"
        ),
        infiniteTransition.animateFloat(
            initialValue = 12f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(tween(380), RepeatMode.Reverse),
            label = "bar2"
        ),
        infiniteTransition.animateFloat(
            initialValue = 6f,
            targetValue = 16f,
            animationSpec = infiniteRepeatable(tween(520), RepeatMode.Reverse),
            label = "bar3"
        ),
        infiniteTransition.animateFloat(
            initialValue = 14f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(tween(410), RepeatMode.Reverse),
            label = "bar4"
        )
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(16.dp)
    ) {
        heights.forEach { animHeight ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(animHeight.value.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(DeepTealPrimary)
            )
        }
    }
}

