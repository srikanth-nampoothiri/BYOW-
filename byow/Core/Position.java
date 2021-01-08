package byow.Core;

import java.io.Serializable;

/**
 * @Source Point class from Bearmaps
 */

public class Position implements Serializable {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public static double distance(Position p1, Position p2) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        double distance = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return distance;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPosition = (Position) other;
        return getX() == otherPosition.getX() && getY() == otherPosition.getY();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y);
    }

}
