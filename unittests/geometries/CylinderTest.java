package geometries;

import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTest {

    @Test
    void testGetNormal() {
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(0,0,0), new Vector(0, 0, 1)),
                2,
                10
        );


        //נקודה על צד הגליל
        assertEquals(
                new Vector(2, 0, 0).normalize(),
                cylinder.getNormal(new Point(2, 0, 5)),
                ""
        );

        //נקודה על הבסיס העליון
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(new Point(1, 1, 10)),
                ""
        );

        //נקודה על הבסיס התחתון
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(new Point(1, 1, 0)),
                ""
        );

        //נקודת המרכז על הבסיס העליון
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(new Point(0, 0, 10)),
                ""
        );

        //נקודת המרכז על הבסיס התחתון
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(new Point(0, 0, 0)),
                ""
        );

    }

}