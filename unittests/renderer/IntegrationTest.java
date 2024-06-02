package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import geometries.*;
import org.junit.jupiter.api.Test;

import primitives.*;

/**
 *
 */
class IntegrationTest {
    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(1)
            .setVpSize(3, 3);

    /**
     * Checks intersections between rays from the camera and a geometry.
     * Computes the total number of intersections and compares it to the expected value.
     *
     * @param c             The camera object
     * @param g             The geometry to test intersections with
     * @param intersections The expected number of intersections
     * @param assertMessage The message to display in case of assertion failure
     */
    private void checkIntersections(Camera c, Geometry g, int intersections, String assertMessage) {
        int count = 0;
        for (int i = 0; i < 3; i++) { //go through the 3x3 view plane
            for (int j = 0; j < 3; j++) {
                Ray r = c.constructRay(3, 3, j, i);
                if (g.findIntersections(r) != null) {
                    count += g.findIntersections(r).size(); //add the intersection if exists
                }
            }
        }
        assertEquals(intersections, count, assertMessage);
    }

    /**
     * Tests intersections between rays from the camera and spheres.
     */
    @Test
    void testSphereIntersections() {
        Camera c1 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

        //TC01 The view plane passes through the center of the sphere
        Sphere s1 = new Sphere(new Point(0, 0, -3), 1);
        checkIntersections(
                c1,
                s1,
                2,
                "ERROR Sphere: Expected 2 points"
        );

        //TC02 The sphere is larger than the view plane - 18 intersections
        Sphere s2 = new Sphere(new Point(0, 0, -2.5), 2.5);
        checkIntersections(
                c1,
                s2,
                18,
                "ERROR Sphere: Expected 18 points"
        );

        //TC03 The view plane originates from inside the sphere - 10 intersections
        Sphere s3 = new Sphere(new Point(0, 0, -2), 2);
        checkIntersections(
                c1,
                s3,
                10,
                "ERROR Sphere: Expected 10 points"
        );

        //TC04 The view plane originates from the center of the sphere - 9 intersections
        Sphere s4 = new Sphere(new Point(0, 0, -2), 4);
        checkIntersections(
                c1,
                s4,
                9,
                "ERROR Sphere: Expected 9 points"
        );

        //TC05  No intersections between the view plane and the sphere
        Sphere s5 = new Sphere(new Point(0, 0, 1), 0.5);
        checkIntersections(
                c1,
                s5,
                0,
                "ERROR Sphere: Expected 0 points"
        );
    }

    /**
     * Tests intersections between rays from the camera and planes.
     */
    @Test
    void testPlaneIntersections() {
        Camera c1 = cameraBuilder.build();

        //TC01 Plane parallel to view plane - 9 points
        Plane p1 = new Plane(new Point(0, 0, -3), new Vector(0, 0, 1));
        checkIntersections(
                c1,
                p1,
                9,
                "ERROR Plane: Expected 9 points"
        );

        //TC02 Plane slightly slanted against view plane - 9 points
        Plane p2 = new Plane(new Point(0, 0, -3), new Vector(0, 1, 2));
        checkIntersections(
                c1,
                p2,
                9,
                "ERROR Plane: Expected 9 points"
        );

        //TC03 Plane very slanted against view plane - 6 points
        Plane p3 = new Plane(new Point(0, 0, -3), new Vector(0, 1, 1));
        checkIntersections(
                c1,
                p3,
                6,
                "ERROR Plane: Expected 6 points"
        );

        //TC04 Plane behind view plane - 0 points
        Plane p4 = new Plane(new Point(1, 1, 2), new Point(-1, 1, 2), new Point(0, -10, 2));
        checkIntersections(
                c1,
                p4,
                0,
                "ERROR Plane: Expected 0 points"
        );
    }

    /**
     * Tests intersections between rays from the camera and polygons.
     */
    @Test
    void testPolygonIntersections() {
        Camera c1 = cameraBuilder.build();

        //TC01 small triangle in front of view plane - 1 point
        Polygon p1 = new Polygon(
                new Point(0, 1, -2),
                new Point(1, 1, -2),
                new Point(-1, -1, -2)
        );
        checkIntersections(
                c1,
                p1,
                1,
                "ERROR Polygon: Expected 1 points"
        );

        //TC02 tall triangle - 2 points
        Polygon p2 = new Polygon(
                new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        checkIntersections(
                c1,
                p2,
                2,
                "ERROR Polygon: Expected 2 points"
        );

        //TC03 triangle behind view plane - 0 points
        Polygon p3 = new Polygon(
                new Point(1, 1, 2),
                new Point(-1, 1, 2),
                new Point(0, -20, 2)
        );
        checkIntersections(
                c1,
                p3,
                0,
                "ERROR Polygon: Expected 0 points"
        );
    }
}
