package company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Map<String, AudioInputStream> allSound = new HashMap<>();

    public SoundManager(){


    }

    public static   synchronized void playSound(final String url) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream( "/audio/" + url));
                clip.open(inputStream);
                clip.start();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }


}
