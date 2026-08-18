package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.screens.BrainGamesScreen
import com.example.ui.screens.FocusSessionScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.OnboardingAssessmentScreen
import com.example.ui.screens.PopcornBrainInfoScreen
import com.example.ui.screens.ProfileStatsScreen
import com.example.ui.screens.StudyTipsScreen
import com.example.ui.theme.FocusBrainTheme
import com.example.ui.viewmodel.FocusViewModel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsEsports
import com.example.ui.components.ReminderManagerModal

class MainActivity : ComponentActivity() {
    private val viewModel: FocusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FocusBrainTheme {
                // Lifecycle observer to detect when user leaves/exits app during active study session
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                            viewModel.onAppPaused()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                val userProfile by viewModel.userProfile.collectAsState()
                val isRetakingAssessment by viewModel.isRetakingAssessment.collectAsState()
                var currentTab by remember { mutableIntStateOf(0) } // 0: Session, 1: Games, 2: Leaderboard, 3: Info, 4: Tips, 5: Profile

                val isAssessmentCompleted = userProfile?.isAssessmentCompleted == true

                if (!isAssessmentCompleted || isRetakingAssessment) {
                    OnboardingAssessmentScreen(
                        viewModel = viewModel,
                        onAssessmentCompleted = {
                            viewModel.finishRetakingAssessment()
                            currentTab = 0
                        }
                    )
                } else {
                    MainAppScaffold(
                        viewModel = viewModel,
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(
    viewModel: FocusViewModel,
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val reminderMessage by viewModel.reminderMessage.collectAsState()
    var showRemindersDialog by remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showRemindersDialog) {
        ReminderManagerModal(
            viewModel = viewModel,
            onDismiss = { showRemindersDialog = false }
        )
    }

    if (reminderMessage != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.dismissReminderMessage() },
            icon = { Text("⏰", fontSize = 36.sp) },
            title = { Text("Lời nhắc tập trung", fontWeight = FontWeight.Bold) },
            text = { Text(reminderMessage!!, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { viewModel.dismissReminderMessage() },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Đã rõ")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentTab) {
                            0 -> "Phiên học tập trung"
                            1 -> "Game rèn luyện tập trung"
                            2 -> "Bảng xếp hạng"
                            3 -> "Học về não bỏng ngô"
                            4 -> "Bí quyết tập trung"
                            5 -> "Hồ sơ cá nhân"
                            else -> "Brain Focus"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    val todayStr = viewModel.getTodayDateString()
                    val yesterdayStr = viewModel.getYesterdayDateString()
                    val streakCount = userProfile?.getActiveStreak(todayStr, yesterdayStr) ?: 0
                    var showStreakDialog by remember { androidx.compose.runtime.mutableStateOf(false) }

                    // Streak Badge Button
                    androidx.compose.material3.Surface(
                        onClick = { showStreakDialog = true },
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("top_bar_streak_badge")
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$streakCount ngày",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (showStreakDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showStreakDialog = false },
                            icon = { Text("🔥", fontSize = 36.sp) },
                            title = {
                                Text(
                                    text = "Chuỗi ngày học tập trung",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            },
                            text = {
                                androidx.compose.foundation.layout.Column(
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Bạn đã duy trì chuỗi $streakCount ngày học liên tiếp! 🎉",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "• Mỗi ngày học ít nhất 1 phiên để tăng chuỗi.\n• Nếu bỏ lỡ 1 ngày, chuỗi sẽ bị đặt lại về 0.\n• Giữ chuỗi giúp bạn rèn luyện kỉ luật chống não bỏng ngô!",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            confirmButton = {
                                androidx.compose.material3.Button(
                                    onClick = { showStreakDialog = false },
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Tôi đã hiểu")
                                }
                            }
                        )
                    }

                    IconButton(
                        onClick = { showRemindersDialog = true },
                        modifier = Modifier.testTag("top_bar_reminders_button")
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Reminders", tint = MaterialTheme.colorScheme.primary)
                    }

                    if (currentTab != 5) {
                        IconButton(
                            onClick = { onTabSelected(5) },
                            modifier = Modifier.testTag("top_bar_profile_button")
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_navigation_bar")
            ) {
                val items = listOf(
                    Triple(0, "Phiên học", Icons.Default.Timer),
                    Triple(1, "Trò chơi", Icons.Default.SportsEsports),
                    Triple(2, "Xếp hạng", Icons.Default.EmojiEvents),
                    Triple(3, "Kiến thức", Icons.Default.Psychology),
                    Triple(4, "Bí quyết", Icons.Default.Lightbulb)
                )

                items.forEach { (index, label, icon) ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onTabSelected(index) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_$index")
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> FocusSessionScreen(viewModel = viewModel)
                1 -> BrainGamesScreen(viewModel = viewModel)
                2 -> LeaderboardScreen(viewModel = viewModel)
                3 -> PopcornBrainInfoScreen(viewModel = viewModel)
                4 -> StudyTipsScreen(viewModel = viewModel)
                5 -> ProfileStatsScreen(
                    viewModel = viewModel,
                    onRetakeAssessment = { onTabSelected(0) }
                )
            }
        }
    }
}
