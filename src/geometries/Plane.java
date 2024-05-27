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
     * @param p The point on the geometry.
     * @return The normal vector.
     */
    public Vector getNormal(Point p){
        return normal;
    }

    /**
     * Finds intersection point(s) between a ray and the plane.
     * @param ray The ray to intersect with the plane.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    public List<Point> findIntersections(Ray ray) {
        // Calculate numerator of the parametric equation
        double numerator = normal.dotProduct(q.subtract(ray.getHead()));
        // Calculate denominator of the parametric equation
        double denominator = normal.dotProduct(ray.getDirection());

        // If the denominator is 0, the ray is parallel to the plane
        if (denominator == 0) {
            return null;
        }

        // Calculate parameter t of intersection point
        double t = numerator / denominator;

        // If t is less than or equal to 0, the intersection point is behind the ray's head
        if (t <= 0) {
            return null;
        }

        // Calculate intersection point using the parametric equation of the ray
        Point p = ray.getPoint(t);

        // Return a list containing the intersection point
        return List.of(p);
    }
}
