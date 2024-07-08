package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {
    /**
     * Test method for {@link Ray#getDirection()}.
     */
    @Test
    void testGetDirection() {
        Ray ray = new Ray(new Point(1, 1, 1), new Vector(2, 2, 2));
        assertEquals(
                ray.getHead(),
                new Point(1, 1, 1),
                "does not return the correct point"
        );
    }

    /**
     * Test method for {@link Ray#getHead()}.
     */
    @Test
    void testGetHead() {
        Ray ray = new Ray(new Point(1, 1, 1), new Vector(2, 2, 2));
        assertEquals(
                ray.getDirection(),
                new Vector(2, 2, 2).normalize(),
                "does not return the correct direction"
        );
    }

    /**
     * Test method for {@link Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        Point head = Point.ZERO;
        Vector direction = new Vector(1, 0, 0);
        Ray ray = new Ray(head, direction);

        // ============ Equivalence Partition Test ==============
        // TC01: t is positive.
        double t = 1;
        assertEquals(
                new Point(1, 0, 0),
                ray.getPoint(t),
                "The correct point is (1,0,0)"
        );

        // TC02: t is negative (-1).
        t = -1;
        assertEquals(
                new Point(-1, 0, 0),
                ray.getPoint(t),
                "The correct point is (-1,0,0)"
        );

        // =============== Boundary Values Tests ==================
        // TC11: t is zero.
        t = 0;
        assertEquals(
                Point.ZERO,
                ray.getPoint(t),
                "The correct point is (0,0,0)"
        );
    }

    /**
     * Test method for {@link Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        Point point1 = new Point(1, 1, 0);
        Point point2 = new Point(2, 0, 0);
        Point point3 = new Point(3, 3, 0);
        Point point4 = new Point(1, 0, 0);
        List<Point> points = List.of(
                point2,
                point1,
                point3
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Closest point is in the middle of the list
        assertEquals(
                point1,
                ray.findClosestPoint(points),
                "Returned wrong result"
        );

        // ============ Boundary Values Tests ==============
        // TC11: list is empty (should return null)
        points = null;
        Point closestPoint = ray.findClosestPoint(points);
        assertNull(
                closestPoint,
                "Should have returned null"
        );

        // TC12: closest point is at start of list
         points = List.of(
                point4,
                point2,
                point3
        );
        assertEquals(
                point4,
                ray.findClosestPoint(points),
                "Returned wrong result"
        );

        // TC13: closest point is at end of list
        points = List.of(
                point2,
                point3,
                point4
        );
        assertEquals(
                point4,
                ray.findClosestPoint(points),
                "Returned wrong result"
        );
    }
}