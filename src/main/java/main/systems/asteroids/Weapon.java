package main.systems.asteroids;

import java.util.ArrayList;

public class Weapon {
    private static ArrayList<Sprite> laserList = new ArrayList<>();
    private static Sprite laser;

    public static void genLaser() {
        laser = new Sprite(CommonData.getLASER());
        laser.position.set(Ship.getSpaceShip().position.getX(), Ship.getSpaceShip().position.getY());
        laser.velocity.setAngle(Ship.getSpaceShip().rotation);
        laser.velocity.setLength(CommonData.getLASER_SPEED());
        laserList.add(laser);
    }

    public static ArrayList<Sprite> getLaserList() {
        return laserList;
    }

    public static void removeLaser(int index) {
        laserList.remove(index);
    }
}
