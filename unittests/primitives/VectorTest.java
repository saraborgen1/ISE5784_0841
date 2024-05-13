package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class VectorTest {

    @Test
    void testAdd() {
    }

    @Test
    void testScale() {
    }

    @Test
    void testDotProduct() {
    }

    @Test
    void testCrossProduct() {
    }

    @Test
    void testLengthSquared() {
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(14, v1.lengthSquared(),0.00001,"ERROR: lengthSquared() wrong value");
    }

    @Test
    void testLength() {
    }

    @Test
    void testNormalize() {
    }
}