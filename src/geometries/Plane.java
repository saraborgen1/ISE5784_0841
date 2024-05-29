package geometries;
import primitives.*;

import java.util.List;

/**
 * Class representing a plane in 3D space.
 */
public class Plane implements Geometry{
    private final Point q;
    private final Vector normal;

    /**
     * Constructs a new Plane from three points.
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1=p2.subtract(p1);
        Vector v2=p3.subtract(p1);
        this.normal=v1.crossProduct(v2).normalize();
        this.q=p1;
    }

    /**
     * Constructs a new Plane with a reference point and normal vector.
     * @param point The reference point on the plane.
     * @param normal The normal vector to the plane.
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane.
     * @return The normal vector.
     */
    public Vector getNormal(){
        return normal;
    }

    /**
     * Returns the normal vector to the plane at a given point.
     * @param point The point on the geometry.
     * @return The normal vector.
     */
    public Vector getNormal(Point point){
        return normal;
    }

    /**
     * Finds intersection point(s) between a ray and the plane.
     * @param ray The ray to intersect with the plane.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    public List<Point> findIntersections(Ray ray) {

        double numerator = normal.dotProduct(q.subtract(ray.getHead()));
        double denominator = normal.dotProduct(ray.getDirection());

        if (denominator == 0) {
            return null;
        }

        double t = numerator / denominator;

        if (t <= 0) {
            return null;
        }

        Point p = ray.getPoint(t);
        return List.of(p);
    }
}
