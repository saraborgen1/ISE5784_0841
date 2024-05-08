package primitives;

/**
 *Class representing a point in 3D space.
 */
public class Point {

    protected final Double3 xyz;
    public static final Point ZERO = new Point(Double3.ZERO);

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    public Point(Double3 xyz) {
        this.xyz=xyz;
    }

    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }

    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    public double distanceSquared(Point p1) {
        Double3 temp=this.xyz.subtract(p1.xyz);
        Double3 temp2=new Double3(temp.d1*temp.d1,temp.d2*temp.d2,temp.d3*temp.d3);
        return (temp2.d1+temp2.d2+temp2.d3);
    }

    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
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
