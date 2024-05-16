package geometries;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    private final double DELTA = 0.000001;

    @Test
    public void testGetNormal() {

        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p1, p2, p3,,3);

        // בדיקת גודל הנורמל
        assertEquals(1, triangle.getNormal().length(),DELTA);
    }

}