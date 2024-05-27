package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Tube tube= new Tube(new Ray(new Point(1,1,1), new Vector(0, 1, 0)),1);

        // ============ Equivalence Partitions Tests ==============
        /*
         * TC01:Test normal at a general point on the surface of the tube
         */
        assertEquals(
                new Vector(0, 0, 1),
                tube.getNormal(new Point(1, 3, 2)),
                "ERROR: getNormal() wrong result"
        );

        // =============== Boundary Values Tests ==================
        /*
         * TC01: Test normal at the tube's axis point, should throw IllegalArgumentException
         */
        assertThrows(
                IllegalArgumentException.class,
                ()-> tube.getNormal(new Point(1,1,1)),
                "ERROR: No exception thrown for point on the axis of the tube"
        );
    }

    @Test
    void testFindIntersections() {
    }
}