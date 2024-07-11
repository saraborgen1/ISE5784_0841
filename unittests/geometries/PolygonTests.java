package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 *
 * @author Dan
 */
public class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(0, 1, 0),
                        new Point(1, 0, 0),
                        new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices"
        );

        // TC03: Not in the same plane
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane"
        );

        // TC04: Concave quadrangular
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon"
        );

        // =============== Boundary Values Tests ==================
        // TC10: Vertex on a side of a quadrangular
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side"
        );

        // TC11: Last point = first point
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side"
        );

        // TC12: Co-located points
        assertThrows(
                IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side"
        );
    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)
                };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(
                1,
                result.length(),
                DELTA,
                "Polygon's normal is not a unit vector"
        );
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i) {
            assertEquals(
                    0d,
                    result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])),
                    DELTA,
                    "Polygon's normal is not orthogonal to one of the edges"
            );
        }
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Polygon polygon = new Polygon(
                new Point(0, 0, 0),
                new Point(2, 0, 0),
                new Point(2, 2, 0),
                new Point(0, 2, 0)
        );

        // Points to help with calculations
        final Point pOutside1 = new Point(0.2, 0.2, -1);
        final Point pOutside2 = new Point(1.5, 0.5, -1);
        final Point pOutside3 = new Point(-0.5, -0.5, -1);
        final Point pOutside4 = new Point(0.5, 0, -1);
        final Point pOutside5 = new Point(1.5, 0, -1);
        final Point pInside1 = new Point(0.2, 0.2, 0);

        // Direction vectors to help with calculations
        final Vector v001 = new Vector(0, 0, 1);
        final Vector v300 = new Vector(3, 0, 0);
        final Vector v200 = new Vector(2, 0, 0);
        final Vector v1050 = new Vector(1, -0.5, 0);
        final Vector vMinus02020 = new Vector(-0.2, -0.2, 0);

        // TC01: Inside the Polygon
        final var result1 = polygon.findIntersections(new Ray(pOutside1, v001));
        assertNotNull(
                result1,
                "Ray crosses polygon but result is null"
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

        // TC02: Outside the Polygon (Next to Edge)
        final var result2 = polygon.findIntersections(new Ray(pOutside2, v1050));
        assertNull(
                result2,
                "TC02 ERROR: Ray's line out of polygon"
        );

        // TC03: Outside the Polygon (Next to Vertex)
        final var result3 = polygon.findIntersections(new Ray(pOutside3, vMinus02020));
        assertNull(
                result3,
                "TC03 ERROR: Ray's line out of polygon"
        );

        // =============== Boundary Values Tests ==================
        // TC11: On the Edge
        final var result11 = polygon.findIntersections(new Ray(pOutside4, v001));
        assertNull(
                result11,
                "TC11 ERROR: Ray's line out of polygon"
        );

        // TC12: On the Vertex
        final var result12 = polygon.findIntersections(new Ray(pOutside5, v200));
        assertNull(
                result12,
                "TC12 ERROR: Ray's line out of polygon"
        );

        // TC13: On the Boundary (Edge)
        final var result13 = polygon.findIntersections(new Ray(pOutside5, v300));
        assertNull(
                result13,
                "TC13 ERROR: Ray's line out of polygon"
        );
    }
}
