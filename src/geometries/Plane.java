package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Class representing a plane in 3D space.
 */
public class Plane implements Geometry{
    private final Point q;
    private final Vector normal;

    /**
     * Constructs a new Plane from three points.
     * @param point1 The first point.
     * @param point2 The second point.
     * @param point3 The third point.
     */
    public Plane(Point point1, Point point2, Point point3) {
        Vector vector1=point2.subtract(point1);
        Vector vector2=point3.subtract(point1);
        this.normal=vector1.crossProduct(vector2).normalize();
        this.q=point1;
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

        if (isZero(denominator)) {
            return null;
        }

        double t = numerator / denominator;

        if (t <= 0) {
            return null;
        }

        Point point = ray.getPoint(t);
        return List.of(point);
    }
}
