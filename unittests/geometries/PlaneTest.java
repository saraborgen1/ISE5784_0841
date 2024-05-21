package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTest {

    private final double DELTA = 0.000001;

    @Test
    void testConstructor() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Plane(
                            new Point(1, 1, 1),
                            new Point(1, 1, 1),
                            new Point(0, 0, 1)
                    ),
                    "Constructor should throw exception when first and second points coincide"
            );

            // Test case 2: All points lie on the same line
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

    @Test
    void testGetNormal() {
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);
        Plane plane = new Plane(p1, p2, p3);

        assertEquals(
                1,
                plane.getNormal().length() ,
                DELTA,
                "Normal vector should have length 1"
        );

        //לבדוק שזה באמת יצא תקין
        assertEquals(
                new Vector(1/Math.sqrt(3), 1/Math.sqrt(3),1/Math.sqrt(3)),
                plane.getNormal().normalize(),
                ""
        );
    }

}