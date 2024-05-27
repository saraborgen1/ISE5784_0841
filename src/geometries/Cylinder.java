package geometries;
import primitives.*;

import java.util.List;

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

    /**
     * Returns the normal vector to the cylinder at a given point.
     * @param p The point on the surface of the cylinder.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point p) {
        Point point = axis.getHead();
        Vector vector = axis.getDirection().normalize();

        // Check if the point is on the bottom base of the cylinder.
        if (p.equals(point) || Util.isZero(p.subtract(point).dotProduct(vector))) {
            // Normal points in the opposite direction of the axis.
            return vector.scale(-1);
        }

        // Check if the point is on the top base of the cylinder.
        if ( p.equals(point.add(vector.scale(height)))||
                Util.isZero(p.subtract(point.add(vector.scale(height))).dotProduct(vector)))
            // Normal points in the direction of the axis.
            return vector;

        // If the point is on the curved surface of the cylinder, use the Tube's normal calculation.
        return super.getNormal(p);
    }

    public List<Point> findIntersections(Ray ray){
        return null;
    }
}
