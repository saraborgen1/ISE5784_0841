package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with non-zero coordinates.");
    }

    public Vector(Double3 double3) {
        super(double3);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector cannot be initialized with non-zero coordinates.");
    }

    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    public Vector scale(double number) {
        return new Vector(xyz.scale(number));
    }

    public double dotProduct(Vector vector) {
        Vector tempVector = new Vector(xyz.product(vector.xyz));
        return (tempVector.xyz.d1+tempVector.xyz.d2+tempVector.xyz.d3);
    }

    public Vector crossProduct(Vector vector) {
        return new Vector(
                (this.xyz.d2*vector.xyz.d3-this.xyz.d3*vector.xyz.d2),
                (this.xyz.d3*vector.xyz.d1-this.xyz.d1*vector.xyz.d3),
                (this.xyz.d1*vector.xyz.d2-this.xyz.d2*vector.xyz.d1)
        );
    }

    public double lengthSquared() {
        Vector tempVector = new Vector(xyz.product(xyz));
        return (tempVector.xyz.d1+tempVector.xyz.d2+tempVector.xyz.d3);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

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
