package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for primitives.Point class
 */
class PointTest {

    @Test
    void testSubtract() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);
        Vector v1= new Vector(-1, -2, -3);

        assertEquals(
                v1,
                p1.subtract(p2),
                "ERROR: (point1 - point2) does not work correctly"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> p1.subtract(p1),
                "ERROR: (point - itself) does not throw an exception"
        );
    }

    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        Vector v1 = new Vector(1, 2, 3);

        assertEquals(
                new Vector(2, 4, 6),
                p1.add(v1),
                "ERROR: (point + vector) = other point does not work correctly"
        );

        assertEquals(
                Point.ZERO,
                p1.add(new Vector(-1, -2, -3)),
                "ERROR: (point + vector) = center of coordinates does not work correctly"
        );
    }

    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        assertEquals(
                0,
                p1.distanceSquared(p1),
                "ERROR: point squared distance to itself is not zero"
        );

        assertEquals(
                9,
                p1.distanceSquared(p2),
                "ERROR: squared distance between points is wrong"
        );

        assertEquals(
                9,
                p2.distanceSquared(p1),
                "ERROR: squared distance between points is wrong"
        );
    }

    @Test
    void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        assertEquals(
                0,
                p1.distance(p1),
                "ERROR: point squared distance to itself is not zero"
        );

        assertEquals(
                3,
                p1.distance(p2),
                "ERROR: distance between points to itself is wrong"
        );

        assertEquals(
                3,
                p2.distance(p1),
                "ERROR: distance between points to itself is wrong"
        );
    }
}