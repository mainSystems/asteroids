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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Asteroids extends Application {
    public static final int SCREEN_X = 1280;
    public static final int SCREEN_Y = 1024;
    private static final double DELTA_TIME = 1 / 60.0;
    private static final int ELAPSED_TIME = 2;
    private static final String TITLE = "Asteroids";
    private static final int TEXT_X = 700, TEXT_Y = 15;
    private static final int asteroidsCount = 16;
    private static final List<String> ASTEROID_LIST = Arrays.asList("img/asteroids/ast1.png", "img/asteroids/ast2.png", "img/asteroids/ast3.png");
    private static final List<String> SHIP_LIST = Arrays.asList("img/ship/ship1.png", "img/ship/ship2.png", "img/ship/ship3.png");
    private static final List<String> BACKGROUND_LIST = Arrays.asList("img/background/bg1.jpg", "img/background/bg2.jpg", "img/background/bg3.jpg");
    private static final String LASER = "img/laser.png";
    private static final int SPACE_SHIP_SPEED = 400;
    private static final int SPACE_SHIP_ROTATION = 3;
    private static final int LASER_SPEED = 1600;
    private static final int ASTEROID_SPEED = 30;
    private GraphicsContext context;
    private ArrayList<String> keyPressedList = new ArrayList<>();
    private ArrayList<String> keyJustPressedList = new ArrayList<>();
    private ArrayList<Sprite> laserList = new ArrayList<>();
    private ArrayList<Sprite> asteroidList = new ArrayList<>();
    private Sprite background;
    private Sprite spaceShip;
    private Sprite asteroid;
    private Sprite laser;
    private int lives = 3;
    private int score = 0;

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
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
                        keyJustPressedList.add(keyName);
                    }
                }
        );

        mainScene.setOnKeyReleased(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();

                    if (keyPressedList.contains(keyName)) {
                        keyPressedList.remove(keyName);
                    }
                }
        );


        background = new Sprite(BACKGROUND_LIST.get(genRandom(3)));
        background.position.set(SCREEN_X / 2, SCREEN_Y / 2);


        spaceShip = new Sprite(SHIP_LIST.get(genRandom(3)));
        spaceShip.position.set(SCREEN_X / 8, SCREEN_Y / 2);

        for (int i = 0; i < genRandom(asteroidsCount); i++) {
            asteroid = new Sprite(ASTEROID_LIST.get(genRandom(3)));
            double x = 500 * Math.random() + 300;
            double y = 400 * Math.random() + 100;
            asteroid.position.set(x, y);
            double angle = 360 * Math.random();
            asteroid.velocity.setAngle(angle);
            asteroid.velocity.setLength(ASTEROID_SPEED);
            asteroidList.add(asteroid);
        }

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long nanotime) {
                handleKey();
                movings();
                collisions();
                render();
            }
        };

        gameLoop.start();
        mainStage.show();
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
        String text = String.format("Score: %s\nLives: %s", score, lives);
        context.fillText(text, TEXT_X, TEXT_Y);
        context.strokeText(text, TEXT_X, TEXT_Y);
    }

    private void collisions() {
        for (int laserNum = 0; laserNum < laserList.size(); laserNum++) {
            laser = laserList.get(laserNum);
            for (int asteroidsNum = 0; asteroidsNum < asteroidList.size(); asteroidsNum++) {
                asteroid = asteroidList.get(asteroidsNum);
                if (laser.overlaps(asteroid)) {
                    laserList.remove(laserNum);
                    asteroidList.remove(asteroidsNum);
                    score++;
                }
            }
        }

        for (Sprite asteroid : asteroidList) {
            if (asteroid.overlaps(spaceShip)) {
                System.out.println("ship broken");
                lives--;
            }
        }
    }

    private void movings() {
        spaceShip.update(DELTA_TIME);

        for (Sprite asteroid : asteroidList) {
            asteroid.update(DELTA_TIME);
        }
        for (int n = 0; n < laserList.size(); n++) {
            laser = laserList.get(n);
            laser.update(DELTA_TIME);
            if (laser.elapsedTime > ELAPSED_TIME) {
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
                default -> spaceShip.velocity.setLength(0);
            }
        }
        if (keyJustPressedList.contains("SPACE")) {
            laser = new Sprite(LASER);
            laser.position.set(spaceShip.position.x, spaceShip.position.y);
            laser.velocity.setAngle(spaceShip.rotation);
            laser.velocity.setLength(LASER_SPEED);
            laserList.add(laser);
        }
        keyJustPressedList.clear();
    }

    private int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
