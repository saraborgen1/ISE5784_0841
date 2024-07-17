package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class representing a plane in 3D space.
 */
public class Plane extends Geometry {

    private final Point q;
    private final Vector normal;

    /**
     * Constructs a new Plane from three points.
     *
     * @param point1 The first point.
     * @param point2 The second point.
     * @param point3 The third point.
     */
    public Plane(Point point1, Point point2, Point point3) {
        Vector vector1 = point2.subtract(point1);
        Vector vector2 = point3.subtract(point1);
        this.normal = vector1.crossProduct(vector2).normalize();
        this.q = point1;
    }

    /**
     * Constructs a new Plane with a reference point and normal vector.
     *
     * @param point  The reference point on the plane.
     * @param normal The normal vector to the plane.
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane.
     *
     * @return The normal vector.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Returns the normal vector to the plane at a given point.
     *
     * @param point The point on the geometry.
     * @return The normal vector.
     */
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Finds intersection point(s) between a ray and the plane.
     *
     * @param ray The ray to intersect with the plane.
     * @param maxDistance The maximum distance to consider for intersections.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // Calculate the numerator in the plane intersection formula
        double numerator = normal.dotProduct(q.subtract(ray.getHead()));
        // Calculate the denominator in the plane intersection formula
        double denominator = normal.dotProduct(ray.getDirection());

        // If the denominator is zero, the ray is parallel to the plane and there is no intersection
        if (isZero(denominator)) {
            return null;
        }

        // Calculate the distance from the ray's head to the intersection point
        double t = numerator / denominator;

        // If the distance is less than or equal to zero or greater than the maximum distance, there is no intersection
        if (t <= 0 || alignZero(t - maxDistance) >= 0) {
            return null;
        }

        // Calculate the intersection point
        Point point = ray.getPoint(t);
        // Return a list containing the intersection point
        return List.of(new GeoPoint(this, point));
    }
}
