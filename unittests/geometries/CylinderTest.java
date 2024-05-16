package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    @Test
    void testGetNormal() {
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                1,
                5
        );

        Vector v=new Vector(1, 0, 0);

        //נקודה על צד הגליל
        assertEquals(
                v,
                cylinder.getNormal(new Point(1, 0, 2)),
                ""
        );

        //נקודה על הבסיס העליון
        assertEquals(
                new Vector(0, 0, 1),
                cylinder.getNormal(new Point(0.5, 0.5, 5)),
                ""
        );

        //נקודה על הבסיס התחתון
        assertEquals(
                new Vector(0, 0, -1),
                cylinder.getNormal(new Point(0.5, 0.5, 0)),
                ""
        );

        //נקודת המרכז על הבסיס העליון
        assertEquals(
                v,
                cylinder.getNormal(new Point(0, 0, 5)),
                ""
        );

        //נקודת המרכז על הבסיס התחתון
        assertEquals(
                v,
                cylinder.getNormal(new Point(0, 0, 0)),
                ""
        );

    }

}