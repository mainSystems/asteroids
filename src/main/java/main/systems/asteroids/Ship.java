package main.systems.asteroids;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Ship {
    private static Sprite spaceShip;

    public static void genShip() {
        spaceShip = new Sprite(CommonData.getSHIP_LIST().get(CommonData.genRandom(3)));
        spaceShip.position.set(CommonData.getSCREEN_X() / 8, CommonData.getSCREEN_Y() / 2);
    }

    public static Sprite getSpaceShip() {
        return spaceShip;
    }

    public static int getSpaceShipSpeed() {
        return CommonData.getSpaceShipSpeed();
    }

    public static int getSpaceShipRotation() {
        return CommonData.getSpaceShipRotation();
    }
}
