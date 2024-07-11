package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class representing a sphere in 3D space.
 */
public class Sphere extends RadialGeometry {

    private final Point center;
    private final double DELTA = 0.000001;

    /**
     * Constructs a new Sphere with the given center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     *
     * @param point The point on the surface of the sphere
     * @return normal vector at the given point.
     */
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }

    /**
     * Finds intersection points between a ray and the object.
     *
     * @param ray The ray to intersect with the object.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

        Vector u;
        try {
            // Calculate vector from ray's head to the center of the object
            u = center.subtract(ray.getHead());
        } catch (IllegalArgumentException e) {
            // If the ray's head is at the center, return the point at the radius distance along the ray
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }

        // Project vector u onto the ray's direction
        double tm = ray.getDirection().dotProduct(u);
        // Calculate the perpendicular distance from the center to the ray
        double d = Math.sqrt(u.lengthSquared() - tm * tm);

        // If the perpendicular distance is greater than the radius, there's no intersection
        if (alignZero(d - radius) >= 0)
            return null;

        // Calculate the distance from the point of perpendicular intersection to the intersection points
        double th = Math.sqrt(radius * radius - d * d);
        // Calculate the distances along the ray to the intersection points
        double t1 = tm - th;
        double t2 = tm + th;

        // If both distances are less than or equal to zero, there's no intersection
        if (alignZero(t1) <= 0 && alignZero(t2) <= 0) {
            return null;
        }

        List<GeoPoint> intersections = new ArrayList<>();

        // Add the intersection point at t1 if it is greater than zero
        if (alignZero(t1) > 0 && alignZero(t1 - maxDistance) <= 0)
            intersections.add(new GeoPoint(this, ray.getPoint(t1)));
        // Add the intersection point at t2 if it is greater than zero
        if (alignZero(t2) > 0 && alignZero(t2 - maxDistance) <= 0)
            intersections.add(new GeoPoint(this, ray.getPoint(t2)));

        // If there are no valid intersection points, return null
        if (intersections.isEmpty()) {
            return null;
        }

        // Sort the intersection points by their distance from the ray's head
        intersections.sort(Comparator.comparingDouble(p -> p.point.distance(ray.getHead())));

        return List.copyOf(intersections);
    }
}
