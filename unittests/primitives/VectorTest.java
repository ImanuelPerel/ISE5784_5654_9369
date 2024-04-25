package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit test for primitives.Vector class
 * @author Shneor and Emanuel
 */
class VectorTest {
    private final Vector v1 = new Vector(1, 2, 2);
    private final Vector v2 = new Vector(-3, -4, -5);
    private final Vector v3 = new Vector(-1, 2, -3);
    private final Vector v4 = new Vector(0, 2, -2);
    private final Vector v5 = new Vector(2, 4, 4);


    /**
     * Test method for
     * {@link primitives.Vector#equals(Object)}.
     */
    @Test
    void testEquals() {
        // ============ Equivalence Partitions Tests ==============

        //TC01: Checks whether two vectors are equal
        assertEquals(v1, new Vector(1, 2, 2), "ERROR: The equality is not working properly");

        // =============== Boundary Values Tests ==================
        //TC02: Testing the ZERO vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "ERROR: can't create the zero vector");
    }


    /**
     * Test method for
     * {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        //TC01: Test for a vector with positive components
        assertEquals(9, v1.lengthSquared(), "ERROR: Incorrect squared length for positive components");

        //TC02: Test for a vector with negative components
        assertEquals(50, v2.lengthSquared(), "ERROR: Incorrect squared length for negative components");

        //TC03: Test for a vector with mixed positive and negative components
        assertEquals(14, v3.lengthSquared(), "ERROR: Incorrect squared length for mixed components");

    }

    /**
     * Test method for
     * {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============

        //TC01: Test for a vector with positive components
        assertEquals(3, v1.length(), "ERROR: Incorrect length for positive components");

        //TC02: Test for a vector with negative components
        assertEquals(Math.sqrt(50), v2.length(), "ERROR: Incorrect length for negative components");

        //TC03: Test for a vector with mixed positive and negative components
        assertEquals(Math.sqrt(14), v3.length(), "ERROR: Incorrect length for mixed components");
    }

    /**
     * Test method for
     * {@link primitives.Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        //TC01: A test of the addition of two vectors
        assertEquals(new Vector(-2, -2, -3), v1.add(v2), "ERROR: The add function does not work properly");

        // =============== Boundary Values Tests ==================

        //TC02: Test for an exception being thrown in the zero case vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1, -2, -2)), "ERROR: The add function returns the zero vector");
    }

    /**
     * Test method for
     * {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test the Scalar multiplication of a positive number
        assertEquals(new Vector(-2, 4, -6), v3.scale(2), "ERROR: the Scale function not working properly");

        //TC02: Test the Scalar multiplication of a negative number
        assertEquals(new Vector(3, -6, 9), v3.scale(-3), "ERROR: the Scale function not working properly");

        // =============== Boundary Values Tests ==================

        //TC03: Test with the 0
        assertThrows(IllegalArgumentException.class, () -> v3.scale(0), "ERROR: the Vector can't be the ZERO Vector");
    }

    /**
     * Test method for
     * {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============

        //TC01: Test of dotProduct between two vectors
        assertEquals(10, v2.dotProduct(v3), "ERROR: the DotProduct function not working properly");

        // =============== Boundary Values Tests ==================

        //TC02: Test between two perpendicular vectors
        assertEquals(0, v1.dotProduct(v4), "ERROR: the DotProduct function does not work properly with perpendicular vectors");

        //TC03: Test between two parallel vectors
        assertEquals(18, v1.dotProduct(v5), "ERROR: the DotProduct function does not work properly with parallel vectors");
    }


    /**
     * Test method for
     * {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector vr = v1.crossProduct(v4);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Cross product test between two vectors
        assertEquals(new Vector(22,-4,-10),v2.crossProduct(v3),"ERROR: The CrossProduct function does not work properly");

        // =============== Boundary Values Tests ==================
        //TC02: Cross product test between two perpendicular vectors
        assertEquals(v1.length()*v4.length(),vr.length(),0.01,"ERROR: A cross product between two perpendicular vectors does not work properly");

        //TC03: Cross product test between two perpendicular vectors
        assertThrows(IllegalArgumentException.class,()->v1.crossProduct(v5),"ERROR: A cross product does not work properly with the ZERO Vector");
    }

    /**
     * Test method for
     * {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Checking the length of the vector
        assertEquals(1,(v3.normalize()).length(),"ERROR: The length of the vector must be 1");
    }
}