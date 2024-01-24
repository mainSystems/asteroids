package main.systems.asteroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    public Vector position;
    public Vector velocity;
    public double rotation;
    public Rectangle boundary;
    public Image image;
    public double elapsedTime;
    public int collisionCount;

    public Sprite() {
        position = new Vector();
        velocity = new Vector();
        rotation = 0;
        boundary = new Rectangle();
        elapsedTime = 0;
        collisionCount = 99;
    }

    public Sprite(String imageFilename) {
        this();
        setImage(imageFilename);
    }

    public void setImage(String imageFileName) {
        image = new Image(imageFileName);
        boundary.setSize(image.getWidth(), image.getHeight());
    }

    public Rectangle getBoundary() {
        boundary.setPosition(position.x, position.y);
        return boundary;
    }

    public boolean overlaps(Sprite other) {
        return getBoundary().overlaps(other.getBoundary());
    }

    public void wrap(double screenWidth, double screenHeight) {
        double halfWidth = image.getWidth() / 2;
        double halfHeight = image.getHeight() / 2;

        if (position.x + halfWidth < 0) {
            position.x = screenWidth + halfWidth;
        }
        if (position.x > screenWidth + halfWidth) {
            position.x = -halfWidth;
        }
        if (position.y + halfHeight < 0) {
            position.y = screenHeight + halfHeight;
        }
        if (position.y > screenHeight + halfHeight) {
            position.y = -halfHeight;
        }
    }

    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        wrap(Asteroids.SCREEN_X, Asteroids.SCREEN_Y);
    }

    public void render(GraphicsContext context) {
        context.save();
        context.translate(position.x, position.y);
        context.rotate(rotation);
        context.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        context.drawImage(image, 0, 0);
        context.restore();
    }
}
