package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Geometries class
 */
class GeometriesTest {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(0, 0, -5), 1),
                new Triangle(
                        new Point(-2, -2, -2),
                        new Point(2, -2, -2),
                        new Point(0, 2, -2)
                ),
                new Plane(
                        new Point(0, 0, -10),
                        new Vector(0, 0, -1)
                )
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some of the shapes are intersected but not all of them
        Ray ray = new Ray(new Point(0, 0, -7), new Vector(0, 0, 1));
        var intersections = geometries.findIntersections(ray);
        assertEquals(3, intersections.size());

        // =============== Boundary Values Tests ==================
        // TC11: Empty collection
        Geometries geometries2 = new Geometries();
        ray = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        intersections = geometries2.findIntersections(ray);
        assertTrue(intersections.isEmpty());

        // TC12: None of the shapes is intersected
        ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        intersections = geometries.findIntersections(ray);
        assertTrue(intersections.isEmpty());

        // TC13: Just one shape is intersected
        ray = new Ray(new Point(0, 0, -2.5), new Vector(0, 0, 1));
        intersections = geometries.findIntersections(ray);
        assertEquals(1, intersections.size());

        // TC14: All the shapes are intersected
        ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, -1));
        intersections = geometries.findIntersections(ray);
        assertEquals(4, intersections.size());
   }
}