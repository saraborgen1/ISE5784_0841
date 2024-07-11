package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for primitives.Point class
 */
class PointTest {
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);
        Vector v1 = new Vector(-1, -2, -3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test subtracting two points, should result in the correct vector
        assertEquals(
                v1,
                p1.subtract(p2),
                "ERROR: (point1 - point2) does not work correctly"
        );

        // =============== Boundary Values Tests ==================
        //TC01:Test subtracting a point from itself, should throw IllegalArgumentException
        assertThrows(
                IllegalArgumentException.class,
                () -> p1.subtract(p1),
                "ERROR: (point - itself) does not throw an exception"
        );
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test adding a vector to a point, should result in the correct point
        assertEquals(
                new Vector(2, 4, 6),
                p1.add(v1),
                "ERROR: (point + vector) = other point does not work correctly"
        );

        //TC02: Test adding a negative vector to a point, should result in the zero point
        assertEquals(
                Point.ZERO,
                p1.add(new Vector(-1, -2, -3)),
                "ERROR: (point + vector) = center of coordinates does not work correctly"
        );
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test squared distance of a point to itself, should be zero
        assertEquals(
                0,
                p1.distanceSquared(p1),
                "ERROR: point squared distance to itself is not zero"
        );

        //TC02: Test squared distance between two points, should result in the correct value
        assertEquals(
                9,
                p1.distanceSquared(p2),
                "ERROR: squared distance between points is wrong"
        );

        //TC03:Test squared distance between two points in reverse order, should result in the correct value
        assertEquals(
                9,
                p2.distanceSquared(p1),
                "ERROR: squared distance between points is wrong"
        );
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test distance of a point to itself, should be zero
        assertEquals(
                0,
                p1.distance(p1),
                "ERROR: point squared distance to itself is not zero"
        );

        //TC02: Test distance between two points, should result in the correct value
        assertEquals(
                3,
                p1.distance(p2),
                "ERROR: distance between points to itself is wrong"
        );

        //TC03: Test distance between two points in reverse order, should result in the correct value
        assertEquals(
                3,
                p2.distance(p1),
                "ERROR: distance between points to itself is wrong"
        );
    }
}