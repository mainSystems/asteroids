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

public class Main extends Application {
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
        mainStage.setTitle(CommonData.getTITLE());

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        Canvas canvas = new Canvas(CommonData.getSCREEN_X(), CommonData.getSCREEN_Y());
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
        background = new Sprite(CommonData.getBACKGROUND_LIST().get(CommonData.genRandom(3)));
        background.position.set(CommonData.getSCREEN_X() / 2, CommonData.getSCREEN_Y() / 2);

        Ship.genShip();

        engine = new Sprite(CommonData.getENGINE_LIST());

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
        context.fillText(text, CommonData.getSCREEN_X(), -135, CommonData.getSCREEN_Y() - 80);
        context.strokeText(text, CommonData.getSCREEN_X() - 135, CommonData.getSCREEN_Y() - 80);
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
                        double newPositionX = asteroid.position.getX() + Math.random() * 3;
                        double newPositionY = asteroid.position.getY() + Math.random() * 1;
                        double newWidth = asteroid.getBoundary().getWidth() / 2;
                        double newHeight = asteroid.getBoundary().getHeight() / 2;
                        for (int i = 0; i < CommonData.genRandom(CommonData.getAsteroidsCountMin()); i++) {
                            Asteroid.generateAsteroidFragment(asteroidImg, newPositionX, newPositionY, newWidth, newHeight);
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
        Ship.getSpaceShip().update(CommonData.getDELTA_TIME());

        for (Sprite asteroid : Asteroid.getAsteroidList()) {
            asteroid.update(CommonData.getDELTA_TIME());
        }
        for (int laserNum = 0; laserNum < Weapon.getLaserList().size(); laserNum++) {
            Sprite laser = Weapon.getLaserList().get(laserNum);
            laser.update(CommonData.getDELTA_TIME());
            if (laser.getElapsedTime() > CommonData.getELAPSED_TIME()) {
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
}
