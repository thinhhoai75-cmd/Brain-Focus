package com.example.data.model

data class AssessmentOption(
    val text: String,
    val scoreValue: Int // 10 (high popcorn brain) to 100 (high focus resilience)
)

data class AssessmentQuestion(
    val id: Int,
    val question: String,
    val category: String, // "Thói quen lướt mạng", "Độ tập trung 90p", "Xao nhãng", "Kiểm soát xung động"
    val options: List<AssessmentOption>
)

data class BfsDiagnosis(
    val score: Int,
    val levelTitle: String,
    val severityTag: String, // "Nhẹ", "Trung bình", "Báo động"
    val summary: String,
    val review: String,
    val recommendations: List<String>
)

object AssessmentData {
    val questions = listOf(
        AssessmentQuestion(
            id = 1,
            question = "Khi bắt đầu ngồi vào bàn học hoặc làm bài tập, bạn thường mở điện thoại sau bao lâu?",
            category = "Kiểm soát xung động",
            options = listOf(
                AssessmentOption("Ngay lập tức hoặc dưới 5 phút", 10),
                AssessmentOption("Khoảng 10 - 15 phút", 40),
                AssessmentOption("Khoảng 30 - 45 phút", 75),
                AssessmentOption("Giữ vững trên 60 - 90 phút mà không chạm vào điện thoại", 100)
            )
        ),
        AssessmentQuestion(
            id = 2,
            question = "Bạn có thói quen vừa học vừa nghe nhạc có lời hoặc liên tục đổi bài hát không?",
            category = "Độ tập trung",
            options = listOf(
                AssessmentOption("Thường xuyên, nếu không có tiếng nhạc/clip thì không ngồi yên được", 15),
                AssessmentOption("Thỉnh thoảng đổi bài hoặc lướt chọn playlist", 45),
                AssessmentOption("Chỉ nghe nhạc không lời/lofi/tiếng ồn trắng cố định", 85),
                AssessmentOption("Học trong không gian yên tĩnh tuyệt đối hoặc không bị phụ thuộc", 100)
            )
        ),
        AssessmentQuestion(
            id = 3,
            question = "Khi gặp một bài toán khó hoặc đoạn văn dài, cảm giác đầu tiên của bạn là gì?",
            category = "Sức bền nhận thức",
            options = listOf(
                AssessmentOption("Cực kỳ bồn chồn, muốn bỏ qua hoặc mở app khác ngay lập tức", 10),
                AssessmentOption("Đọc lướt nhanh, dễ mất kiên nhẫn sau 2-3 phút", 35),
                AssessmentOption("Cố gắng đọc lại 1-2 lần để tìm hướng giải", 70),
                AssessmentOption("Bình tĩnh phân tích từng bước, duy trì chú ý cho đến khi giải xong", 100)
            )
        ),
        AssessmentQuestion(
            id = 4,
            question = "Thời gian bạn lướt các video ngắn (TikTok, Reels, Shorts) mỗi ngày là bao nhiêu?",
            category = "Thói quen số",
            options = listOf(
                AssessmentOption("Trên 3 - 4 tiếng mỗi ngày", 10),
                AssessmentOption("Từ 1.5 - 3 tiếng", 40),
                AssessmentOption("Dưới 1 tiếng mỗi ngày", 75),
                AssessmentOption("Rất hiếm khi hoặc dưới 20 phút mỗi ngày", 100)
            )
        ),
        AssessmentQuestion(
            id = 5,
            question = "Bạn có thường cảm thấy 'rung chuông ảo' (nghĩ điện thoại có thông báo dù không có) không?",
            category = "Xung động vô thức",
            options = listOf(
                AssessmentOption("Liên tục trong ngày, luôn vô thức mở màn hình kiểm tra", 15),
                AssessmentOption("Thỉnh thoảng, đặc biệt khi cảm thấy chán bài học", 40),
                AssessmentOption("Hiếm khi", 80),
                AssessmentOption("Hoàn toàn không", 100)
            )
        ),
        AssessmentQuestion(
            id = 6,
            question = "Khả năng duy trì đọc một cuốn sách giáo khoa/tài liệu giấy liên tục 30 phút của bạn thế nào?",
            category = "Độ sâu chú ý",
            options = listOf(
                AssessmentOption("Gần như không thể, mắt đọc nhưng đầu nghĩ việc khác", 15),
                AssessmentOption("Đọc được 10-15 phút là muốn với tay lấy điện thoại", 45),
                AssessmentOption("Đọc tốt khoảng 25-30 phút với độ hiểu bài khá", 80),
                AssessmentOption("Đọc say mê 45-60 phút mà không hề bị phân tâm", 100)
            )
        ),
        AssessmentQuestion(
            id = 7,
            question = "Khi có thông báo tin nhắn mới khi đang học, phản xạ của bạn là gì?",
            category = "Kiểm soát kích thích",
            options = listOf(
                AssessmentOption("Mở xem và trả lời ngay lập tức, sau đó bị cuốn vào lướt mạng xã hội", 10),
                AssessmentOption("Mở xem tin nhắn rồi cố gắng quay lại học tiếp", 40),
                AssessmentOption("Liếc nhìn preview, nếu không khẩn cấp sẽ đợi hết phiên học", 75),
                AssessmentOption("Tắt toàn bộ thông báo hoặc để điện thoại ở chế độ Không làm phiền", 100)
            )
        ),
        AssessmentQuestion(
            id = 8,
            question = "Sau một phiên học dài 60-90 phút, bạn cảm thấy thế nào?",
            category = "Năng lượng não bộ",
            options = listOf(
                AssessmentOption("Rất hiếm khi học được 60 phút, thường bỏ dở giữa chừng", 15),
                AssessmentOption("Căng thẳng, mệt mỏi và thèm kích thích từ điện thoại", 45),
                AssessmentOption("Khá thoải mái nếu có quãng nghỉ ngắn giữa chừng", 80),
                AssessmentOption("Cảm giác sảng khoái và tự hào vì đạt trạng thái tập trung sâu", 100)
            )
        ),
        AssessmentQuestion(
            id = 9,
            question = "Bạn có thực hiện đa nhiệm (vừa học vừa nhắn tin, xem video phụ) không?",
            category = "Tập trung đơn nhiệm",
            options = listOf(
                AssessmentOption("Thường xuyên làm nhiều việc một lúc", 10),
                AssessmentOption("Thỉnh thoảng mở tab phụ để trò chuyện", 40),
                AssessmentOption("Chủ yếu chỉ tập trung vào tài liệu môn học", 80),
                AssessmentOption("Luôn thực hiện đơn nhiệm tuyệt đối (Monotasking)", 100)
            )
        ),
        AssessmentQuestion(
            id = 10,
            question = "Buổi tối trước khi đi ngủ, bạn thường sử dụng điện thoại đến khi nào?",
            category = "Nhịp sinh học",
            options = listOf(
                AssessmentOption("Dùng đến tận lúc ngủ thiếp đi, để điện thoại ngay đầu giường", 10),
                AssessmentOption("Dùng đến trước khi ngủ 10-15 phút", 40),
                AssessmentOption("Cất điện thoại trước khi ngủ 30-45 phút", 75),
                AssessmentOption("Ngừng dùng thiết bị điện tử trước khi ngủ 60 phút và để ngoài tầm với", 100)
            )
        )
    )

    fun calculateFbsScore(selectedOptionIndices: List<Int>): Int {
        if (selectedOptionIndices.size != questions.size) return 500
        var total = 0
        for (i in questions.indices) {
            val optionIdx = selectedOptionIndices[i]
            val score = questions[i].options.getOrNull(optionIdx)?.scoreValue ?: 50
            total += score
        }
        return total.coerceIn(100, 1000)
    }

    fun getFbsDiagnosis(score: Int): BfsDiagnosis {
        return when {
            score >= 750 -> BfsDiagnosis(
                score = score,
                levelTitle = "Não bộ Kháng Xao Nhãng (Deep Focus Master)",
                severityTag = "Tối ưu",
                summary = "Khả năng tập trung của bạn ở mức rất xuất sắc, ít bị ảnh hưởng bởi hội chứng Não Bỏng Ngô.",
                review = "Bạn sở hữu vùng vỏ não trước trán (Prefrontal Cortex) vững vàng, có khả năng trì hoãn khoái cảm dopamine và duy trì sự chú ý bền bỉ trong các chu kỳ học tập 90 phút.",
                recommendations = listOf(
                    "Duy trì các phiên học Ultradian 90 phút hằng ngày.",
                    "Thử thách bản thân với các bài học đòi hỏi tư duy trừu tượng cao.",
                    "Khởi động phiên học bằng các bài tập rèn luyện trí nhớ nâng cao."
                )
            )
            score >= 450 -> BfsDiagnosis(
                score = score,
                levelTitle = "Não bộ Nhạy Cảm Kích Thích (Moderate Popcorn Brain)",
                severityTag = "Trung bình",
                summary = "Bạn có một số biểu hiện cho thấy sự chú ý dễ bị gián đoạn bởi các kích thích từ môi trường số.",
                review = "Bộ não của bạn đã bắt đầu hình thành phản xạ tìm kiếm phần thưởng dopamine ngắn hạn khi gặp bài toán khó hoặc nội dung dài. Tuy nhiên, khả năng phục hồi là rất cao nếu áp dụng kỷ luật môi trường.",
                recommendations = listOf(
                    "Áp dụng nguyên tắc 'Điện thoại ngoài tầm nhìn' (đặt xa bàn học ít nhất 3m).",
                    "Chia nhỏ thời gian học thành các hiệp Pomodoro 25/5 hoặc 45/15.",
                    "Giới hạn thời gian lướt video ngắn xuống dưới 30 phút mỗi ngày."
                )
            )
            else -> BfsDiagnosis(
                score = score,
                levelTitle = "Hội Chứng Não Bỏng Ngô Mức Cao (Acute Popcorn Brain)",
                severityTag = "Báo động",
                summary = "Mức độ phân tán chú ý cao. Cần thực hiện lộ trình thanh lọc kỹ thuật số (Dopamine Reset) ngay.",
                review = "Tâm trí bạn liên tục 'nhảy cóc' như những hạt ngô nổ trong chảo nóng. Việc tiếp xúc quá nhiều với clip ngắn dưới 15 giây đã làm suy giảm ngưỡng kiên nhẫn của hệ thần kinh, gây khó khăn lớn khi ôn thi.",
                recommendations = listOf(
                    "Thực hiện 'Cai nghiện số 3 ngày': Tắt hoàn toàn thông báo mạng xã hội.",
                    "Bắt đầu với phiên học ngắn 15-20 phút và bật nhạc sóng não 40Hz hoặc White Noise.",
                    "Không dùng điện thoại trong 60 phút đầu sau khi thức dậy và trước khi đi ngủ."
                )
            )
        }
    }
}
