package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTest {

    private final Point p100 = new Point(1, 0, 0);

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Sphere sphere = new Sphere(Point.ZERO, 1);

        // ============ Equivalence Partitions Tests ==============
        /*
         *TC01: Test normal at a specific point on the sphere's surface
         */
        assertEquals(
                new Vector(1,0,0),
                sphere.getNormal(p100).normalize(),
                "ERROR: getNormal() wrong result"
        );
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1);
        //נקודות על הSphere
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final Point gp3 = Point.ZERO;
        final Point gp4 = new Point(2, 0, 0);
        final Point gp5 = new Point(1, 1, 0);
        final Point gp6= new Point(1.8867496997597595,0.4622498999199199,0.0);

        //רשימות של נקודות על הSphere
        final var exp1 = List.of(gp1, gp2);
        final var exp2 = List.of(gp3, gp4);

        //וקטורי עזר
        final Vector v310 = new Vector(3, 1, 0);
        final Vector vMinus310 = new Vector(-3, -1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Vector v300= new Vector (3, 0, 0);
        final Vector vMinus300= new Vector (-3, 0, 0);
        final Vector vMinus010= new Vector(0, -1, 0);

        //נקודות עזר
        final Point pOutside = new Point(-1, 0, 0);
        final Point pInside = new Point(0.5, 0, 0);
        final Point pOutside2 = new Point(-1, 1, 0);
        final Point pOutside3 = new Point(1.5, 1, 0);
        final Point pOutside4 = new Point(3, 0, 0);


        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(pOutside, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        var result2 = sphere.findIntersections(new Ray(pOutside, v310));
        assertNotNull(result2, "Ray crosses sphere but result is null");
        result2 = result2.stream().sorted(Comparator.comparingDouble(p -> p.distance(pOutside))).toList();
        assertEquals(2, result2.size(), "Wrong number of points");
        assertEquals(exp1, result2, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final var result3 = sphere.findIntersections(new Ray(pInside, v310));
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(gp6), result3, "Ray starts inside sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(pOutside, vMinus310)),
                "Ray starts after sphere"
        );

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        final var result11 = sphere.findIntersections(new Ray(gp1, v310));
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(List.of(gp2), result11, "Ray starts at sphere and goes inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(
                sphere.findIntersections(new Ray(gp1, vMinus310)),
                "Ray starts at sphere and goes outside"
        );

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        var result13 = sphere.findIntersections(new Ray(pOutside4, vMinus300));
        assertNotNull(result13, "Ray crosses sphere but result is null");
        result13 = result13.stream().sorted(Comparator.comparingDouble(p -> p.distance(pOutside))).toList();
        assertEquals(2, result13.size(), "Wrong number of points");
        assertEquals(exp2, result13, "Ray crosses sphere");

        // TC14: Ray starts at sphere and goes inside (1 point)
        final var result14 = sphere.findIntersections(new Ray(gp3, v300));
        assertEquals(1, result14.size(), "Wrong number of points");
        assertEquals(List.of(gp4), result14, "Ray starts at sphere and goes through the center");

        // TC15: Ray starts inside (1 point)
        final var result15 = sphere.findIntersections(new Ray(pInside,vMinus300));
        assertEquals(1, result15.size(), "Wrong number of points");
        assertEquals(List.of(gp3), result15, "Ray starts inside and goes through the center");

        // TC16: Ray starts at the center (1 point)
        final var result16 = sphere.findIntersections(new Ray(p100, v300));
        assertEquals(1, result16.size(), "Wrong number of points");
        assertEquals(List.of(gp4), result16, "Ray starts at the center");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(
                sphere.findIntersections(new Ray(gp4, v300)),
                "Ray starts at sphere and goes through the center outside"
        );

        // TC18: Ray starts after sphere (0 points)
        assertNull(
                sphere.findIntersections(new Ray(pOutside,vMinus300)),
                "Ray starts after sphere and goes through the center outside"
        );

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(
                sphere.findIntersections(new Ray(pOutside2, v310)),
                "Ray is tangent to the sphere, starts before the tangent point"
        );

        // TC20: Ray starts at the tangent point
        assertNull(
                sphere.findIntersections(new Ray(gp5, v310)),
                "Ray is tangent to the sphere, starts at the tangent point"
        );

        // TC21: Ray starts after the tangent point
        assertNull(
                sphere.findIntersections(new Ray(pOutside3, v310)),
                "Ray is tangent to the sphere, starts after the tangent point"
        );

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(
                sphere.findIntersections(new Ray(pOutside2, vMinus010)),
                "Ray is orthogonal to the line from ray start to sphere's center and does not intersect"
        );
    }
}