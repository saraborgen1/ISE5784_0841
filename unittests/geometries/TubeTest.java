package geometries;
import primitives.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    @Test
    void testGetNormal() {
        Tube tube= new Tube(new Ray(Point.ZERO, new Vector(1, 0, 0)),1);

        assertEquals(
                new Vector(0, 0, 1),
                tube.getNormal(Point.ZERO),
                "Normal is incorrect"
        );

        assertEquals(
                new Vector(-1, 0, 0),
                tube.getNormal(new Point(1,0,0)),
                "Normal at the endpoint is incorrect"
        );

    }

}