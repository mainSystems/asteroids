package main.systems.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Asteroids extends Application {
    private static final double DELTA_TIME = 1 / 60.0;
    private static final int ELAPSED_TIME = 2;
    private static final int SCREEN_X = 800;
    private static final int SCREEN_Y = 600;
    private static final String TITLE = "Asteroids";
    private static final int asteroidsCount = 6;
    private static final String BACKGROUND_1 = "img/bg1.jpg";
    private static final String SHIP_1 = "img/ship1.png";
    private static final String ASTEROID_1 = "img/ast1.png";
    private static final String LASER = "img/laser.png";
    private static final int SPACE_SHIP_SPEED = 100;
    private static final int SPACE_SHIP_ROTATION = 3;
    private static final int LASER_SPEED = 400;
    private static final int ASTEROID_SPEED = 10;
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
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);


        ArrayList<String> keyPressedList = new ArrayList<>();
        ArrayList<String> keyJustPressedList = new ArrayList<>();

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


        Sprite background = new Sprite(BACKGROUND_1);
        background.position.set(SCREEN_X / 2, SCREEN_Y / 2);


        Sprite spaceShip = new Sprite(SHIP_1);
        spaceShip.position.set(SCREEN_X / 8, SCREEN_Y / 2);

        ArrayList<Sprite> laserList = new ArrayList<>();
        ArrayList<Sprite> asteroidList = new ArrayList<>();

        for (int i = 0; i < asteroidsCount; i++) {
            Sprite asteroid = new Sprite(ASTEROID_1);
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
                for (String key : keyPressedList) {
                    switch (key) {
                        case "LEFT":
                            spaceShip.rotation -= SPACE_SHIP_ROTATION;
                            break;
                        case "RIGHT":
                            spaceShip.rotation += SPACE_SHIP_ROTATION;
                            break;
                        case "UP":
                            spaceShip.velocity.setAngle(spaceShip.rotation);
                            spaceShip.velocity.setLength(SPACE_SHIP_SPEED);
                            break;
                        default:
                            spaceShip.velocity.setLength(0);
                            break;
                    }
                }
                if (keyJustPressedList.contains("SPACE")) {
                    Sprite laser = new Sprite(LASER);
                    laser.position.set(spaceShip.position.x, spaceShip.position.y);
                    laser.velocity.setAngle(spaceShip.rotation);
                    laser.velocity.setLength(LASER_SPEED);
                    laserList.add(laser);
                }
                keyJustPressedList.clear();


                spaceShip.update(DELTA_TIME);

                for (Sprite asteroid : asteroidList) {
                    asteroid.update(DELTA_TIME);
                }

                for (int n = 0; n < laserList.size(); n++) {
                    Sprite laser = laserList.get(n);
                    laser.update(DELTA_TIME);
                    if (laser.elapsedTime > ELAPSED_TIME) {
                        laserList.remove(n);
                    }
                }

                for (int laserNum = 0; laserNum < laserList.size(); laserNum++) {
                    Sprite laser = laserList.get(laserNum);
                    for (int asteroidsNum = 0; asteroidsNum < asteroidList.size(); asteroidsNum++) {
                        Sprite asteroid = asteroidList.get(asteroidsNum);
                        if (laser.overlaps(asteroid)) {
                            laserList.remove(laserNum);
                            asteroidList.remove(asteroidsNum);
                            score++;
                        }
                    }
                }


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
                context.setLineWidth(3);
                String text = "Score: " + score;
                int textX = 700;
                int textY = 15;
                context.fillText(text, textX, textY);
                context.strokeText(text, textX, textY);

            }
        };

        gameLoop.start();
        mainStage.show();
    }
}
