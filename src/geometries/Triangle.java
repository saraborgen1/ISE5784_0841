package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 *  * Triangle class represents a triangle in 3D Cartesian coordinate system.
 */
public class Triangle extends Polygon {
    /**
     * Constructor for Triangle class.
     *
     * @param point1 The first vertex of the triangle.
     * @param point2 The second vertex of the triangle.
     * @param point3 The third vertex of the triangle.
     */
    public Triangle(Point point1, Point point2, Point point3) {
        super(point1, point2, point3);
    }

    /**
     * Finds intersection points between a ray and the object.
     *
     * @param ray The ray to intersect with the object.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // Find intersection points with the plane
        var intersections = plane.findIntersections(ray);

        // If no intersection with the plane, return null
        if (intersections == null)
            return null;

        // Calculate vectors from ray head to each vertex of the triangle
        Vector vector1 = this.vertices.get(0).subtract(ray.getHead());
        Vector vector2 = this.vertices.get(1).subtract(ray.getHead());
        Vector vector3 = this.vertices.get(2).subtract(ray.getHead());

        // Calculate normals of the triangle using cross product of edge vectors
        Vector normal1 = vector1.crossProduct(vector2);
        Vector normal2 = vector2.crossProduct(vector3);
        Vector normal3 = vector3.crossProduct(vector1);

        // Calculate dot product of normals and ray direction
        double d1 = alignZero(normal1.dotProduct(ray.getDirection()));
        double d2 = alignZero(normal2.dotProduct(ray.getDirection()));
        double d3 = alignZero(normal3.dotProduct(ray.getDirection()));

        // If all dot products have the same sign, the ray intersects the triangle
        if ((d1 > 0 && d2 > 0 && d3 > 0) || (d1 < 0 && d2 < 0 && d3 < 0))
            return List.of(new GeoPoint(this, intersections.getFirst()));

        // Otherwise, no intersection with the triangle
        return null;
    }
}
