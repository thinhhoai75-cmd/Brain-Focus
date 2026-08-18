package com.example.data.model

data class PopcornArticle(
    val id: Int,
    val title: String,
    val subtitle: String,
    val readTimeMinutes: Int,
    val category: String,
    val contentSections: List<ArticleSection>,
    val keyTakeaways: List<String>
)

data class ArticleSection(
    val sectionHeader: String,
    val textContent: String
)

object ArticleData {
    val articles = listOf(
        PopcornArticle(
            id = 1,
            title = "Hiện tượng 'não bỏng ngô' (popcorn brain) là gì?",
            subtitle = "Hiểu rõ lý do tại sao đầu óc bạn liên tục 'nổ' các suy nghĩ phân tán khi cố gắng tập trung.",
            readTimeMinutes = 4,
            category = "Khoa học não bộ",
            contentSections = listOf(
                ArticleSection(
                    "1. Nguồn gốc thuật ngữ não bỏng ngô",
                    "Thuật ngữ 'Popcorn Brain' được đưa ra lần đầu bởi Giáo sư David Levy thuộc Đại học Washington năm 2011. Ông mô tả đây là trạng thái não bộ quen với nhịp sống số nhanh đến mức các suy nghĩ nảy tưng tưng liên tục như những hạt bắp nổ trong nồi áp suất."
                ),
                ArticleSection(
                    "2. Tại sao lại xảy ra hiện tượng này?",
                    "Khi lướt các nền tảng video ngắn (TikTok, Reels, Shorts), thuật toán cung cấp phần thưởng dopamine cực kỳ nhanh chóng mỗi 5-15 giây. Khi chuyển sang các hoạt động nhịp chậm như đọc sách, làm bài tập hay nghe giảng, não bộ ngay lập tức cảm thấy 'thiếu dopamine' và tạo cảm giác bồn chồn."
                ),
                ArticleSection(
                    "3. Dấu hiệu nhận biết bạn đang bị não bỏng ngô",
                    "- Không thể ngồi yên đọc 1 trang sách mà không cầm điện thoại.\n- Đang làm việc này lại nhảy sang mở ứng dụng khác không mục đích.\n- Cảm thấy thời gian trôi qua quá chậm khi học tập.\n- Bị sương mù não (Brain Fog) và giảm khả năng ghi nhớ thông tin dài."
                )
            ),
            keyTakeaways = listOf(
                "Não bỏng ngô không phải là bệnh bẩm sinh mà là thói quen do môi trường số gây ra.",
                "Khả năng tập trung hoàn toàn có thể phục hồi nhờ tính linh hoạt thần kinh (Neuroplasticity).",
                "Giải pháp là rèn luyện các khoảng thời gian học sâu không gián đoạn."
            )
        ),
        PopcornArticle(
            id = 2,
            title = "Bẫy dopamine rác từ video ngắn & mạng xã hội",
            subtitle = "Giải mã hóa học thần kinh đằng sau cơn nghiện lướt màn hình vô thức.",
            readTimeMinutes = 5,
            category = "Cơ chế thần kinh",
            contentSections = listOf(
                ArticleSection(
                    "1. Dopamine chất lượng cao và dopamine rác",
                    "Dopamine chất lượng cao được tiết ra khi bạn giải xong một bài toán khó, hoàn thành phiên học 90 phút hay chinh phục mục tiêu lớn. Trong khi đó, 'Dopamine rác' đến từ việc lướt video ngắn ngẫu nhiên không tốn sức."
                ),
                ArticleSection(
                    "2. Ngưỡng dopamine bị đẩy lên quá cao",
                    "Khi tiêu thụ Dopamine rác quá mức, thụ thể Dopamine trong não sẽ bị giảm nhạy cảm (Desensitization). Kết quả là những hoạt động bình thường như học bài, làm việc sẽ trở nên cực kỳ nhàm chán và kiệt sức."
                ),
                ArticleSection(
                    "3. Vòng xoáy lặp lại",
                    "Lướt video ngắn -> Não tiết Dopamine rác -> Mất nhạy cảm -> Cần lướt nhiều hơn -> Giảm chú ý nghiêm trọng."
                )
            ),
            keyTakeaways = listOf(
                "Dopamine là chất dẫn truyền thần kinh thúc đẩy hành động, không phải chỉ là cảm giác sướng.",
                "Cắt giảm tiêu thụ nội dung rác giúp khôi phục độ nhạy thụ thể Dopamine.",
                "Mỗi phiên học tập trung là 1 bước giúp tái tạo Dopamine chất lượng cao."
            )
        ),
        PopcornArticle(
            id = 3,
            title = "Phương pháp tái cấu trúc khả năng tập trung (neuroplasticity)",
            subtitle = "Cách rèn luyện lại não bộ để giữ chú ý liên tục trong 90 phút.",
            readTimeMinutes = 6,
            category = "Rèn luyện tập trung",
            contentSections = listOf(
                ArticleSection(
                    "1. Tính linh hoạt thần kinh (neuroplasticity)",
                    "Bộ não con người giống như cơ bắp. Càng rèn luyện khả năng chống lại sự xao nhãng, các đường truyền thần kinh phụ trách kiểm soát chú ý (Prefrontal Cortex) càng trở nên dày dặn và mạnh mẽ."
                ),
                ArticleSection(
                    "2. Chiến lược phiên học 90 phút",
                    "Nhịp sinh học Ultradian Rhythms của con người chạy theo chu kỳ 90 phút đỉnh cao tập trung. Khi học tập liên tục trong 90 phút mà không có xao nhãng, bạn đạt trạng thái sâu nhất của trí tuệ."
                ),
                ArticleSection(
                    "3. Quy tắc không thoát app trong Brain Focus",
                    "Khi thực hiện phiên học, nếu rời khỏi ứng dụng, não bộ sẽ bị đứt gãy luồng tư duy. Việc duy trì phiên học trọn vẹn giúp tăng chỉ số BFS và nâng hạng trên Bảng xếp hạng."
                )
            ),
            keyTakeaways = listOf(
                "Tập trung là một kỹ năng có thể huấn luyện, không phải năng khiếu cố định.",
                "Tuân thủ 1-3 phiên học mỗi ngày để cảm nhận sự thay đổi rõ rệt sau 7 ngày.",
                "Loại bỏ hoàn toàn các yếu tố gây xao nhãng trước khi bật bộ đếm thời gian."
            )
        ),
        PopcornArticle(
            id = 4,
            title = "Kiến trúc môi trường học tập chống xao nhãng (Friction Architecture)",
            subtitle = "Nghệ thuật tạo rào cản vật lý và thị giác để bảo vệ 100% sự chú ý của não bộ.",
            readTimeMinutes = 5,
            category = "Môi trường",
            contentSections = listOf(
                ArticleSection(
                    "1. Định luật ma sát hành vi (Friction Principle)",
                    "Bộ não luôn chọn con đường tốn ít năng lượng nhất. Nếu điện thoại nằm ngay trước mặt, bạn chỉ mất 0.5 giây để cầm lên. Nhưng nếu điện thoại đặt ở phòng khác hoặc trong ngăn kéo có khóa, ma sát hành vi tăng lên khiến vỏ não trước có đủ thời gian để ngăn chặn xung động bốc đồng."
                ),
                ArticleSection(
                    "2. Dọn sạch rác thị giác ngoại vi",
                    "Nghiên cứu từ Viện Khoa học Thần kinh Princeton chứng minh rằng: Môi trường bừa bộn làm phân tán khả năng xử lý thông tin của não bộ. Giữ mặt bàn chỉ có 1 môn học duy nhất giúp tăng 25% tốc độ tư duy logic."
                ),
                ArticleSection(
                    "3. Thiết lập 'Góc linh thiêng' cho việc học",
                    "Chỉ ngồi vào bàn học khi thực sự học tập trung. Không ăn uống, không xem phim, không lướt mạng tại góc này để xây dựng phản xạ có điều kiện (Conditioned Response) cho não bộ: Hễ ngồi vào bàn là bước vào trạng thái Deep Work."
                )
            ),
            keyTakeaways = listOf(
                "Ý chí là hữu hạn, thiết kế môi trường mới là giải pháp bền vững.",
                "Tăng rào cản với các cám dỗ và giảm tối đa rào cản với tài liệu học tập.",
                "Một chiếc bàn sạch sẽ là nền tảng của một tâm trí tĩnh lặng."
            )
        ),
        PopcornArticle(
            id = 5,
            title = "Lộ trình cai nghiện Dopamine số cho học sinh & sinh viên",
            subtitle = "Phương pháp thanh lọc kích thích quá tải và thiết lập lại thụ thể khoái cảm lành mạnh.",
            readTimeMinutes = 6,
            category = "Cai nghiện số",
            contentSections = listOf(
                ArticleSection(
                    "1. Dopamine Detox thực sự là gì?",
                    "Cai nghiện Dopamine không phải là loại bỏ hoàn toàn niềm vui, mà là cắt đứt các nguồn kích thích siêu nhân tạo (Hyper-stimuli) như video ngắn liên tục, thông báo vô tận để đưa ngưỡng thụ thể não về mức bình thường."
                ),
                ArticleSection(
                    "2. Ba bước thanh lọc kỹ thuật số hiệu quả",
                    "- Tắt toàn bộ thông báo ứng dụng không khẩn cấp (chỉ giữ cuộc gọi gia đình).\n- Áp dụng 'Màn hình xám' (Grayscale) trên điện thoại để loại bỏ kích thích màu sắc sặc sỡ.\n- Thiết lập 'Khoảng trắng 60 phút': Không chạm vào thiết bị số trong 60 phút sau khi thức dậy và 60 phút trước khi đi ngủ."
                ),
                ArticleSection(
                    "3. Thay thế thói quen bằng phần thưởng thực chất",
                    "Khi não thèm lướt điện thoại, hãy thay thế bằng 5 phút rèn luyện não bộ trên Brain Focus, uống 1 ly nước ấm hoặc thực hiện 10 nhịp thở sâu. Sau 3-5 ngày, cảm giác bồn chồn sẽ giảm rõ rệt."
                )
            ),
            keyTakeaways = listOf(
                "Não bộ chỉ mất khoảng 7-14 ngày để phục hồi độ nhạy dopamine tự nhiên.",
                "Thay thế thói quen xấu bằng hành động tích cực thay vì chỉ cố gắng chịu đựng.",
                "Mỗi lần bạn từ chối mở điện thoại là một lần củng cố sức mạnh của thùy trán trước."
            )
        )
    )
}
