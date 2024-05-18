package geometries;
import primitives.*;

/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructs a new Cylinder with the given axis ray, radius, and height.
     * @param axis The central axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder(Ray axis, double radius, double height){
        super(axis, radius);
        this.height = height;
    }

    public Vector getNormal(Point p) {
        Point point = axis.getHead();
        Vector vector = axis.getDirection();

        if (p.equals(point))
            return vector;

        Vector u = p.subtract(point);

        // distance from p0 to the o who is in from of point
        double t = Util.alignZero(u.dotProduct(vector));

        // if the point is at a base
        if (Util.isZero(t) || Util.isZero(height - t))
            return vector;

        //the other point on the axis facing the given point
        Point o = point.add(vector.scale(t));

        return p.subtract(o).normalize();
    }
}
