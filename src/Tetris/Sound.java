package Tetris;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public Clip music_clip;

    public Sound() {
    }

    /**
     * Phát âm thanh từ file tương ứng với chỉ số `i` (mở rộng nếu cần nhiều âm
     * thanh)
     * 
     * @param i     - chỉ số âm thanh (hiện tại chỉ có 1 file)
     * @param music - true nếu muốn phát lặp lại
     */
    public void play(int i, boolean music) {
        String soundPath = "assets/ding-1-14705.wav";

        try {
            // Đóng clip cũ nếu đang chạy
            if (music_clip != null && music_clip.isRunning()) {
                music_clip.stop();
                music_clip.close();
            }

            File soundFile = new File(soundPath);
            if (!soundFile.exists()) {
                System.err.println("File không tồn tại: " + soundPath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            music_clip = AudioSystem.getClip();
            music_clip.open(audioStream);

            if (music) {
                music_clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                music_clip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phát lặp lại clip hiện tại (nếu đã được mở)
     */
    public void loop() {
        if (music_clip != null && music_clip.isOpen()) {
            music_clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Dừng phát âm thanh hiện tại
     */
    public void stop() {
        if (music_clip != null && music_clip.isRunning()) {
            music_clip.stop();
        }
    }
}