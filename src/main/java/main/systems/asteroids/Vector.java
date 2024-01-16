package main.systems.asteroids;

public class Vector {
    public double x;
    public double y;

    public Vector() {
        this.set(0, 0);
    }

    public Vector(double x, double y) {
        this.set(x, y);
    }

    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void multiply(double m) {
        this.x *= m;
        this.y *= m;
    }

    public double getLength() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public void setLength(double length) {
        double currentLength = this.getLength();

        if (currentLength == 0) {
            System.out.println("1L = " + length);
            this.set(length, 0);
        } else {
            System.out.println("2L = " + length);
            this.multiply(1 / currentLength);
//            this.multiply(length);
            this.multiply(50);
        }
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(this.y, this.x));
    }

    public void setAngle(double angleDegrees) {
        double L = this.getLength();
        double angleRadians = Math.toRadians(angleDegrees);
        this.x = Math.cos(angleRadians);
        this.y = Math.sin(angleRadians);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
