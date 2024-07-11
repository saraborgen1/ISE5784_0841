package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a tube in 3D space.
 */
public class Tube extends RadialGeometry {
    protected final Ray axis;

    /**
     * Constructs a new Tube with the given axis ray and radius.
     *
     * @param axis   The central axis ray of the tube.
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at a given point.
     *
     * @param point The point on the surface of the tube.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point point) {
        double t = axis.getDirection().dotProduct(point.subtract(axis.getHead()));
        Point helpPoint = axis.getPoint(t);
        return point.subtract(helpPoint).normalize();
    }

    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }
}
