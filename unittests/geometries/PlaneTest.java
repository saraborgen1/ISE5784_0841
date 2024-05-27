package geometries;

import primitives.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;
    private final Point p1 = new Point(1, 0, 0);
    private final Point p2 = new Point(0, 1, 0);
    private final Point p3 = new Point(0, 0, 1);
    private final Plane plane = new Plane(p1, p2, p3);

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     * Test method for {@link geometries.Plane#Plane(Point, Vector)}.
     */
    @Test
    void testConstructor() {

        // =============== Boundary Values Tests ==================
        /*
         * TC01: Test constructor with two identical points, should throw IllegalArgumentException
         */
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(1, 1, 1),
                        new Point(1, 1, 1),
                        new Point(0, 0, 1)
                ),
                "Constructor should throw exception when first and second points coincide"
        );

        /*
         * TC02:A test constructor with three points on the same line should throw IllegalArgumentException
         */
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plane(
                        new Point(1, 1, 1),
                        new Point(2, 2, 2),
                        new Point(3, 3, 3)
                ),
                "Constructor should throw exception when all points lie on the same line"
        );
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        /*
         *TC01:Test that the normal vector has a length of 1
         */
        assertEquals(
                1,
                plane.getNormal().length(),
                DELTA,
                "Normal vector should have length 1"
        );

        /*
         *TC02:Test that the normal vector is correctly calculated and normalized
         */
        assertEquals(
                new Vector(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3)),
                plane.getNormal().normalize(),
                "ERROR: getNormal() wrong result"
        );
    }

    @Test
    void testFindIntersections() {
        //Direction vectors to help with calculations
        final Vector vMinus111 = new Vector(-1, -1, -1);
        final Vector v111 = new Vector(1, 1, 1);
        final Vector v110Minus = new Vector(1,-1,0);
        final Vector V110= new Vector(1,1,0);
        final Vector v121 = new Vector(1,2,-1);

        //Points to help with calculations
        final Point pOutside1 = new Point(1, 1, 1);
        final Point pOutside2 = Point.ZERO;
        final Point pOutside3 = new Point(2, 2, 2);
        final Point pInside1 = new Point(1 / 3.0, 1 / 3.0, 1 / 3.0);


        // ============ Equivalence Partitions Tests ==============
        //The Ray must be neither orthogonal nor parallel to the plane
        // TC01: Ray intersects the plane (1 point)
        List<Point> result1 = plane.findIntersections(new Ray(pOutside1,vMinus111));
        assertNotNull(result1, "Ray crosses plane but result is null");
        assertEquals(1, result1.size(), "TC01 ERROR: Wrong number of points");
        assertEquals(List.of(pInside1), result1, "TC01 ERROR: Wrong intersection point");

        // TC02: Ray does not intersect the plane(0 point)
        List<Point> result2 = plane.findIntersections(new Ray(pOutside1,v111));
        assertNull(result2, "TC02 ERROR: Ray's line out of plane");

        // =============== Boundary Values Tests ==================
        // ** Group: Ray is parallel to the plane
        // TC11:Ray included in the plane (0 point)
        List<Point> result11 = plane.findIntersections(new Ray(p3,v110Minus));
        assertNull(result11,"TC11 ERROR: Ray's line out of plane");

        //TC12:Ray not included in the plane (0 point)
        List<Point> result12 = plane.findIntersections(new Ray(pOutside1,v110Minus));
        assertNull(result12,"TC12 ERROR: Ray's line out of plane");

        // ** Group: Ray is orthogonal to the plane
        //TC13:Ray start before plane (1 point)
        List<Point> result13 = plane.findIntersections(new Ray(pOutside2,v111));
        assertNotNull(result13, "Ray crosses plane but result is null");
        assertEquals(1, result13.size(), "TC13 ERROR: Wrong number of points");
        assertEquals(List.of(pInside1), result13, "TC13 ERROR: Wrong intersection point");

        //TC14:Ray start in  plane (0 point)
        assertNull(plane.findIntersections(new Ray(p3,v111)),
                "TC14 ERROR: Ray's line out of plane");

        //TC15:Ray start after  plane (0 point)
        assertNull(plane.findIntersections(new Ray(pOutside3,v111)),
                "TC15 ERROR: Ray's line out of plane");

        // ** Group: Ray is neither orthogonal nor parallel to and begins at the plane
        //TC16:not the same point which appears as reference point in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(p2,v121)),
                "TC16 ERROR: Ray's line out of plane");

        //TC17: the same point which appears as reference point in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(p3,V110)),
                "TC17 ERROR: Ray's line out of plane");
    }
}