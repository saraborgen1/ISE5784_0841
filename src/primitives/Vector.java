package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with zero coordinates.");
    }

    public Vector(Double3 double3) {
        super(double3);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with zero coordinates.");
    }

    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    public Vector scale(double number) {
        return new Vector(xyz.scale(number));
    }

    public double dotProduct(Vector vector) {
        return this.xyz.d1 * vector.xyz.d1 + this.xyz.d2 * vector.xyz.d2 + this.xyz.d3 * vector.xyz.d3;
    }

    public Vector crossProduct(Vector vector) {
        double x1 = this.xyz.d1, y1 = this.xyz.d2, z1 = this.xyz.d3;
        double x2 = vector.xyz.d1, y2 = vector.xyz.d2, z2 = vector.xyz.d3;
        return new Vector(
                y1 * z2 - z1 * y2,
                z1 * x2 - x1 * z2,
                x1 * y2 - y1 * x2
        );
    }

    public double lengthSquared() {
        return this.xyz.d1 * this.xyz.d1 + this.xyz.d2 * this.xyz.d2 + this.xyz.d3 * this.xyz.d3;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

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
