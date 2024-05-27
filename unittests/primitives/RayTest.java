package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {
    /**
     *Tests the getDirection method of the Ray class.
     */
    @Test
    void testGetDirection() {
        Ray ray = new Ray(new Point(1,1,1),new Vector(2,2,2));
        assertEquals(ray.getHead(), new Point(1,1,1), "does not return the correct point");
    }

    /**
     *Tests the getHead method of the Ray class.
     */
    @Test
    void testGetHead() {
        Ray ray = new Ray(new Point(1,1,1),new Vector(2,2,2));
        assertEquals(ray.getDirection(), new Vector(2,2,2).normalize(), "does not return the correct direction");
    }

    /**
     *Tests the getPoint method of the Ray class.
     */
    @Test
    void testGetPoint() {
        Point head=Point.ZERO;
        Vector direction=new Vector(1,0,0);
        Ray ray=new Ray(head,direction);

        // ============ Equivalence Partition Test ==============
        // TC01: t is positive.
        double t = 1;
        assertEquals(new Point(1,0,0), ray.getPoint(t), "The correct point is (1,0,0)");

        // TC02: t is negative (-1).
        t = -1;
        assertEquals(new Point(-1,0,0), ray.getPoint(t), "The correct point is (-1,0,0)");

        // =============== Boundary Values Tests ==================
        // TC11: t is zero.
        t = 0;
        assertEquals(Point.ZERO, ray.getPoint(t), "The correct point is (0,0,0)");
    }
}