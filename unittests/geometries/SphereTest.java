package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void testGetNormal() {
        Sphere sphere = new Sphere(Point.ZERO, 1);
        Point point = new Point(1, 0, 0);
        assertEquals(
                1,
                sphere.getNormal(point).length(),
                "Normal vector should have length 1"
        );
    }
}