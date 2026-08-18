package com.example.data.model

data class QuestionOption(
    val optionText: String,
    val scoreWeight: Int // Score contribution to FBS (Total max = 1000)
)

data class AssessmentQuestion(
    val id: Int,
    val title: String,
    val subtitle: String,
    val options: List<QuestionOption>
)

data class FbsDiagnosis(
    val levelName: String,
    val levelNumber: Int,
    val levelEmoji: String,
    val badgeColorHex: String,
    val review: String, // Nhận xét
    val suggestions: List<String>, // Brain Focus đề xuất
    val message: String, // Thông điệp
    val damagePercentage: Int = 15,
    val damageTitle: String = "Tổn hại rất thấp",
    val damageDetail: String = "",
    val summary: String = "",
    val adviceList: List<String> = emptyList()
)

object AssessmentData {
    val questions = listOf(
        AssessmentQuestion(
            id = 1,
            title = "1. Thời gian sử dụng điện thoại trung bình mỗi ngày của bạn là bao lâu?",
            subtitle = "Bao gồm lướt mạng xã hội, giải trí và xem video ngắn.",
            options = listOf(
                QuestionOption("A. Dưới 2 giờ/ngày", 100),
                QuestionOption("B. Từ 2–4 giờ/ngày", 75),
                QuestionOption("C. Từ 4–6 giờ/ngày", 40),
                QuestionOption("D. Trên 6 giờ/ngày", 10)
            )
        ),
        AssessmentQuestion(
            id = 2,
            title = "2. Bạn xem video ngắn (TikTok, Reels, Shorts...) với tần suất như thế nào?",
            subtitle = "Mức độ và thời lượng tiêu thụ nội dung định dạng ngắn mỗi ngày.",
            options = listOf(
                QuestionOption("A. Rất hiếm khi/Không sử dụng", 100),
                QuestionOption("B. Thỉnh thoảng (dưới 30 phút/ngày)", 75),
                QuestionOption("C. Thường xuyên (khoảng 1–2 giờ/ngày)", 40),
                QuestionOption("D. Nhiều lần trong ngày, thường xem liên tục", 10)
            )
        ),
        AssessmentQuestion(
            id = 3,
            title = "3. Khi đọc sách hoặc học bài, bạn thường có thể tập trung liên tục trong bao lâu trước khi muốn chuyển sang việc khác?",
            subtitle = "Khả năng duy trì chú ý vào tài liệu học thuật hoặc văn bản dài.",
            options = listOf(
                QuestionOption("A. Trên 45 phút", 100),
                QuestionOption("B. Khoảng 20–45 phút", 75),
                QuestionOption("C. Khoảng 10–20 phút", 40),
                QuestionOption("D. Dưới 10 phút", 10)
            )
        ),
        AssessmentQuestion(
            id = 4,
            title = "4. Khi không kiểm tra điện thoại trong khoảng 30–60 phút, bạn cảm thấy thế nào?",
            subtitle = "Trạng thái tâm lý khi tách khỏi các thông báo và thiết bị số.",
            options = listOf(
                QuestionOption("A. Hoàn toàn bình thường và thoải mái", 100),
                QuestionOption("B. Hơi tò mò nhưng vẫn kiểm soát được", 75),
                QuestionOption("C. Thường xuyên muốn kiểm tra", 40),
                QuestionOption("D. Rất bồn chồn hoặc lo lắng nếu không kiểm tra", 10)
            )
        ),
        AssessmentQuestion(
            id = 5,
            title = "5. Khi học tập, bạn thường làm gì?",
            subtitle = "Mức độ tập trung đơn nhiệm hoặc thói quen đa nhiệm khi học.",
            options = listOf(
                QuestionOption("A. Chỉ tập trung vào một nhiệm vụ", 100),
                QuestionOption("B. Thỉnh thoảng kiểm tra tin nhắn", 75),
                QuestionOption("C. Thường mở nhiều ứng dụng/tab cùng lúc", 40),
                QuestionOption("D. Vừa học vừa xem video/mạng xã hội", 10)
            )
        ),
        AssessmentQuestion(
            id = 6,
            title = "6. Khi học một bài giảng hoặc đọc tài liệu dài trên 30 phút, bạn thường:",
            subtitle = "Khả năng tiếp thu, phân tích sâu và xử lý thông tin phức tạp.",
            options = listOf(
                QuestionOption("A. Hiểu sâu và dễ ghi nhớ", 100),
                QuestionOption("B. Cần nỗ lực nhưng vẫn theo dõi được", 75),
                QuestionOption("C. Dễ mất tập trung hoặc bỏ dở", 40),
                QuestionOption("D. Rất khó duy trì chú ý, thường muốn chuyển sang nội dung khác", 10)
            )
        ),
        AssessmentQuestion(
            id = 7,
            title = "7. Bạn thường sử dụng điện thoại như thế nào trước khi ngủ hoặc ngay sau khi thức dậy?",
            subtitle = "Thói quen sử dụng thiết bị trong các khoảng thời gian nhạy cảm của não bộ.",
            options = listOf(
                QuestionOption("A. Hiếm khi, thường để điện thoại sang một bên", 100),
                QuestionOption("B. Thỉnh thoảng xem giờ hoặc báo thức", 75),
                QuestionOption("C. Thường xem điện thoại 30–60 phút trước khi ngủ", 40),
                QuestionOption("D. Thường xuyên dùng điện thoại ngay trước khi ngủ và ngay sau khi thức dậy", 10)
            )
        ),
        AssessmentQuestion(
            id = 8,
            title = "8. Khi có một khoảng thời gian rảnh rất ngắn, bạn thường:",
            subtitle = "Phản xạ vô thức trong các khoảng nghỉ ngắn (Micro-moments).",
            options = listOf(
                QuestionOption("A. Không cần dùng điện thoại", 100),
                QuestionOption("B. Thỉnh thoảng mới kiểm tra", 75),
                QuestionOption("C. Thường lấy điện thoại ra xem", 40),
                QuestionOption("D. Gần như ngay lập tức mở điện thoại", 10)
            )
        ),
        AssessmentQuestion(
            id = 9,
            title = "9. Khi đang học mà điện thoại xuất hiện thông báo, bạn thường:",
            subtitle = "Phản ứng trước các kích thích bất ngờ và khả năng kiểm soát xung động.",
            options = listOf(
                QuestionOption("A. Bỏ qua và tiếp tục học", 100),
                QuestionOption("B. Nhìn nhanh nhưng chưa mở", 75),
                QuestionOption("C. Dừng học để kiểm tra", 40),
                QuestionOption("D. Kiểm tra và thường tiếp tục sử dụng điện thoại sau đó", 10)
            )
        ),
        AssessmentQuestion(
            id = 10,
            title = "10. Sau khi vừa xem điện thoại hoặc video ngắn, bạn cảm thấy thế nào khi quay lại bài học?",
            subtitle = "Độ trễ và quán tính tư duy khi chuyển đổi ngữ cảnh từ giải trí sang học tập.",
            options = listOf(
                QuestionOption("A. Có thể tập trung lại ngay", 100),
                QuestionOption("B. Cần một chút thời gian để tập trung", 75),
                QuestionOption("C. Khá khó quay lại bài học", 40),
                QuestionOption("D. Thường muốn tiếp tục xem điện thoại thay vì học", 10)
            )
        )
    )

    fun calculateFbsScore(selectedOptionIndexes: List<Int>): Int {
        var totalWeight = 0
        questions.forEachIndexed { qIdx, question ->
            val optIdx = selectedOptionIndexes.getOrNull(qIdx) ?: 0
            val weight = question.options.getOrNull(optIdx)?.scoreWeight ?: 50
            totalWeight += weight
        }
        // Formula: 10 questions, each 10 to 100 => Total ranges from 100 to 1000
        return totalWeight.coerceIn(100, 1000)
    }

    fun getFbsDiagnosis(score: Int): FbsDiagnosis {
        return when {
            score >= 700 -> {
                val suggestions = listOf(
                    "Phiên học tập trung 30 phút",
                    "Duy trì chuỗi ngày học mỗi ngày",
                    "Theo dõi tiến độ rèn luyện hằng tuần",
                    "Thử thách đọc sách hoặc học tập 30 phút không gián đoạn"
                )
                FbsDiagnosis(
                    levelName = "Level 1 – Khả năng tập trung ổn định",
                    levelNumber = 1,
                    levelEmoji = "🟢",
                    badgeColorHex = "#10B981",
                    review = "Bạn đang duy trì khá tốt khả năng tập trung và có thói quen sử dụng thiết bị số tương đối hợp lý.",
                    suggestions = suggestions,
                    message = "Hãy duy trì những thói quen tốt để khả năng tập trung ngày càng bền vững.",
                    damagePercentage = 15,
                    damageTitle = "Tổn hại rất thấp (15%) • Khỏe mạnh & ổn định",
                    damageDetail = "Tế bào thần kinh thùy trán của bạn giữ được sự liên kết vững chắc, ít bị ảnh hưởng bởi dopamine ngắn và video clip ngắn.",
                    summary = "Bạn đang duy trì khá tốt khả năng tập trung và có thói quen sử dụng thiết bị số tương đối hợp lý.",
                    adviceList = suggestions
                )
            }
            score >= 400 -> {
                val suggestions = listOf(
                    "Phiên học tập trung 25 phút",
                    "Thả lỏng não bộ 2 phút trước mỗi phiên học",
                    "Chế độ tắt thông báo khi học",
                    "Thử thách 7 ngày tập trung",
                    "Ghi nhật ký tập trung sau mỗi buổi học"
                )
                FbsDiagnosis(
                    levelName = "Level 2 – Dễ bị phân tâm",
                    levelNumber = 2,
                    levelEmoji = "🟡",
                    badgeColorHex = "#F59E0B",
                    review = "Bạn có một số biểu hiện cho thấy sự chú ý dễ bị gián đoạn bởi các kích thích từ môi trường số.",
                    suggestions = suggestions,
                    message = "Chỉ cần thay đổi một vài thói quen nhỏ mỗi ngày, khả năng tập trung của bạn sẽ được cải thiện rõ rệt.",
                    damagePercentage = 45,
                    damageTitle = "Tổn hại trung bình (45%) • Dễ bị phân tâm",
                    damageDetail = "Khả năng chú ý còn tốt nhưng có dấu hiệu suy giảm nhẹ do thói quen kiểm tra thông báo và lướt mạng xã hội ngắt quãng.",
                    summary = "Bạn có một số biểu hiện cho thấy sự chú ý dễ bị gián đoạn bởi các kích thích từ môi trường số.",
                    adviceList = suggestions
                )
            }
            else -> {
                val suggestions = listOf(
                    "Tham gia lộ trình 21 ngày phục hồi não bộ",
                    "Phiên học tập trung 15–20 phút rồi tăng dần thời lượng",
                    "Nhật ký sử dụng điện thoại",
                    "Bài tập hít thở và thư giãn trước giờ học",
                    "Theo dõi tiến độ rèn luyện hằng tuần",
                    "Nhắc nhở giảm thời gian sử dụng mạng xã hội trong khung giờ học"
                )
                FbsDiagnosis(
                    levelName = "Level 3 – Cần rèn luyện thêm",
                    levelNumber = 3,
                    levelEmoji = "🔴",
                    badgeColorHex = "#EF4444",
                    review = "Bạn đang có khá nhiều biểu hiện cho thấy khả năng tập trung bị ảnh hưởng bởi việc sử dụng thiết bị số.",
                    suggestions = suggestions,
                    message = "Đừng lo nếu bạn đang ở Level 3. Khả năng tập trung hoàn toàn có thể được cải thiện nếu kiên trì rèn luyện từng bước.",
                    damagePercentage = 80,
                    damageTitle = "Tổn hại cao (80%) • Cần rèn luyện thêm",
                    damageDetail = "Khả năng tập trung liên tục bị phân mảnh. Não bộ dễ bị cuốn theo các kích thích ngắn và đa nhiệm.",
                    summary = "Bạn đang có khá nhiều biểu hiện cho thấy khả năng tập trung bị ảnh hưởng bởi việc sử dụng thiết bị số.",
                    adviceList = suggestions
                )
            }
        }
    }
}
