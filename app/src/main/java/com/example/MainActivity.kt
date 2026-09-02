package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.BrainGamesScreen
import com.example.ui.screens.FocusSessionScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.OnboardingAssessmentScreen
import com.example.ui.screens.PopcornBrainInfoScreen
import com.example.ui.screens.ProfileStatsScreen
import com.example.ui.screens.StudyTipsScreen
import com.example.ui.theme.BrainFocusTheme
import com.example.ui.theme.DeepTealPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.viewmodel.FocusViewModel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import com.example.ui.screens.WebReviewScreen
import com.example.ui.theme.DeepTealDark

enum class AppTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    FOCUS_TIMER("Phiên học", Icons.Default.Timer),
    BRAIN_GYM("Gym não", Icons.Default.Psychology),
    POPCORN_INFO("Thư viện", Icons.Default.AutoStories),
    STUDY_TIPS("Bí quyết", Icons.Default.Lightbulb),
    LEADERBOARD("Xếp hạng", Icons.Default.EmojiEvents),
    PROFILE("Cá nhân", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    private val viewModel: FocusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrainFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppContent(viewModel = viewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.handleAppBackgrounded()
    }
}

@Composable
fun MainAppContent(viewModel: FocusViewModel) {
    var isWebMode by remember { mutableStateOf(true) }

    if (isWebMode) {
        WebReviewScreen(
            onSwitchToNative = {
                isWebMode = false
            }
        )
    } else {
        val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
        val isRetaking by viewModel.isRetakingAssessment.collectAsStateWithLifecycle()
        val diagnosis by viewModel.assessmentDiagnosis.collectAsStateWithLifecycle()

        var currentTab by remember { mutableStateOf(AppTab.FOCUS_TIMER) }

        // If assessment is not yet completed or currently retaking, show OnboardingAssessmentScreen
        val isFirstTime = userProfile == null || !(userProfile?.isAssessmentCompleted ?: false)

        if (isFirstTime || isRetaking) {
            OnboardingAssessmentScreen(
                onComplete = { name, school, goal, answers ->
                    viewModel.finishAssessment(name, school, goal, answers)
                },
                existingDiagnosis = diagnosis,
                isRetaking = isRetaking,
                onContinueToApp = {
                    currentTab = AppTab.FOCUS_TIMER
                }
            )
        } else {
            Scaffold(
                topBar = {
                    Surface(
                        color = DeepTealDark,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📱 Brain Focus (Native)",
                                color = androidx.compose.ui.graphics.Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            AssistChip(
                                onClick = { isWebMode = true },
                                label = {
                                    Text(
                                        text = "🌐 Xem Giao Diện Web",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = DeepTealDark
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFF5EEAD4)
                                )
                            )
                        }
                    }
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        AppTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepTealPrimary,
                                    selectedTextColor = DeepTealPrimary,
                                    indicatorColor = MintContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        AppTab.FOCUS_TIMER -> FocusSessionScreen(viewModel = viewModel)
                        AppTab.BRAIN_GYM -> BrainGamesScreen(viewModel = viewModel)
                        AppTab.POPCORN_INFO -> PopcornBrainInfoScreen()
                        AppTab.STUDY_TIPS -> StudyTipsScreen(viewModel = viewModel)
                        AppTab.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel)
                        AppTab.PROFILE -> ProfileStatsScreen(
                            viewModel = viewModel,
                            onRetakeAssessment = {
                                viewModel.startRetakeAssessment()
                            }
                        )
                    }
                }
            }
        }
    }
}
