package main.systems.asteroids;

public class Vector {
    private double x;
    private double y;

    public Vector() {
        set(0, 0);
    }

    public void add(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void multiply(double m) {
        x *= m;
        y *= m;
    }

    public void divide(double d) {
        x /= d;
        y /= d;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void setLength(double length) {
        double currentLength = getLength();

        if (length >= 0) {
//            if (currentLength == 0) {
//                set(length, 0);
//            } else {
//                multiply(1 / currentLength);
            multiply(length);
//            }
        } else {
            divide(Math.abs(length));
        }
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    public void setAngle(double angleDegrees) {
        double L = getLength();
        double angleRadians = Math.toRadians(angleDegrees);
        x = Math.cos(angleRadians);
        y = Math.sin(angleRadians);
    }

    public void set(double dx, double dy) {
        x = dx;
        y = dy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
