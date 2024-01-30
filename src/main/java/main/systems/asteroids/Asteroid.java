package main.systems.asteroids;

import java.util.ArrayList;

public class Asteroid {
    private static Sprite asteroid;
    private static ArrayList<Sprite> asteroidList = new ArrayList<>();

    public static void genAsteroid() {
        for (int i = 0; i < CommonData.genRandom(CommonData.getAsteroidsCount()) + CommonData.getAsteroidsCountMin(); i++) {
            double x = 500 * Math.random() + 300;
            double y = 400 * Math.random() + 100;
            double angle = 360 * Math.random();
            asteroid = new Sprite(CommonData.getASTEROID_LIST().get(CommonData.genRandom(3)));
            asteroid.position.set(x, y);
            asteroid.velocity.setAngle(angle);
            asteroid.velocity.setLength(CommonData.getASTEROID_SPEED());
            asteroid.setCollisionCount(CommonData.genRandom(3));
            asteroidList.add(asteroid);
        }
    }

    public static void generateAsteroidFragment(String asteroidImg, double newPositionX, double newPositionY, double newWidth, double newHeight) {
        double angle = 360 * Math.random();
        double speed = 60 * Math.random() + 150;
        Sprite asteroidFragment = new Sprite(asteroidImg, newWidth, newHeight);
        asteroidFragment.position.set(newPositionX, newPositionY);
        asteroidFragment.velocity.setAngle(angle);
        asteroidFragment.velocity.setLength(speed);
        asteroidFragment.setCollisionCount(asteroid.getCollisionCount() - 1);
        asteroidList.add(asteroidFragment);
    }

    public static ArrayList<Sprite> getAsteroidList() {
        return asteroidList;
    }

    public static void removeAsteroid(int index) {
        asteroidList.remove(index);
    }
}
