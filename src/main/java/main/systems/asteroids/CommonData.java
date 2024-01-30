package main.systems.asteroids;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CommonData {
    private static final double SCREEN_X = 1280, SCREEN_Y = 1024;
    private static final List<String> BACKGROUND_LIST = Arrays.asList("img/background/bg1.jpg", "img/background/bg2.jpg", "img/background/bg3.jpg");
    private static final List<String> ASTEROID_LIST = Arrays.asList("img/asteroids/ast1.png", "img/asteroids/ast2.png", "img/asteroids/ast3.png");
    private static final List<String> SHIP_LIST = Arrays.asList("img/ship/ship1.png", "img/ship/ship2.png", "img/ship/ship3.png");
    private static final String TITLE = "Asteroids";
    private static final String ENGINE_LIST = "img/ship/engine/fire1.jpg";
    private static final double DELTA_TIME = 1 / 60.0,  ELAPSED_TIME = 0.8;
    private static final int ASTEROID_SPEED = 30;
    private static final int asteroidsCount = 16;
    private static final int asteroidsCountMin = 3;
    private static final String LASER = "img/laser.png";
    private static final int LASER_SPEED = 1600;
    private static final int SPACE_SHIP_SPEED = 400;
    private static final int SPACE_SHIP_ROTATION = 3;
    public static double getSCREEN_X() {
        return SCREEN_X;
    }

    public static double getSCREEN_Y() {
        return SCREEN_Y;
    }

    public static double getDELTA_TIME() {
        return DELTA_TIME;
    }

    public static double getELAPSED_TIME() {
        return ELAPSED_TIME;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static String getENGINE_LIST() {
        return ENGINE_LIST;
    }

    public static List<String> getBACKGROUND_LIST() {
        return BACKGROUND_LIST;
    }

    public static int getAsteroidsCount() {
        return asteroidsCount;
    }

    public static int getASTEROID_SPEED() {
        return ASTEROID_SPEED;
    }

    public static List<String> getASTEROID_LIST() {
        return ASTEROID_LIST;
    }

    public static int getAsteroidsCountMin() {
        return asteroidsCountMin;
    }

    public static String getLASER() {
        return LASER;
    }

    public static int getLASER_SPEED() {
        return LASER_SPEED;
    }

    public static int getSpaceShipSpeed() {
        return SPACE_SHIP_SPEED;
    }

    public static int getSpaceShipRotation() {
        return SPACE_SHIP_ROTATION;
    }

    public static List<String> getSHIP_LIST() {
        return SHIP_LIST;
    }

    public static int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
