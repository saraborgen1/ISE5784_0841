package renderer;

import geometries.Geometry;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
class IntegrationTest {
    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(1)
            .setVpSize(3, 3);

    /**
     * Checks expected between rays from the camera and a geometry.
     * Computes the total number of expected and compares it to the expected value.
     *
     * @param expected The expected number of expected
     * @param camera   The camera object
     * @param geometry The geometry to test expected with
     * @param nX       The number of pixels in the X direction of the view plane (resolution width)
     * @param nY       The number of pixels in the Y direction of the view plane (resolution height)
     * @param message  The message to display in case of assertion failure
     */
    private void assertIntersections
    (int expected, Camera camera, Geometry geometry, int nX, int nY, String message) {
        int count = 0;
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                if (geometry.findIntersections(ray) != null) {
                    count += geometry.findIntersections(ray).size(); //add the intersection if exists
                }
            }
        }
        assertEquals(expected, count, message);
    }

    /**
     * Tests intersections between rays from the camera and spheres.
     */
    @Test
    void testSphereIntersections() {
        Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

        //TC01 The view plane passes through the center of the sphere
        Sphere sphere1 = new Sphere(new Point(0, 0, -3), 1);
        assertIntersections(
                2,
                camera,
                sphere1,
                3, 3, "ERROR Sphere: Expected 2 points"
        );

        //TC02 The sphere is larger than the view plane - 18 intersections
        Sphere sphere2 = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertIntersections(
                18,
                camera,
                sphere2,
                3, 3, "ERROR Sphere: Expected 18 points"
        );

        //TC03 The view plane originates from inside the sphere - 10 intersections
        Sphere sphere3 = new Sphere(new Point(0, 0, -2), 2);
        assertIntersections(
                10, camera,
                sphere3,
                3, 3, "ERROR Sphere: Expected 10 points"
        );

        //TC04 The view plane originates from the center of the sphere - 9 intersections
        Sphere sphere4 = new Sphere(new Point(0, 0, -2), 4);
        assertIntersections(
                9, camera,
                sphere4,
                3, 3, "ERROR Sphere: Expected 9 points"
        );

        //TC05  No intersections between the view plane and the sphere
        Sphere sphere5 = new Sphere(new Point(0, 0, 1), 0.5);
        assertIntersections(
                0, camera,
                sphere5,
                3, 3, "ERROR Sphere: Expected 0 points"
        );
    }

    /**
     * Tests intersections between rays from the camera and planes.
     */
    @Test
    void testPlaneIntersections() {
        Camera camera = cameraBuilder.build();

        //TC01 Plane parallel to view plane - 9 points
        Plane plane1 = new Plane(new Point(0, 0, -3), new Vector(0, 0, 1));
        assertIntersections(
                9, camera,
                plane1,
                3, 3, "ERROR Plane: Expected 9 points"
        );

        //TC02 Plane slightly slanted against view plane - 9 points
        Plane plane2 = new Plane(new Point(0, 0, -3), new Vector(0, 1, 2));
        assertIntersections(
                9, camera,
                plane2,
                3, 3, "ERROR Plane: Expected 9 points"
        );

        //TC03 Plane very slanted against view plane - 6 points
        Plane plane3 = new Plane(new Point(0, 0, -3), new Vector(0, 1, 1));
        assertIntersections(
                6, camera,
                plane3,
                3, 3, "ERROR Plane: Expected 6 points"
        );

        //TC04 Plane behind view plane - 0 points
        Plane plane4 = new Plane(
                new Point(1, 1, 2),
                new Point(-1, 1, 2),
                new Point(0, -10, 2)
        );
        assertIntersections(
                0, camera,
                plane4,
                3, 3, "ERROR Plane: Expected 0 points"
        );
    }

    /**
     * Tests intersections between rays from the camera and polygons.
     */
    @Test
    void testPolygonIntersections() {
        Camera camera = cameraBuilder.build();

        //TC01 small triangle in front of view plane - 1 point
        Polygon polygon1 = new Polygon(
                new Point(0, 10, -2),
                new Point(1, 1, -2),
                new Point(-1, -1, -2)
        );
        assertIntersections(
                1,
                camera,
                polygon1,
                3, 3, "ERROR Polygon: Expected 1 points"
        );

        //TC02 tall triangle - 2 points
        Polygon polygon2 = new Polygon(
                new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
        assertIntersections(
                2, camera,
                polygon2,
                3, 3, "ERROR Polygon: Expected 2 points"
        );

        //TC03 triangle behind view plane - 0 points
        Polygon p3 = new Polygon(
                new Point(1, 1, 2),
                new Point(-1, 1, 2),
                new Point(0, -20, 2)
        );
        assertIntersections(
                0, camera,
                p3,
                3, 3, "ERROR Polygon: Expected 0 points"
        );
    }
}
