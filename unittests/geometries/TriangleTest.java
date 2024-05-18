package geometries;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    public void testGetNormal() {

        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );


        assertEquals(
                new Vector(0, 0, 1),
                triangle.getNormal(new Point(0, 0, 0)),
                "Wrong normal calculation"
        );
    }

}