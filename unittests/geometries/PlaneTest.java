package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    private final double DELTA = 0.000001;

    @Test
    void testConstructor() {

    }

    @Test
    void testGetNormal() {
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);
        Plane plane = new Plane(p1, p2, p3);

        assertEquals(
                1,
                plane.getNormal().length() ,
                DELTA,
                "Normal vector should have length 1"
        );
    }

    @Test
    void testTestGetNormal() {
    }
}