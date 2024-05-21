package primitives;

/**
 *Class representing a Vector in 3D space.
 */
public class Vector extends Point {

    /**
     * Constructor to create a vector from three coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        // Check if the vector is a zero vector and throw an exception if true.
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with zero coordinates.");
    }

    /**
     * Constructor to create a vector from a Double3 object.
     * @param double3 The Double3 object containing the coordinates.
     */
    public Vector(Double3 double3) {
        super(double3);
        // Check if the vector is a zero vector and throw an exception if true.
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with zero coordinates.");
    }

    /**
     * Adds another vector to this vector.
     * @param vector The Vector to add.
     * @return A new vector which is the sum of this vector and the given vector.
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a given number.
     * @param number The scaling factor.
     * @return A new vector which is this vector scaled by the given number.
     */
    public Vector scale(double number) {
        return new Vector(xyz.scale(number));
    }

    /**
     * Computes the dot product of this vector and another vector.
     * @param vector The other vector.
     * @return The dot product of the two vectors.
     */
    public double dotProduct(Vector vector) {
        return this.xyz.d1 * vector.xyz.d1 + this.xyz.d2 * vector.xyz.d2 + this.xyz.d3 * vector.xyz.d3;
    }

    /**
     * Computes the cross product of this vector and another vector.
     * @param vector The other vector
     * @return A new vector which is the cross product of this vector and the given vector.
     */
    public Vector crossProduct(Vector vector) {
        double x1 = this.xyz.d1, y1 = this.xyz.d2, z1 = this.xyz.d3;
        double x2 = vector.xyz.d1, y2 = vector.xyz.d2, z2 = vector.xyz.d3;
        return new Vector(
                y1 * z2 - z1 * y2,
                z1 * x2 - x1 * z2,
                x1 * y2 - y1 * x2
        );
    }

    /**
     * Computes the squared length of this vector.
     * @return The squared length of this vector.
     */
    public double lengthSquared() {
        return this.xyz.d1 * this.xyz.d1 + this.xyz.d2 * this.xyz.d2 + this.xyz.d3 * this.xyz.d3;
    }

    /**
     * Computes the length of this vector.
     * @return The length of this vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector (scales it to length 1).
     * @return A new normalized vector.
     */
    public Vector normalize() {
        double len = length();
        if (len == 0) {
            throw new IllegalArgumentException("Cannot normalize zero vector");
        }
        return new Vector(this.xyz.d1 / len, this.xyz.d2 / len, this.xyz.d3 / len);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "Vector{} " + super.toString();
    }
}
