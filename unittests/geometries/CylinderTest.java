package geometries;

import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTest {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                2,
                10
        );

        // ============ Equivalence Partitions Tests ==============
        //TC01:Test normal at a point on the side surface of the cylinder
        assertEquals(
                new Vector(2, 0, 0).normalize(),
                cylinder.getNormal(new Point(2, 0, 5)),
                "ERROR: getNormal() wrong result for point on side surface"
        );

        // TC02:Test normal at a point on the top base of the cylinder
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(new Point(1, 1, 10)),
                "ERROR: getNormal() wrong result for point on top base"
        );

        // TC03:Test normal at a point on the bottom base of the cylinder
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(new Point(1, 1, 0)),
                "ERROR: getNormal() wrong result for point on bottom base"
        );

        // =============== Boundary Values Tests ==================
        //TC01:Test normal at the center of the top base
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(new Point(0, 0, 10)),
                "ERROR: getNormal() wrong result for point at center of top base"
        );


        //TC02:Test normal at the center of the bottom base
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(new Point(0, 0, 0)),
                "ERROR: getNormal() wrong result for point at center of bottom base"
        );
    }
}