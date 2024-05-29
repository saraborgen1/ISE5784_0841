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
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     * @param point The point on the surface of the sphere
     * @return normal vector at the given point.
     */
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }

    /**
     * Finds intersection points between a ray and the object.
     * @param ray The ray to intersect with the object.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    public List<Point> findIntersections(Ray ray) {
        Vector u;
        try {
            u = center.subtract(ray.getHead());
        } catch (IllegalArgumentException e) {
            return List.of(ray.getPoint(radius));
        }

        double tm = ray.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);

        if (Util.alignZero(d - radius) >= 0)
            return null;

        double th = Math.sqrt(radius * radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        if (Util.alignZero(t1) <= 0 && Util.alignZero(t2) <= 0) {
            return null;
        }

        List<Point> intersections = new ArrayList<>();

        if (Util.alignZero(t1) > 0)
            intersections.add(ray.getPoint(t1));
        if (Util.alignZero(t2) > 0)
            intersections.add(ray.getPoint(t2));

        if (intersections.isEmpty()) {
            return null;
        }

        return List.copyOf(intersections);
    }
}
