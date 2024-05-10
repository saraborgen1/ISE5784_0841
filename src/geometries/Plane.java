package geometries;
import primitives.*;

/**
 * Class representing a plane in 3D space.
 */
public class Plane {
    private final Point q;
    private final Vector normal;

    /**
     * Constructs a new Plane from three points.
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.normal=null;
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

    public Vector getNormal(){
        return normal;
    }

    public Vector getNormal(Point p){
        return normal;
    }
}
