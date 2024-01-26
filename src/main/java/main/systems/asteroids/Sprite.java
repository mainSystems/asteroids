package main.systems.asteroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    public Vector position;
    public Vector velocity;
    public double rotation;
    private Rectangle boundary;
    private Image image;
    private double elapsedTime;
    private int collisionCount;
    private String imageFile;

    public Sprite() {
        position = new Vector();
        velocity = new Vector();
        rotation = 0;
        boundary = new Rectangle();
        elapsedTime = 0;
        collisionCount = 0;
    }

    public Sprite(String imageFilename) {
        this();
        setImage(imageFilename);
    }

    public Sprite(String imageFileName, double width, double height) {
        this();
        resizeImage(imageFileName, width, height);
    }

    public void resizeImage(String imageFileName, double width, double height) {
        imageFile = imageFileName;
        image = new Image(imageFileName, width, height, false, false);
        boundary.setSize(image.getWidth(), image.getHeight());
    }

    public Rectangle getBoundary() {
        boundary.setPosition(position.getX(), position.getY());
        return boundary;
    }

    public boolean overlaps(Sprite other) {
        return getBoundary().overlaps(other.getBoundary());
    }

    public void wrap(double screenWidth, double screenHeight) {
        double halfWidth = image.getWidth() / 2;
        double halfHeight = image.getHeight() / 2;

        if (position.getX() + halfWidth < 0) {
            position.setX(screenWidth + halfWidth);
        }
        if (position.getX() > screenWidth + halfWidth) {
            position.setX(-halfWidth);
        }
        if (position.getY() + halfHeight < 0) {
            position.setY(screenHeight + halfHeight);
        }
        if (position.getY() > screenHeight + halfHeight) {
            position.setY(-halfHeight);
        }
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        position.add(velocity.getX() * deltaTime, velocity.getY() * deltaTime);
        wrap(Main.SCREEN_X, Main.SCREEN_Y);
    }

    public void render(GraphicsContext context) {
        context.save();
        context.translate(position.getX(), position.getY());
        context.rotate(rotation);
        context.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        context.drawImage(image, 0, 0);
        context.restore();
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public void setCollisionCount(int collisionCount) {
        if (collisionCount < 0) {
            this.collisionCount--;
        } else {
            this.collisionCount += collisionCount;
        }
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String imageFileName) {
        imageFile = imageFileName;
        image = new Image(imageFileName);
        boundary.setSize(image.getWidth(), image.getHeight());
    }

    public String getImageFile() {
        return imageFile;
    }
}
