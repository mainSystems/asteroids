package main.systems.asteroids;

public class Rectangle {
    private double x;
    private double y;
    private double width;
    private double height;

    public Rectangle() {
        setPosition(0, 0);
        setSize(1, 1);
    }

    public Rectangle(double x, double y, double w, double h) {
        setPosition(x, y);
        setSize(w, h);
    }

    public void setPosition(double dx, double dy) {
        x = dx;
        y = dy;
    }

    public void setSize(double w, double h) {
        height = h;
        width = w;
    }

    public boolean overlaps(Rectangle other) {
        boolean noOverlaps =
                x + width < other.x ||
                        other.x + other.width < x ||
                        y + height < other.y ||
                        other.y + other.height < y;
        return !noOverlaps;
    }
}
