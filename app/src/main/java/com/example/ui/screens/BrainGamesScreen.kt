package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.DeepTealDark
import com.example.ui.theme.DeepTealLight
import com.example.ui.theme.DeepTealPrimary
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.PenaltyRed
import com.example.ui.theme.SunsetOrangeAccent
import com.example.ui.viewmodel.FocusViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class BrainGameType {
    STROOP,
    MEMORY_MATRIX,
    SPEED_MATH
}

@Composable
fun BrainGamesScreen(viewModel: FocusViewModel) {
    var selectedGame by remember { mutableStateOf<BrainGameType?>(null) }
    val rewardMessage by viewModel.lastGameRewardMessage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedGame == null) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MintContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = DeepTealPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Brain Focus Gym",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Kích thích não bộ & tăng điểm BFS",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game reward banner
            if (rewardMessage != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MintContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = rewardMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = OnMintContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearGameRewardMessage() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Text("✕", color = DeepTealPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    BrainGymHeroBanner()
                }

                item {
                    Text(
                        text = "Danh sách bài tập khởi động vỏ não",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item {
                    BrainGameCard(
                        game = BrainGameType.STROOP,
                        title = "1. Stroop Thần Kinh",
                        subtitle = "Ức chế xung động & Phân biệt màu chữ",
                        benefit = "Tăng sức đề kháng trước cám dỗ vuốt màn hình",
                        icon = Icons.Default.Psychology,
                        iconBg = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF0284C7),
                        onClick = { selectedGame = BrainGameType.STROOP }
                    )
                }

                item {
                    BrainGameCard(
                        game = BrainGameType.MEMORY_MATRIX,
                        title = "2. Ma Trận Ô Nhớ",
                        subtitle = "Rèn luyện Working Memory (Trí nhớ làm việc)",
                        benefit = "Mở rộng dung lượng ghi nhớ thông tin khi giải toán",
                        icon = Icons.Default.GridOn,
                        iconBg = MintContainer,
                        iconTint = DeepTealPrimary,
                        onClick = { selectedGame = BrainGameType.MEMORY_MATRIX }
                    )
                }

                item {
                    BrainGameCard(
                        game = BrainGameType.SPEED_MATH,
                        title = "3. Phản Xạ Tính Nhẩm",
                        subtitle = "Tăng tốc độ xử lý nơ-ron dưới áp lực 20 giây",
                        benefit = "Đánh thức não bộ khỏi trạng thái uể oải, buồn ngủ",
                        icon = Icons.Default.Calculate,
                        iconBg = Color(0xFFFFEDD5),
                        iconTint = SunsetOrangeAccent,
                        onClick = { selectedGame = BrainGameType.SPEED_MATH }
                    )
                }
            }
        } else {
            // In Game Screen
            when (selectedGame) {
                BrainGameType.STROOP -> {
                    StroopGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 2).coerceIn(10, 40)
                            val bonusFbs = (score / 2).coerceIn(5, 20)
                            viewModel.completeBrainGame("Stroop thần kinh", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                }
                BrainGameType.MEMORY_MATRIX -> {
                    MemoryMatrixGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 3).coerceIn(10, 50)
                            val bonusFbs = (score / 2).coerceIn(5, 25)
                            viewModel.completeBrainGame("Ma trận ô nhớ", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                }
                BrainGameType.SPEED_MATH -> {
                    SpeedMathGameView(
                        onFinishGame = { score ->
                            val bonusPts = (score * 2).coerceIn(10, 40)
                            val bonusFbs = (score / 2).coerceIn(5, 20)
                            viewModel.completeBrainGame("Phản xạ tính nhẩm", score, bonusPts, bonusFbs)
                        },
                        onBack = { selectedGame = null }
                    )
                }
                null -> {}
            }
        }
    }
}

@Composable
fun BrainGymHeroBanner() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DeepTealPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "5 phút tập luyện trước giờ học",
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
}

@Composable
fun BrainGameCard(
    game: BrainGameType,
    title: String,
    subtitle: String,
    benefit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    val (tagLabel, durationLabel, rewardLabel) = when (game) {
        BrainGameType.STROOP -> Triple("Kiểm soát xung động", "20 giây", "+40 đ & +20 BFS")
        BrainGameType.MEMORY_MATRIX -> Triple("Trí nhớ làm việc", "Không giới hạn", "+50 đ & +25 BFS")
        BrainGameType.SPEED_MATH -> Triple("Tốc độ xử lý", "20 giây", "+40 đ & +20 BFS")
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "✨ Tác dụng: $benefit",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = DeepTealPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MintContainer)
                ) {
                    Text(
                        text = rewardLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnMintContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Luyện ngay")
                }
            }
        }
    }
}

// ---------------- GAME 1: STROOP TEST ----------------
@Composable
fun StroopGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val colorItems = listOf(
        Pair("ĐỎ", Color(0xFFEF4444)),
        Pair("XANH DƯƠNG", Color(0xFF3B82F6)),
        Pair("XANH LÁ", Color(0xFF10B981)),
        Pair("VÀNG", Color(0xFFF59E0B)),
        Pair("TÍM", Color(0xFF8B5CF6))
    )

    var round by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(20) }
    var isGameOver by remember { mutableStateOf(false) }

    var displayedWord by remember { mutableStateOf("ĐỎ") }
    var displayedColor by remember { mutableStateOf(Color(0xFF3B82F6)) }
    var actualColorName by remember { mutableStateOf("XANH DƯƠNG") }

    fun nextQuestion() {
        val wordPair = colorItems.random()
        val inkPair = colorItems.random()
        displayedWord = wordPair.first
        displayedColor = inkPair.second
        actualColorName = inkPair.first
    }

    LaunchedEffect(Unit) {
        nextQuestion()
        while (timeLeft > 0 && !isGameOver) {
            delay(1000L)
            timeLeft -= 1
        }
        isGameOver = true
        onFinishGame(score)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }
            Text(
                text = "Thử thách Stroop Thần Kinh",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer)
            ) {
                Text(
                    text = "${timeLeft}s",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                    color = DeepTealPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isGameOver) {
            Text(
                text = "Hãy chọn MÀU CỦA MỰC (Bỏ qua nội dung chữ viết)",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = displayedWord,
                        color = displayedColor,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Điểm hiện tại: $score",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepTealPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                colorItems.chunked(2).forEach { rowColors ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowColors.forEach { item ->
                            Button(
                                onClick = {
                                    if (item.first == actualColorName) {
                                        score += 1
                                    } else {
                                        score = (score - 1).coerceAtLeast(0)
                                    }
                                    round += 1
                                    nextQuestion()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = item.second)
                            ) {
                                Text(
                                    text = item.first,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Result
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 HẾT GIỜ!", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = DeepTealPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Số câu đúng: $score", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Vỏ não trước trán của bạn đã được kích hoạt xung động ức chế rất tốt!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Quay về danh sách game")
                    }
                }
            }
        }
    }
}

// ---------------- GAME 2: MEMORY MATRIX ----------------
@Composable
fun MemoryMatrixGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val gridSize = 4 // 4x4
    var level by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var highlightedIndices = remember { mutableStateListOf<Int>() }
    var selectedIndices = remember { mutableStateListOf<Int>() }
    var isMemorizing by remember { mutableStateOf(true) }
    var isGameOver by remember { mutableStateOf(false) }

    fun startNewRound() {
        highlightedIndices.clear()
        selectedIndices.clear()
        val count = (level + 2).coerceAtMost(7)
        val allIndices = (0 until 16).shuffled()
        highlightedIndices.addAll(allIndices.take(count))
        isMemorizing = true
    }

    LaunchedEffect(level) {
        if (!isGameOver) {
            startNewRound()
            delay(1500L) // show pattern for 1.5s
            isMemorizing = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }
            Text(
                text = "Ma Trận Ô Nhớ (Cấp $level)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer)
            ) {
                Text(
                    text = "Điểm: $score",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                    color = DeepTealPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isGameOver) {
            Text(
                text = if (isMemorizing) "Ghi nhớ các ô màu sáng..." else "Chạm vào các ô vừa sáng",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isMemorizing) SunsetOrangeAccent else DeepTealPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4x4 Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(16) { idx ->
                    val isHighlighted = isMemorizing && highlightedIndices.contains(idx)
                    val isSelected = selectedIndices.contains(idx)
                    val isCorrectSelection = isSelected && highlightedIndices.contains(idx)
                    val isWrongSelection = isSelected && !highlightedIndices.contains(idx)

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isHighlighted -> DeepTealPrimary
                                isCorrectSelection -> DeepTealLight
                                isWrongSelection -> PenaltyRed
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable(enabled = !isMemorizing && !isSelected) {
                                selectedIndices.add(idx)
                                if (!highlightedIndices.contains(idx)) {
                                    // Wrong selection -> Game over
                                    isGameOver = true
                                    onFinishGame(score)
                                } else {
                                    // Check if all found
                                    val foundAll = highlightedIndices.all { selectedIndices.contains(it) }
                                    if (foundAll) {
                                        score += level * 2
                                        if (level < 6) {
                                            level += 1
                                        } else {
                                            isGameOver = true
                                            onFinishGame(score)
                                        }
                                    }
                                }
                            }
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        } else {
            // Game Over
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🧠 KẾT THÚC BÀI TẬP", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = DeepTealPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Đạt đến cấp độ: $level", style = MaterialTheme.typography.titleMedium)
                    Text("Tổng điểm thưởng: $score", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Quay về")
                    }
                }
            }
        }
    }
}

// ---------------- GAME 3: SPEED MENTAL MATH ----------------
@Composable
fun SpeedMathGameView(
    onFinishGame: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(20) }
    var isGameOver by remember { mutableStateOf(false) }

    var num1 by remember { mutableIntStateOf(7) }
    var num2 by remember { mutableIntStateOf(8) }
    var operator by remember { mutableStateOf("+") }
    var correctAnswer by remember { mutableIntStateOf(15) }
    var options by remember { mutableStateOf(listOf(13, 14, 15, 16)) }

    fun generateNewMath() {
        val opType = Random.nextInt(3) // 0: +, 1: -, 2: *
        when (opType) {
            0 -> {
                num1 = Random.nextInt(12, 60)
                num2 = Random.nextInt(9, 45)
                operator = "+"
                correctAnswer = num1 + num2
            }
            1 -> {
                num1 = Random.nextInt(30, 90)
                num2 = Random.nextInt(10, num1)
                operator = "-"
                correctAnswer = num1 - num2
            }
            else -> {
                num1 = Random.nextInt(3, 12)
                num2 = Random.nextInt(3, 12)
                operator = "×"
                correctAnswer = num1 * num2
            }
        }
        val wrong1 = correctAnswer + Random.nextInt(1, 4)
        val wrong2 = correctAnswer - Random.nextInt(1, 4)
        val wrong3 = correctAnswer + (if (Random.nextBoolean()) 10 else -10)
        options = listOf(correctAnswer, wrong1, wrong2, wrong3).distinct().shuffled().take(4)
    }

    LaunchedEffect(Unit) {
        generateNewMath()
        while (timeLeft > 0 && !isGameOver) {
            delay(1000L)
            timeLeft -= 1
        }
        isGameOver = true
        onFinishGame(score)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }
            Text(
                text = "Phản Xạ Tính Nhẩm",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer)
            ) {
                Text(
                    text = "${timeLeft}s",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                    color = DeepTealPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isGameOver) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "$num1 $operator $num2 = ?",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Điểm tính đúng: $score",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = DeepTealPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.chunked(2).forEach { rowOpts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowOpts.forEach { opt ->
                            Button(
                                onClick = {
                                    if (opt == correctAnswer) {
                                        score += 1
                                    } else {
                                        score = (score - 1).coerceAtLeast(0)
                                    }
                                    generateNewMath()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTealDark)
                            ) {
                                Text(
                                    text = "$opt",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚡ KẾT QUẢ TÍNH NHẨM", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = DeepTealPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Số phép tính đúng: $score", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Quay về")
                    }
                }
            }
        }
    }
}
