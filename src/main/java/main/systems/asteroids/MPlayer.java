package main.systems.asteroids;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MPlayer {
    public static void player(String soundFile) {
        try {
            Player playMP3 = new Player(new FileInputStream(soundFile));
            playMP3.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }

    }
}
