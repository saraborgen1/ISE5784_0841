package geometries;
import primitives.*;

/**
 * Interface representing a geometric body in 3D space.
 */
public interface Geometry extends Intersectable {

    /**
     * Calculates the normal vector to the geometry at a given point.
     * @param point The point on the geometry.
     * @return The normal vector to the geometry at the given point.
     */
     Vector getNormal(Point point);
}
