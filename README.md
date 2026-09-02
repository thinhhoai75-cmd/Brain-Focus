# Brain Focus 🧠 - Ứng Dụng Khắc Phục Não Bỏng Ngô & Luyện Tập Trung

**Brain Focus** là ứng dụng Android được phát triển bằng **Kotlin & Jetpack Compose (Material Design 3)**, ứng dụng các nghiên cứu khoa học thần kinh để giúp người học khắc phục hội chứng Não Bỏng Ngô (Popcorn Brain), nâng cao chỉ số tập trung **Brain Focus Score (BFS)** thông qua các chu kỳ học sâu 90 phút (Ultradian Rhythms) và rèn luyện vỏ não trước trán.

---

## 🌟 Tính Năng Nổi Bật

1. **Đánh Giá Chỉ Số BFS (Brain Focus Score)**:
   - Bộ 10 câu hỏi trắc nghiệm tâm lý - thần kinh đánh giá mức độ nhạy cảm xao nhãng.
   - Thang điểm chuẩn hóa từ 0 - 1000 điểm với phân loại mức độ và lộ trình rèn luyện cá nhân hóa.

2. **Phiên Học Sâu Chu Kỳ Sinh Học (Ultradian Rhythm 90 Phút & Pomodoro)**:
   - Đồng hồ đếm ngược trực quan với vòng tròn tiến độ động.
   - Bộ phát âm thanh sóng não chuyên dụng (Gamma 40Hz, White Noise, Mưa rơi, Lofi Synth).
   - Cơ chế phòng chống xao nhãng: Tự động phát hiện khi thoát app / mở mạng xã hội và áp dụng trừ điểm BFS / điểm xếp hạng.
   - Đúc kết & ghi chú cảm xúc sau mỗi buổi học để củng cố trí nhớ dài hạn (+thưởng BFS).

3. **Brain Focus Gym (3 Bài Tập Kích Thích Vỏ Não Trước Trán)**:
   - **Stroop Thần Kinh**: Kiểm soát và ức chế xung động tức thời.
   - **Ma Trận Ô Nhớ**: Mở rộng dung lượng trí nhớ làm việc (Working Memory).
   - **Phản Xạ Tính Nhẩm**: Đánh thức tốc độ xử lý của mạng lưới nơ-ron dưới áp lực 20 giây.

4. **Thư Viện Khoa Học & Mẹo Kỷ Luật**:
   - Các bài viết khoa học giải thích cơ chế Dopamine, chu kỳ Ultradian, kiến trúc giảm ma sát (Friction Architecture).
   - Kho mẹo học tập thực tế hỗ trợ đánh dấu yêu thích (Bookmark).

5. **Bảng Xếp Hạng Cộng Đồng & Thống Kê**:
   - Bục vinh danh Top 3 và bảng xếp hạng điểm tích lũy thi đua.
   - Quản lý lịch nhắc nhở rèn luyện kỷ luật.
   - Lưu trữ dữ liệu ngoại tuyến ổn định với **Room Database**.

---

## 🛠️ Kiến Trúc & Công Nghệ

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose, Material 3
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Local Persistence**: Android Room Database (KSP) + Flow
- **Audio Engine**: Android Sound Synthesis / MediaPlayer
- **State Management**: Kotlin Coroutines & `StateFlow`
