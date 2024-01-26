package main.systems.asteroids;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Ship {
    private static final double SCREEN_X = 1280, SCREEN_Y = 1024;
    private static final List<String> SHIP_LIST = Arrays.asList("img/ship/ship1.png", "img/ship/ship2.png", "img/ship/ship3.png");
    private static int SPACE_SHIP_SPEED = 400;
    private static int SPACE_SHIP_ROTATION = 3;
    private static Sprite spaceShip;

    public static void genShip() {
        spaceShip = new Sprite(SHIP_LIST.get(genRandom(3)));
        spaceShip.position.set(SCREEN_X / 8, SCREEN_Y / 2);
    }

    public static Sprite getSpaceShip() {
        return spaceShip;
    }

    public static int getSpaceShipSpeed() {
        return SPACE_SHIP_SPEED;
    }

    public static int getSpaceShipRotation() {
        return SPACE_SHIP_ROTATION;
    }

    private static int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
