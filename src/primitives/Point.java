package primitives;

/**
 *Class representing a point in 3D space.
 */
public class Point {

    protected final Double3 xyz;
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructs a new Point with the given coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    /**
     * Constructs a new Point from a Double3 object.
     * @param xyz The Double3 object representing the coordinates.
     */
    public Point(Double3 xyz) {
        this.xyz=xyz;
    }

    /**
     * Subtracts another Point from this Point, returning the resulting Vector.
     * @param point Point to subtract.
     * @return The Vector representing the difference between this Point and the given Point.
     */
    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }

    /**
     * Adds a Vector to this Point, returning the resulting Point.
     * @param vector The Vector to add.
     * @return The new Point after adding the Vector.
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Calculates the squared distance between this Point and another Point.
     * @param point The other Point.
     * @return  The squared distance between the two Points.
     */
    public double distanceSquared(Point point) {
        Double3 temp=this.xyz.subtract(point.xyz);
        return (temp.d1*temp.d1+temp.d2*temp.d2+temp.d3*temp.d3);
    }

    /**
     * Calculates the distance between this Point and another Point.
     * @param point The other Point.
     * @return The distance between the two Points.
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }
}
