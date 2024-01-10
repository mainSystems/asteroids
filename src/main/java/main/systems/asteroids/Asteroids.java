package main.systems.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Asteroids extends Application {
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
        mainScene.setOnKeyPressed(
                (KeyEvent event) -> {
                    String keyName = event.getCode().toString();

                    if (!keyPressedList.contains(keyName)) {
                        keyPressedList.add(keyName);
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
        //spaceShip.velocity.set(50,0);
        //spaceShip.rotation = 90;


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
                    spaceShip.velocity.setLength(0);
                }

                spaceShip.update(1 / 60.0);
                background.render(context);
                spaceShip.render(context);
            }
        };

        gameLoop.start();
        mainStage.show();
    }
}
