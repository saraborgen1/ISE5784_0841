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
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    @Test
    void testConstructor(){

    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
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

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Point)}.
     */
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

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
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

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
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

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
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

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
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

    /**
     * Test method for {@link Vector#length()}.
     */
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

    /**
     * Test method for {@link Vector#normalize()}.
     */
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