package main.systems.asteroids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Asteroid {
    private static final int asteroidsCount = 16;
    private static final int ASTEROID_SPEED = 30;
    private static final List<String> ASTEROID_LIST = Arrays.asList("img/asteroids/ast1.png", "img/asteroids/ast2.png", "img/asteroids/ast3.png");
    public static int asteroidsCountMin = 3;
    private static Sprite asteroid;
    private static ArrayList<Sprite> asteroidList = new ArrayList<>();

    public static void genAsteroid() {
        for (int i = 0; i < genRandom(asteroidsCount) + asteroidsCountMin; i++) {
            double x = 500 * Math.random() + 300;
            double y = 400 * Math.random() + 100;
            double angle = 360 * Math.random();
            asteroid = new Sprite(ASTEROID_LIST.get(genRandom(3)));
            asteroid.position.set(x, y);
            asteroid.velocity.setAngle(angle);
            asteroid.velocity.setLength(ASTEROID_SPEED);
            asteroid.setCollisionCount(genRandom(3));
            asteroidList.add(asteroid);
        }
    }

    public static void generateAsteroidFragment() {
        double newPositionX = asteroid.position.getX() + Math.random() + 30;
        double newPositionY = asteroid.position.getY() + Math.random() + 10;
        double angle = 360 * Math.random();
        double speed = 60 * Math.random() + 150;
        double newWidth = asteroid.getBoundary().getWidth() / 2;
        double newHeight = asteroid.getBoundary().getHeight() / 2;
        Sprite asteroidFragment = new Sprite(asteroid.getImageFile(), newWidth, newHeight);
        asteroidFragment.position.set(newPositionX, newPositionY);
        asteroidFragment.velocity.setAngle(angle);
        asteroidFragment.velocity.setLength(speed);
        asteroidFragment.setCollisionCount(asteroid.getCollisionCount() - 1);
        asteroidList.add(asteroidFragment);
    }

    public static ArrayList<Sprite> getAsteroidList() {
        return asteroidList;
    }

    public static int getAsteroidsCountMin() {
        return asteroidsCountMin;
    }

    public static void removeAsteroid(int index) {
        asteroidList.remove(index);
    }

    private static int genRandom(int max) {
        return new Random().nextInt(max);
    }
}