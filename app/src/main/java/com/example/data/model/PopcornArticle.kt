package com.example.data.model

data class ArticleSection(
    val subtitle: String,
    val text: String
)

data class PopcornArticle(
    val id: Int,
    val title: String,
    val subtitle: String,
    val readTimeMinutes: Int,
    val category: String,
    val contentSections: List<ArticleSection>,
    val keyTakeaways: List<String>
)

object PopcornBrainData {
    val articles = listOf(
        PopcornArticle(
            id = 1,
            title = "Khoa học thần kinh về 'Não Bỏng Ngô' (Popcorn Brain)",
            subtitle = "Tại sao tâm trí bạn liên tục nhảy cóc và không thể ngồi yên đọc sách quá 10 phút?",
            readTimeMinutes = 4,
            category = "Khoa học não bộ",
            contentSections = listOf(
                ArticleSection(
                    "1. Định nghĩa Não Bỏng Ngô là gì?",
                    "Thuật ngữ 'Popcorn Brain' được đặt ra bởi nhà nghiên cứu David Levy (Đại học Washington) vào năm 2011 để mô tả trạng thái mà não bộ quen thuộc với nhịp độ kích thích siêu nhanh của thế giới số đến mức dòng suy nghĩ liên tục nảy lên lộp độp như ngô trong lò vi sóng."
                ),
                ArticleSection(
                    "2. Cơ chế Dopamine & Vòng lặp kích thích vô tận",
                    "Mỗi lần bạn vuốt màn hình TikTok hoặc Reels, não nhận được một liều Dopamine bất ngờ (Variable Reward Schedule). Khi tiếp xúc hàng trăm lần mỗi ngày, thụ thể Dopamine D2 bị chai lì (down-regulation). Hậu quả là những hoạt động có tốc độ chậm như đọc sách giáo khoa, suy nghĩ giải toán bỗng trở nên nhàm chán đến mức não không chịu đựng nổi."
                ),
                ArticleSection(
                    "3. Khả năng tái định hình thần kinh (Neuroplasticity)",
                    "Tin vui là não bộ có tính dẻo dai thần kinh. Chỉ sau 7-14 ngày giảm tiếp xúc với kích thích nhanh và tập trung đơn nhiệm, các khớp thần kinh tại thùy trán trước (Prefrontal Cortex) sẽ tự động phục hồi khả năng tập trung sâu."
                )
            ),
            keyTakeaways = listOf(
                "Não bỏng ngô không phải là bệnh bẩm sinh mà là thói quen do môi trường số gây ra.",
                "Thủ phạm chính là phần thưởng Dopamine ngẫu nhiên từ video ngắn dưới 15 giây.",
                "Bạn hoàn toàn có thể khôi phục lại khả năng tập trung bằng phương pháp luyện tập đúng đắn."
            )
        ),
        PopcornArticle(
            id = 2,
            title = "Chu kỳ tập trung 90 phút & Nhịp sinh học Ultradian",
            subtitle = "Bí mật tối ưu năng lượng học tập dựa trên nhịp sinh học tự nhiên của cơ thể con người.",
            readTimeMinutes = 5,
            category = "Phương pháp học",
            contentSections = listOf(
                ArticleSection(
                    "1. Nhịp sinh học Ultradian là gì?",
                    "Giáo sư Nathaniel Kleitman, nhà tiên phong nghiên cứu về giấc ngủ, phát hiện cơ thể con người vận hành theo chu kỳ 90-120 phút cả ngày lẫn đêm. Trong 90 phút, não bộ chuyển từ trạng thái hưng phấn nhẹ -> tập trung tối đa -> suy giảm năng lượng tự nhiên."
                ),
                ArticleSection(
                    "2. Ứng dụng 90 phút vào ôn thi & Deep Work",
                    "Thay vì học 4-5 tiếng liên tục gây kiệt sức, hãy chia ngày học thành các khối (blocks) 90 phút. 10 phút đầu khởi động não, 70 phút giải quyết bài tập khó nhất, và 10 phút cuối đúc kết kiến thức. Sau mỗi 90 phút, bắt buộc nghỉ ngơi 15-20 phút không chạm điện thoại."
                ),
                ArticleSection(
                    "3. Quy tắc không thoát app trong Brain Focus",
                    "Khi thực hiện phiên học, nếu rời khỏi ứng dụng, não bộ sẽ bị đứt gãy luồng tư duy. Việc duy trì phiên học trọn vẹn giúp tăng chỉ số BFS và nâng hạng trên Bảng xếp hạng."
                )
            ),
            keyTakeaways = listOf(
                "Chu kỳ 90 phút là giới hạn vàng của sự chú ý sinh học con người.",
                "Học sâu 90 phút mang lại hiệu quả cao hơn 4 tiếng học vừa làm vừa lướt mạng.",
                "Khoảng nghỉ 15 phút giữa các chu kỳ giúp củng cố trí nhớ dài hạn vào vỏ não."
            )
        ),
        PopcornArticle(
            id = 3,
            title = "Âm thanh sóng não Gamma & Lofi: Tăng cường tập trung",
            subtitle = "Cách tần số 40Hz và tiếng ồn trắng giúp đồng bộ hóa các nơ-ron thần kinh.",
            readTimeMinutes = 3,
            category = "Sức khỏe não bộ",
            contentSections = listOf(
                ArticleSection(
                    "1. Sóng não Gamma 40Hz là gì?",
                    "Sóng não Gamma (30Hz - 100Hz, đặc biệt là 40Hz) liên quan chặt chẽ đến sự chú ý tập trung cao độ, xử lý thông tin phức tạp và liên kết trí nhớ. Nghe âm thanh Binaural Beats 40Hz kích thích đồng bộ hóa sóng não ở hai bán cầu."
                ),
                ArticleSection(
                    "2. Lofi & Tiếng ồn hồng / Tiếng mưa rơi",
                    "Các bản nhạc Lofi có nhịp độ đều đặn 70-80 BPM (tương đương nhịp tim khi nghỉ ngơi) giúp giảm hormone căng thẳng Cortisol, trong khi âm thanh thiên nhiên tạo ra 'màn chắn âm thanh' ngăn chặn tiếng ồn xao nhãng xung quanh."
                )
            ),
            keyTakeaways = listOf(
                "Âm thanh không lời giúp kích hoạt hệ thần kinh phó giao cảm.",
                "Nên sử dụng tai nghe khi học để tạo không gian tập trung riêng biệt.",
                "Kết hợp sóng não 40Hz với các bài học khó giúp tăng tốc độ giải quyết vấn đề."
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
