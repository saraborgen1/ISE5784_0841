package geometries;
import primitives.*;

import java.util.List;

public class Triangle extends Polygon{
    /**
     * Constructor for Triangle class.
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Finds intersection points between a ray and the object.
     * @param ray The ray to intersect with the object.
     * @return A list containing the intersection point(s), or null if no intersection occurs.
     */
    public List<Point> findIntersections(Ray ray){
        // Find intersection points with the plane
        var intersectionPoints = plane.findIntersections(ray);

        // If no intersection with the plane, return null
        if (intersectionPoints == null)
            return null;

        // Calculate vectors from ray head to each vertex of the triangle
        Vector v1 = this.vertices.get(0).subtract(ray.getHead());
        Vector v2 = this.vertices.get(1).subtract(ray.getHead());
        Vector v3 = this.vertices.get(2).subtract(ray.getHead());

        // Calculate normals of the triangle using cross product of edge vectors
        Vector n1 = v1.crossProduct(v2);
        Vector n2 = v2.crossProduct(v3);
        Vector n3 = v3.crossProduct(v1);

        // Calculate dot product of normals and ray direction
        double d1 = n1.dotProduct(ray.getDirection());
        double d2 = n2.dotProduct(ray.getDirection());
        double d3 = n3.dotProduct(ray.getDirection());

        // If all dot products have the same sign, the ray intersects the triangle
        if ((d1 > 0 && d2 > 0 && d3 > 0) || (d1 < 0 && d2 < 0 && d3 < 0))
            return List.of(intersectionPoints.get(0));

        // Otherwise, no intersection with the triangle
        return null;
    }
}
