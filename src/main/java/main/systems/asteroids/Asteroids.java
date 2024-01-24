package main.systems.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Asteroids extends Application {
    public static final double SCREEN_X = 1280, SCREEN_Y = 1024;
    public static final int asteroidsCountMin = 3;
    private static final double DELTA_TIME = 1 / 60.0;
    private static final double ELAPSED_TIME = 0.8;
    private static final String TITLE = "Asteroids";
    private static final int asteroidsCount = 16;
    private static final List<String> ASTEROID_LIST = Arrays.asList("img/asteroids/ast1.png", "img/asteroids/ast2.png", "img/asteroids/ast3.png");
    private static final List<String> SHIP_LIST = Arrays.asList("img/ship/ship1.png", "img/ship/ship2.png", "img/ship/ship3.png");
    private static final List<String> BACKGROUND_LIST = Arrays.asList("img/background/bg1.jpg", "img/background/bg2.jpg", "img/background/bg3.jpg");
    private static final String LASER = "img/laser.png";
    private static final int SPACE_SHIP_SPEED = 400;
    private static final int SPACE_SHIP_ROTATION = 3;
    private static final int LASER_SPEED = 1600;
    private static final int ASTEROID_SPEED = 30;
    private static final ArrayList<String> keyPressedList = new ArrayList<>();
    //    private static final ArrayList<String> keyJustPressedList = new ArrayList<>();
    private static final ArrayList<Sprite> laserList = new ArrayList<>();
    private static final ArrayList<Sprite> asteroidList = new ArrayList<>();
    private GraphicsContext context;
    private Sprite background;
    private Sprite spaceShip;
    private Sprite asteroid;
    private Sprite laser;
    private int score = 0;

    public static void main(String[] args) {
        Thread Mplayer = new Thread(() -> Mplayer("C:\\java\\asteroids\\src\\main\\resources\\music\\DRIVE.mp3"));
        Mplayer.start();

        launch(args);

    }

    private static void Mplayer(String soundFile) {
        try {
            Player playMP3 = new Player(new FileInputStream(soundFile));
            playMP3.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle(TITLE);

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        Canvas canvas = new Canvas(SCREEN_X, SCREEN_Y);
        context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        mainScene.setOnKeyPressed(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();

                    if (!keyPressedList.contains(keyName)) {
                        keyPressedList.add(keyName);
//                        keyJustPressedList.add(keyName);
                    }
                }
        );

        mainScene.setOnKeyReleased(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();
                    keyPressedList.remove(keyName);
                }
        );

        generateObject();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long nanotime) {
                handleKey();
                moving();
                collisions();
                render();
            }
        };

        gameLoop.start();
        mainStage.show();
    }

    private void generateObject() {
        background = new Sprite(BACKGROUND_LIST.get(genRandom(3)));
        background.position.set(SCREEN_X / 2, SCREEN_Y / 2);


        spaceShip = new Sprite(SHIP_LIST.get(genRandom(3)));
        spaceShip.position.set(SCREEN_X / 8, SCREEN_Y / 2);

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

    private void render() {
        background.render(context);
        spaceShip.render(context);
        for (Sprite laser : laserList) {
            laser.render(context);
        }
        for (Sprite asteroid : asteroidList) {
            asteroid.render(context);
        }
        context.setFill(Color.WHITE);
        context.setStroke(Color.GREEN);
        context.setFont(new Font("Hack", 18));
        context.setLineWidth(0);
        String text = String.format("Score: %s\nLives: %s", score, spaceShip.getCollisionCount());
        context.fillText(text, SCREEN_X - 100, SCREEN_Y - 100);
        context.strokeText(text, SCREEN_X - 100, SCREEN_Y - 100);
    }

    private void collisions() {
        for (int laserNum = 0; laserNum < laserList.size(); laserNum++) {
            laser = laserList.get(laserNum);
            for (int asteroidsNum = 0; asteroidsNum < asteroidList.size(); asteroidsNum++) {
                asteroid = asteroidList.get(asteroidsNum);
                if (laser.overlaps(asteroid)) {
                    laserList.remove(laserNum);
                    if (asteroid.getCollisionCount() > 0) {
                        for (int i = 0; i < genRandom(3) + asteroidsCountMin; i++) {
                            generateAsteroidFragment();
                        }
                    }
                    asteroidList.remove(asteroidsNum);
                    score++;
                }
            }
        }

        for (Sprite asteroid : asteroidList) {
            if (asteroid.overlaps(spaceShip)) {
                System.out.println("ship broken");
                System.out.println("spaceShip.collisionCount = " + spaceShip.getCollisionCount());
                spaceShip.setCollisionCount(-1);
            }
        }
    }

    private void generateAsteroidFragment() {
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

    private void moving() {
        spaceShip.update(DELTA_TIME);

        for (Sprite asteroid : asteroidList) {
            asteroid.update(DELTA_TIME);
        }
        for (int n = 0; n < laserList.size(); n++) {
            laser = laserList.get(n);
            laser.update(DELTA_TIME);
            if (laser.getElapsedTime() > ELAPSED_TIME) {
                laserList.remove(n);
            }
        }
    }

    private void handleKey() {
        for (String key : keyPressedList) {
            switch (key) {
                case "LEFT" -> spaceShip.rotation -= SPACE_SHIP_ROTATION;
                case "RIGHT" -> spaceShip.rotation += SPACE_SHIP_ROTATION;
                case "UP" -> {
                    spaceShip.velocity.setAngle(spaceShip.rotation);
                    spaceShip.velocity.setLength(SPACE_SHIP_SPEED);
                }
                case "DOWN" -> spaceShip.velocity.setLength(-5.00);
                case "SPACE" -> {
                    laser = new Sprite(LASER);
                    laser.position.set(spaceShip.position.getX(), spaceShip.position.getY());
                    laser.velocity.setAngle(spaceShip.rotation);
                    laser.velocity.setLength(LASER_SPEED);
                    laserList.add(laser);
                }
                default -> spaceShip.velocity.setLength(0);
            }
        }
//        if (keyJustPressedList.contains("SPACE")) {
//            laser = new Sprite(LASER);
//            laser.position.set(spaceShip.position.x, spaceShip.position.y);
//            laser.velocity.setAngle(spaceShip.rotation);
//            laser.velocity.setLength(LASER_SPEED);
//            laserList.add(laser);
//        }
//        keyJustPressedList.clear();
    }

    private int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
