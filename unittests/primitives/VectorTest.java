package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 */
class VectorTest {

    private final double DELTA = 0.000001;

    @Test
    void testAdd() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(-1, -2, -3);

        assertThrows(
                IllegalArgumentException.class,
                () -> v1.add(v3),
                "ERROR: Vector + -itself does not throw an exception"
        );

        assertEquals(v3, v1.add(v2), "ERROR: Vector + Vector does not work correctly");

    }

    @Test
    void testSubtract() {
        Vector v1 = new Vector(1, 2, 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> v1.subtract(v1),
                "ERROR: Vector - itself does not throw an exception"
        );

        assertEquals(
                new Vector(3, 6, 9),
                v1.subtract(new Vector(-2, -4, -6)),
                "ERROR: Vector + Vector does not work correctly"
        );

    }

    @Test
    void testScale() {
        Vector v1 = new Vector(1, 2, 3);

        assertThrows(
                IllegalArgumentException.class,
                () -> v1.scale(0),
                "ERROR: Vector * 0 does not throw an exception"
        );

        assertEquals(
                new Vector(3, 6, 9),
                v1.scale(3),
                "ERROR: Vector * number does not work correctly"
        );
    }

    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(0, 3, -2);
        Vector v3 = new Vector(-2, -4, -6);

        assertEquals(
                0,
                v1.dotProduct(v2),
                DELTA,
                "ERROR: dotProduct() for orthogonal vectors is not zero"
        );

        assertEquals(
                -28,
                v1.dotProduct(v3),
                DELTA,
                "ERROR: dotProduct() wrong value"
        );
    }

    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        assertThrows(
                IllegalArgumentException.class,
                () -> v1.crossProduct(v2),
                "ERROR: crossProduct() for parallel vectors does not throw an exception"
        );

        assertEquals(
                v1.crossProduct(v3).length(),
                v1.length() * v3.length(),
                DELTA,
                "ERROR: crossProduct() wrong result length"
        );

        assertEquals(
                0,
                v1.crossProduct(v3).dotProduct(v1),
                DELTA,
                "ERROR: crossProduct() result is not orthogonal to its first operand"
        );

        assertEquals(
                0,
                v1.crossProduct(v3).dotProduct(v3),
                DELTA,
                "ERROR: crossProduct() result is not orthogonal to its second operand"
        );
    }

    @Test
    void testLengthSquared() {
        Vector v1 = new Vector(1, 2, 2);

        assertEquals(
                9,
                v1.lengthSquared(),
                DELTA,
                "ERROR: lengthSquared() wrong value"
        );
    }

    @Test
    void testLength() {
        Vector v1 = new Vector(1, 2, 2);

        assertEquals(
                3,
                v1.length(),
                DELTA,
                "ERROR: length() wrong value"
        );
    }

    @Test
    void testNormalize() {
        Vector v1 = new Vector(1, 2, 2);

        assertEquals(
                1,
                v1.normalize().length(),
                DELTA,
                "ERROR: the normalized vector is not a unit vector"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> v1.crossProduct(v1.normalize()),
                "ERROR: the normalized vector is not parallel to the original one"
        );

        assertTrue(
                v1.dotProduct(v1.normalize()) > 0,
                "ERROR: the normalized vector is opposite to the original one"
        );

    }
}