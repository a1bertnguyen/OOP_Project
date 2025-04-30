import sound.Sound;

public class SoundTest {
    public static void main(String[] args) {
        Sound s = new Sound();

        // Phát âm thanh một lần
        s.play(0, false);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();  // Có thể thay bằng Logger nếu cần ghi log chuyên nghiệp
        }
    }
}