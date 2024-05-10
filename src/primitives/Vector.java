package primitives;

/**
 * Class representing a vector in 3D space.
 */
public class Vector extends Point {

    /**
     * Constructs a new Vector with the given coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with non-zero coordinates.");
    }

    /**
     * Constructs a new Vector from a Double3 object.
     * @param double3 The Double3 object representing the coordinates.
     */
    public Vector(Double3 double3) {
        super(double3);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with non-zero coordinates.");
    }

    /**
     *  Adds a Vector to this Vector, returning the resulting Vector.
     * @param vector The Vector to add.
     * @return The new Vector after adding the Vector.
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Scales this Vector by a scalar value, returning the resulting Vector.
     * @param number The scalar value to scale the Vector by.
     * @return The scaled Vector.
     */
    public Vector scale(double number) {
        return new Vector(xyz.scale(number));
    }

    /**
     * Calculates the dot product of this Vector with another Vector.
     * @param vector The other Vector.
     * @return The dot product of the two Vectors.
     */
    public double dotProduct(Vector vector) {
        return (this.xyz.d1*vector.xyz.d1+this.xyz.d2*vector.xyz.d2+this.xyz.d3*vector.xyz.d3);
    }

    /**
     * Calculates the cross product of this Vector with another Vector.
     * @param vector The other Vector.
     * @return The cross product of the two Vectors.
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                (this.xyz.d2*vector.xyz.d3-this.xyz.d3*vector.xyz.d2),
                (this.xyz.d3*vector.xyz.d1-this.xyz.d1*vector.xyz.d3),
                (this.xyz.d1*vector.xyz.d2-this.xyz.d2*vector.xyz.d1)
        );
    }

    /**
     * Calculates the squared length of this Vector.
     * @return The squared length of the Vector.
     */
    public double lengthSquared() {
        return (this.xyz.d1*this.xyz.d1+this.xyz.d2*this.xyz.d2+this.xyz.d3*this.xyz.d3);
    }

    /**
     * Calculates the length of this Vector.
     * @return The length of the Vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this Vector, returning a new Vector in the same direction with unit length.
     * @return The normalized Vector.
     */
    public Vector normalize() {
        return new Vector(this.xyz.d1/this.length(),this.xyz.d2/this.length(),this.xyz.d3/this.length());
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
