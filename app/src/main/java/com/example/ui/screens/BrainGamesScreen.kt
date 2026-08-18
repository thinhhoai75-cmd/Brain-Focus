package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.FocusViewModel
import kotlinx.coroutines.delay

enum class BrainGameType(val title: String, val subtitle: String, val icon: String) {
    STROOP("Thử thách Stroop thần kinh", "Rèn luyện kiểm soát xung động & xao nhãng", "psychology"),
    MEMORY_MATRIX("Ma trận ô nhớ ngắn hạn", "Mở rộng dung lượng trí nhớ làm việc", "grid_view"),
    SPEED_MATH("Phản xạ tính nhẩm siêu tốc", "Kích thích thùy trán trước não bộ", "bolt")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrainGamesScreen(
    viewModel: FocusViewModel,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedGame by remember { mutableStateOf<BrainGameType?>(null) }
    val gameRewardMsg by viewModel.lastGameRewardMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Trò chơi rèn luyện não bộ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kích thích não bộ & tăng điểm BFS",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Trở về"
                            )
                        }
                    } else if (selectedGame != null) {
                        IconButton(onClick = { selectedGame = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Trở về danh sách trò chơi"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.testTag("brain_games_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Toast reward notification
            gameRewardMsg?.let { msg ->
                Surface(
                    color = MintContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = OnMintContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.dismissGameRewardMessage() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Đóng",
                                tint = OnMintContainer
                            )
                        }
                    }
                }
            }

            if (selectedGame == null) {
                // Game Selection Hub
                GameHubList(
                    onSelectGame = { selectedGame = it }
                )
            } else {
                when (selectedGame) {
                    BrainGameType.STROOP -> StroopGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 2).coerceIn(10, 40)
                            val bonusFbs = (score / 2).coerceIn(5, 20)
                            viewModel.completeBrainGame("Stroop thần kinh", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                    BrainGameType.MEMORY_MATRIX -> MemoryMatrixGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 3).coerceIn(10, 50)
                            val bonusFbs = (score / 2).coerceIn(5, 25)
                            viewModel.completeBrainGame("Ma trận ô nhớ", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                    BrainGameType.SPEED_MATH -> SpeedMathGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 2).coerceIn(10, 40)
                            val bonusFbs = (score / 2).coerceIn(5, 20)
                            viewModel.completeBrainGame("Phản xạ tính nhẩm", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun GameHubList(onSelectGame: (BrainGameType) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Visual Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .testTag("brain_games_hero_banner"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkTealSecondary)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_brain_gym),
                    contentDescription = "Brain Gym Artwork",
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
                                    DarkTealSecondary.copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
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
                            Text("🧠", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Phòng tập cho não bộ",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnMintContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Rèn luyện nhận thức 5 phút",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Kích thích thùy trán trước, tăng chỉ số BFS & điểm thưởng",
                        style = MaterialTheme.typography.bodySmall,
                        color = MintContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh sách trò chơi não bộ",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Surface(
                color = MintContainer.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "3 bài tập",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = DeepTealPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        BrainGameType.entries.forEach { game ->
            GameItemCard(
                game = game,
                onClick = { onSelectGame(game) }
            )
        }
    }
}

@Composable
private fun GameItemCard(
    game: BrainGameType,
    onClick: () -> Unit
) {
    val (tagLabel, durationLabel, rewardLabel) = when (game) {
        BrainGameType.STROOP -> Triple("Kiểm soát xung động", "20 giây", "+40 đ & +20 BFS")
        BrainGameType.MEMORY_MATRIX -> Triple("Trí nhớ làm việc", "Không giới hạn", "+50 đ & +25 BFS")
        BrainGameType.SPEED_MATH -> Triple("Tốc độ xử lý", "20 giây", "+40 đ & +20 BFS")
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("game_card_${game.name}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MintContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val iconVector = when (game) {
                            BrainGameType.STROOP -> Icons.Default.Palette
                            BrainGameType.MEMORY_MATRIX -> Icons.Default.GridView
                            BrainGameType.SPEED_MATH -> Icons.Default.Bolt
                        }
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = DeepTealPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = game.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = SleekBorder.copy(alpha = 0.5f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tagLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        color = MintContainer.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "🎁 $rewardLabel",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = OnMintContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Surface(
                    color = DeepTealPrimary,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Chơi ngay",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 1. STROOP GAME
// ----------------------------------------------------
data class StroopColorItem(val name: String, val color: Color)

val stroopColors = listOf(
    StroopColorItem("Đỏ", PenaltyRed),
    StroopColorItem("Xanh dương", Color(0xFF2563EB)),
    StroopColorItem("Xanh lá", Color(0xFF16A34A)),
    StroopColorItem("Vàng", Color(0xFFD97706)),
    StroopColorItem("Tím", Color(0xFF9333EA))
)

@Composable
fun StroopGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(20) }
    var isGameOver by remember { mutableStateOf(false) }

    var currentWord by remember { mutableStateOf(stroopColors.random()) }
    var currentInkColor by remember { mutableStateOf(stroopColors.random()) }

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
            isGameOver = true
            onFinishGame(score)
        }
    }

    fun nextQuestion() {
        currentWord = stroopColors.random()
        currentInkColor = stroopColors.random()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = MintContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Điểm: $score",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = OnMintContainer
                )
            }

            Surface(
                color = if (timeRemaining <= 5) PenaltyRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Thời gian: ${timeRemaining}s",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (timeRemaining <= 5) PenaltyRed else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (!isGameOver) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Chọn nút theo màu mực (không đọc chữ)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Stroop Word Display
                Card(
                    colors = CardDefaults.cardColors(containerColor = SleekSurfaceLight),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, SleekBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentWord.name,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 36.sp
                            ),
                            color = currentInkColor.color
                        )
                    }
                }
            }

            // Answer Buttons Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stroopColors) { item ->
                    Button(
                        onClick = {
                            if (item.color == currentInkColor.color) {
                                score += 1
                            } else {
                                score = (score - 1).coerceAtLeast(0)
                            }
                            nextQuestion()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = item.color),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .height(56.dp)
                            .testTag("stroop_btn_${item.name}")
                    ) {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            // GameOver View
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hoàn thành thử thách Stroop!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tổng số câu trả lời đúng: $score",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepTealPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Trở về danh sách trò chơi")
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2. MEMORY MATRIX GAME
// ----------------------------------------------------
@Composable
fun MemoryMatrixGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    var level by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var isShowingPattern by remember { mutableStateOf(true) }
    var activeIndices by remember { mutableStateOf(setOf<Int>()) }
    var selectedIndices by remember { mutableStateOf(setOf<Int>()) }
    var isGameOver by remember { mutableStateOf(false) }

    fun startLevel(currentLevel: Int) {
        isShowingPattern = true
        selectedIndices = emptySet()
        val countToPick = (3 + currentLevel / 2).coerceAtMost(8)
        activeIndices = (0 until 16).shuffled().take(countToPick).toSet()
    }

    LaunchedEffect(level) {
        startLevel(level)
        delay(1500L) // Show pattern for 1.5 seconds
        isShowingPattern = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(color = MintContainer, shape = RoundedCornerShape(12.dp)) {
                Text(
                    text = "Cấp độ: $level",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = OnMintContainer
                )
            }
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
                Text(
                    text = "Điểm: $score",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (!isGameOver) {
            Text(
                text = if (isShowingPattern) "Hãy ghi nhớ vị trí các ô sáng!" else "Chạm vào các ô đã sáng!",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isShowingPattern) DeepTealPrimary else MaterialTheme.colorScheme.onSurface
            )

            // 4x4 Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(0.9f)
            ) {
                items(16) { index ->
                    val isPattern = activeIndices.contains(index)
                    val isSelected = selectedIndices.contains(index)

                    val tileColor = when {
                        isShowingPattern && isPattern -> DeepTealPrimary
                        isSelected && isPattern -> DeepTealPrimary
                        isSelected && !isPattern -> PenaltyRed
                        else -> SleekSurfaceVariant
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(tileColor)
                            .border(1.dp, SleekBorder, RoundedCornerShape(16.dp))
                            .clickable(enabled = !isShowingPattern) {
                                if (!isSelected) {
                                    val newSelected = selectedIndices + index
                                    selectedIndices = newSelected

                                    if (!isPattern) {
                                        // Wrong tile!
                                        isGameOver = true
                                        onFinishGame(score)
                                    } else if (newSelected == activeIndices) {
                                        // Level complete!
                                        score += level * 10
                                        level++
                                    }
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Kết thúc ma trận nhớ!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Điểm đạt được: $score (Cấp $level)",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepTealPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Trở về danh sách trò chơi")
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. SPEED MATH GAME
// ----------------------------------------------------
@Composable
fun SpeedMathGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(20) }
    var isGameOver by remember { mutableStateOf(false) }

    var num1 by remember { mutableStateOf((1..20).random()) }
    var num2 by remember { mutableStateOf((1..15).random()) }
    var isTrueEquation by remember { mutableStateOf(listOf(true, false).random()) }
    var displayedResult by remember { mutableStateOf(0) }

    fun generateEquation() {
        num1 = (1..25).random()
        num2 = (1..15).random()
        isTrueEquation = listOf(true, false).random()
        val realSum = num1 + num2
        displayedResult = if (isTrueEquation) realSum else realSum + listOf(-2, -1, 1, 2, 3).random()
    }

    LaunchedEffect(Unit) {
        generateEquation()
    }

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
            isGameOver = true
            onFinishGame(score)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(color = MintContainer, shape = RoundedCornerShape(12.dp)) {
                Text(
                    text = "Điểm: $score",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = OnMintContainer
                )
            }
            Surface(
                color = if (timeRemaining <= 5) PenaltyRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Thời gian: ${timeRemaining}s",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (timeRemaining <= 5) PenaltyRed else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (!isGameOver) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SleekSurfaceLight),
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, SleekBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "$num1 + $num2 = $displayedResult",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 38.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (isTrueEquation) score += 1 else score = (score - 1).coerceAtLeast(0)
                        generateEquation()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text("Đúng ✔", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (!isTrueEquation) score += 1 else score = (score - 1).coerceAtLeast(0)
                        generateEquation()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PenaltyRed),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text("Sai ✖", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hoàn thành phản xạ số!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tổng điểm: $score",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = DeepTealPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTealPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Trở về danh sách trò chơi")
                }
            }
        }
    }
}
