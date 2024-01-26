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

public class Main extends Application {
    public static final double SCREEN_X = 1280, SCREEN_Y = 1024;
    private static final double DELTA_TIME = 1 / 60.0;
    private static final double ELAPSED_TIME = 0.8;
    private static final String TITLE = "Asteroids";
    private static final String ENGINE_LIST = "img/ship/engine/fire1.jpg";
    private static final List<String> BACKGROUND_LIST = Arrays.asList("img/background/bg1.jpg", "img/background/bg2.jpg", "img/background/bg3.jpg");
    private static final ArrayList<String> keyPressedList = new ArrayList<>();
//    private static final ArrayList<String> keyJustPressedList = new ArrayList<>();
    private GraphicsContext context;
    private Sprite background;
    private Sprite engine;
    private int score = 0;

    public static void main(String[] args) {
        //Thread player = new Thread(() -> MPlayer.player("src/main/resources/music/DRIVE.mp3"));
        //player.start();

        launch(args);
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

        Ship.genShip();

        engine = new Sprite(ENGINE_LIST);

        Asteroid.genAsteroid();
    }

    private void render() {
        background.render(context);

        Ship.getSpaceShip().render(context);
        engine.render(context);
        for (Sprite laser : Weapon.getLaserList()) {
            laser.render(context);
        }
        for (Sprite asteroid : Asteroid.getAsteroidList()) {
            asteroid.render(context);
        }
        context.setFill(Color.WHITE);
        context.setStroke(Color.GREEN);
        context.setFont(new Font("Hack", 18));
        context.setLineWidth(0);
        String text = String.format("Score: %s\nLives: %s\nCPUInfo: %f", score, Ship.getSpaceShip().getCollisionCount(), Info.getUsageCpu());
        context.fillText(text, SCREEN_X - 100, SCREEN_Y - 100);
        context.strokeText(text, SCREEN_X - 100, SCREEN_Y - 100);
    }

    private void collisions() {
        for (int laserNum = 0; laserNum < Weapon.getLaserList().size(); laserNum++) {
            Sprite laser = Weapon.getLaserList().get(laserNum);
            for (int asteroidsNum = 0; asteroidsNum < Asteroid.getAsteroidList().size(); asteroidsNum++) {
                Sprite asteroid = Asteroid.getAsteroidList().get(asteroidsNum);
                if (laser.overlaps(asteroid)) {
                    Weapon.removeLaser(laserNum);
                    if (asteroid.getCollisionCount() > 0) {
                        String asteroidImg = asteroid.getImageFile();
                        double newPositionX = asteroid.position.getX() + Math.random()*3;
                        double newPositionY = asteroid.position.getY() + Math.random()*1;
                        double newWidth = asteroid.getBoundary().getWidth() / 2;
                        double newHeight = asteroid.getBoundary().getHeight() / 2;
                        for (int i = 0; i < genRandom(Asteroid.getAsteroidsCountMin()); i++) {
                            Asteroid.generateAsteroidFragment(asteroidImg, newPositionX, newPositionY,newWidth,newHeight);
                        }
                    }
                    Asteroid.removeAsteroid(asteroidsNum);
                    score++;
                }
            }
        }

        for (Sprite asteroid : Asteroid.getAsteroidList()) {
            if (asteroid.overlaps(Ship.getSpaceShip())) {
                System.out.println("ship broken");
                System.out.println("spaceShip.collisionCount = " + Ship.getSpaceShip().getCollisionCount());
                Ship.getSpaceShip().setCollisionCount(-1);
            }
        }
    }

    private void moving() {
        Ship.getSpaceShip().update(DELTA_TIME);

        for (Sprite asteroid : Asteroid.getAsteroidList()) {
            asteroid.update(DELTA_TIME);
        }
        for (int laserNum = 0; laserNum < Weapon.getLaserList().size(); laserNum++) {
            Sprite laser = Weapon.getLaserList().get(laserNum);
            laser.update(DELTA_TIME);
            if (laser.getElapsedTime() > ELAPSED_TIME) {
                Weapon.removeLaser(laserNum);
            }
        }
    }

    private void handleKey() {
        for (String key : keyPressedList) {
            switch (key) {
                case "LEFT" -> Ship.getSpaceShip().rotation -= Ship.getSpaceShipRotation();
                case "RIGHT" -> Ship.getSpaceShip().rotation += Ship.getSpaceShipRotation();
                case "UP" -> {
                    Ship.getSpaceShip().velocity.setAngle(Ship.getSpaceShip().rotation);
                    Ship.getSpaceShip().velocity.setLength(Ship.getSpaceShipSpeed());
//                    engine.position.set(spaceShip.position.getX() - 40, spaceShip.position.getY());
//                    engine.velocity.setAngle(spaceShip.rotation);
                }
                case "DOWN" -> Ship.getSpaceShip().velocity.setLength(-5.00);
                case "SPACE" -> Weapon.genLaser();
                default -> Ship.getSpaceShip().velocity.setLength(0);
            }
        }
//        if (keyJustPressedList.contains("SPACE")) {
//            Weapon.getLaserList();
//        }
//        keyJustPressedList.clear();
    }

    private int genRandom(int max) {
        return new Random().nextInt(max);
    }
}
