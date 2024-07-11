package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 */
class TriangleTest {

    private final Triangle triangle = new Triangle(
            new Point(0, 0, 0),
            new Point(1, 0, 0),
            new Point(0, 1, 0)
    );

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {

        // ============ Equivalence Partitions Tests ==============
        //TC01:Test normal calculation at one of the vertices of the triangle
        assertEquals(
                new Vector(0, 0, 1),
                triangle.getNormal(new Point(0, 0, 0)),
                "ERROR: getNormal() wrong result"
        );
    }

    @Test
    void testFindIntersections() {
        //Points to help with calculations
        final Point pOutside1 = new Point(0.2, 0.2, -1);
        final Point pOutside2 = new Point(1.5, 0.5, -1);
        final Point pOutside3 = new Point(-0.5, -0.5, -1);
        final Point pOutside4 = new Point(0.5, 0, -1);
        final Point pOutside5 = new Point(1, 0, -1);
        final Point pOutside6 = new Point(1.5, 0, -1);
        final Point pInside1 = new Point(0.2, 0.2, 0);

        //Direction vectors to help with calculations
        final Vector v001 = new Vector(0, 0, 1);


        // ============ Equivalence Partitions Tests ==============
        // TC01:Inside the Triangle
        final var result1 = triangle.findIntersections(new Ray(pOutside1, v001));
        assertNotNull(
                result1,
                "Ray crosses triangle but result is null"
        );
        assertEquals(
                1,
                result1.size(),
                "ERROR: Wrong number of points"
        );
        assertEquals(
                List.of(pInside1),
                result1,
                "ERROR: Wrong intersection point"
        );

        //TC02:Outside the Triangle (Next to Edge )
        final var result2 = triangle.findIntersections(new Ray(pOutside2, v001));
        assertNull(
                result2,
                "TC02 ERROR: Ray's line out of triangle"
        );

        //TC03: Outside the Triangle (Next to Vertex):
        final var result3 = triangle.findIntersections(new Ray(pOutside3, v001));
        assertNull(
                result3,
                "TC03 ERROR: Ray's line out of triangle"
        );

        // =============== Boundary Values Tests ==================
        // TC11: On the Edge
        final var result11 = triangle.findIntersections(new Ray(pOutside4, v001));
        assertNull(
                result11,
                "TC11 ERROR: Ray's line out of triangle"
        );

        // TC12: On the Vertex
        final var result12 = triangle.findIntersections(new Ray(pOutside5, v001));
        assertNull(
                result12,
                "TC12 ERROR: Ray's line out of triangle"
        );

        // TC13: On the Boundary (Edge)
        final var result13 = triangle.findIntersections(new Ray(pOutside6, v001));
        assertNull(
                result13,
                "TC13 ERROR: Ray's line out of triangle"
        );
    }
}