package geometries;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 */
class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {

        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // ============ Equivalence Partitions Tests ==============
        /*
         *TC01:Test normal calculation at one of the vertices of the triangle
         */
        assertEquals(
                new Vector(0, 0, 1),
                triangle.getNormal(new Point(0, 0, 0)),
                "ERROR: getNormal() wrong result"
        );
    }
}