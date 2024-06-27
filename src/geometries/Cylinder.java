package geometries;

import primitives.*;

import java.util.List;

/** Class representing a cylinder in 3D space. */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructs a new Cylinder with the given axis ray, radius, and height.
     *
     * @param axis   The central axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * Returns the normal vector to the cylinder at a given point.
     *
     * @param point The point on the surface of the cylinder.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point point) {
        Point helpPoint = axis.getHead();
        Vector vector = axis.getDirection().normalize();
        Ray ray = new Ray(helpPoint, vector);

        // Check if the point is on the bottom base of the cylinder.
        if (point.equals(helpPoint) || Util.isZero(point.subtract(helpPoint).dotProduct(vector))) {
            // Normal points in the opposite direction of the axis.
            return vector.scale(-1);
        }

        // Check if the point is on the top base of the cylinder.
        if (point.equals(ray.getPoint(height)) ||
                Util.isZero(point.subtract(ray.getPoint(height)).dotProduct(vector)))
            // Normal points in the direction of the axis.
            return vector;

        // If the point is on the curved surface of the cylinder, use the Tube's normal calculation.
        return super.getNormal(point);
    }


    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return null;
    }
}
