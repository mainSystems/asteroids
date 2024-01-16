package main.systems.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Asteroids extends Application {
    int score;

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
        mainStage.setTitle("Asteroids");

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        Canvas canvas = new Canvas(800, 600);
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


        Sprite background = new Sprite("img/bg1.jpg");
        background.position.set(400, 300);


        Sprite spaceShip = new Sprite("img/ship1.png");
        spaceShip.position.set(100, 300);

        ArrayList<Sprite> laserList = new ArrayList<>();
        ArrayList<Sprite> asteroidList = new ArrayList<>();

        int asteroidsCount = 6;
        score = 0;
        for (int i = 0; i < asteroidsCount; i++) {
            Sprite asteroid = new Sprite("img/ast1.png");
            double x = 500 * Math.random() + 300;
            double y = 400 * Math.random() + 100;
            asteroid.position.set(x, y);
            double angle = 360 * Math.random();
            asteroid.velocity.setLength(10);
            asteroid.velocity.setAngle(angle);
            asteroidList.add(asteroid);
        }

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long nanotime) {
                if (keyPressedList.contains("LEFT")) {
                    spaceShip.rotation -= 3;
                }
                if (keyPressedList.contains("RIGHT")) {
                    spaceShip.rotation += 3;
                }
                if (keyPressedList.contains("UP")) {
                    spaceShip.velocity.setLength(50);
                    spaceShip.velocity.setAngle(spaceShip.rotation);
                } else {
                    spaceShip.velocity.setLength(50);
                }
                if (keyJustPressedList.contains("SPACE")) {
                    Sprite laser = new Sprite("img/laser.png");
                    laser.position.set(spaceShip.position.x, spaceShip.position.y);
                    laser.velocity.setLength(400);
                    laser.velocity.setAngle(spaceShip.rotation);
                    laserList.add(laser);
                }
                keyJustPressedList.clear();

                spaceShip.update(1 / 60.0);

                for (Sprite asteroid : asteroidList) {
                    asteroid.update(1 / 60.0);
                }

                for (int n = 0; n < laserList.size(); n++) {
                    Sprite laser = laserList.get(n);
                    laser.update(1 / 60.0);
                    if (laser.elapsedTime > 2) {
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
                            score ++;
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
            }
        };

        gameLoop.start();
        mainStage.show();
    }
}
