package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

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
     * @param p The point on the surface of the sphere
     * @return normal vector at the given point.
     */
    public Vector getNormal(Point p) {
        return p.subtract(this.center).normalize();
    }

    /**
     * Finds intersection points between a ray and the object.
     * @param ray The ray to intersect with the object.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    public List<Point> findIntersections(Ray ray) {
        Vector u;
        try {
            // Calculate vector from ray head to the center of the sphere
            u = center.subtract(ray.getHead());
        } catch (IllegalArgumentException e) { // Ray starts at center of the sphere
            // Return a list containing the point on the surface of the sphere in the ray direction
            return List.of(center.add(ray.getDirection().scale(radius)));
        }

        // Calculate projection of u onto ray direction
        double tm = ray.getDirection().dotProduct(u);
        // Calculate distance from ray to sphere center
        double d = Math.sqrt(u.lengthSquared() - tm * tm);

        // If the distance from ray to sphere center is greater than the sphere radius, ray misses the sphere
        if (Util.alignZero(d - radius) > 0)
            return null;

        // Calculate half of the chord length
        double th = Math.sqrt(radius * radius - d * d);
        // Calculate distances from ray head to intersection points
        double t1 = tm - th;
        double t2 = tm + th;

        // If both intersection distances are negative, ray origin is inside the sphere, return null
        if (Util.alignZero(t1) <= 0 && Util.alignZero(t2) <= 0) {
            return null;
        }

        // Initialize list to store intersection points
        List<Point> intersections = new ArrayList<>();

        // Add intersection points to the list if they are in front of the ray
        if (Util.alignZero(t1) > 0)
            intersections.add(ray.getPoint(t1));
        if (Util.alignZero(t2) > 0)
            intersections.add(ray.getPoint(t2));

        // If no intersection points were found, return null
        if (intersections.isEmpty()) {
            return null;
        }

        // Return the list of intersection points
        return List.copyOf(intersections);
    }

}
