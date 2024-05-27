package geometries;

import primitives.*;

import java.util.List;

/**
 *Interface representing an intersectable object in 3D space.
 */
public interface Intersectable {
    /**
     * Finds intersections of a given ray with the intersectable object.
     * @param ray The ray to intersect with the object.
     * @return A list of intersection points. If there are no intersections, the list will be empty.
     */
    List<Point> findIntersections(Ray ray);
}
