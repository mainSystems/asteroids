package main.systems.asteroids;

import java.util.ArrayList;
import java.util.Random;

public class Weapon {
    private static final String LASER = "img/laser.png";
    private static final int LASER_SPEED = 1600;
    private static ArrayList<Sprite> laserList = new ArrayList<>();
    private static Sprite laser;

    public static void genLaser() {
        laser = new Sprite(LASER);
        laser.position.set(Ship.getSpaceShip().position.getX(), Ship.getSpaceShip().position.getY());
        laser.velocity.setAngle(Ship.getSpaceShip().rotation);
        laser.velocity.setLength(LASER_SPEED);
        laserList.add(laser);
    }

    public static ArrayList<Sprite> getLaserList() {
        return laserList;
    }

    public static void removeLaser(int index) {
        laserList.remove(index);
    }

    private static int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
