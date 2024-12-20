package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Polygon class represents a two-dimensional polygon in a 3D Cartesian coordinate system.
 * The polygon must be convex.
 */
public class Polygon extends Geometry {

    // List of polygon's vertices
    protected final List<Point> vertices;

    // Associated plane in which the polygon lies
    protected final Plane plane;

    // The size of the polygon - the number of vertices in the polygon
    private final int size;

    /**
     * Polygon constructor based on a list of vertices. The list must be ordered by edge path.
     * The polygon must be convex.
     *
     * @param vertices List of vertices according to their order by edge path.
     * @throws IllegalArgumentException in any case of illegal combination of vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consecutive vertices are the same point</li>
     *                                  <li>The vertices are not in the same plane</li>
     *                                  <li>The order of vertices is not according to edge path</li>
     *                                  <li>Three consecutive vertices lie on the same line (180&#176; angle between two consecutive edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // No need for more tests for a Triangle

        Vector n = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException because of Zero Vector if they connect three vertices that lie on the same line.
        // Generate the direction of the polygon according to the angle between the last and first edge being less than 180 degrees.
        // It is held by the sign of its dot product with the normal. If all the rest of the consecutive edges generate the same sign, the polygon is convex.
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lie in the same plane");
            // Test that the consecutive edges have the same sign
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    /**
     * Finds geometric intersections of a given ray with the polygon.
     *
     * @param ray The ray to intersect with the polygon.
     * @param maxDistance The maximum distance to consider for intersections.
     * @return A list of GeoPoint objects representing the intersections.
     * If there are no intersections, the list will be null.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // Find intersection points with the plane
        var intersections = plane.findGeoIntersections(ray, maxDistance);

        // If no intersection with the plane, return null
        if (intersections == null)
            return null;

        // Calculate vectors from ray head to each vertex of the polygon
        List<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            vectors.add(this.vertices.get(i).subtract(ray.getHead()));
        }

        // Calculate normals of the polygon using the cross product of edge vectors
        List<Vector> normals = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            if ((i + 1) < size)
                normals.add(vectors.get(i).crossProduct(vectors.get(i + 1)));
            else
                normals.add(vectors.get(i).crossProduct(vectors.getFirst()));
        }

        // Calculate the dot product of normals and ray direction
        List<Double> dotProducts = new ArrayList<>();
        for (Vector normal : normals) {
            dotProducts.add(alignZero(normal.dotProduct(ray.getDirection())));
        }

        // If all dot products have the same sign, the ray intersects the polygon
        boolean allPositive = dotProducts.stream().allMatch(d -> d > 0);
        boolean allNegative = dotProducts.stream().allMatch(d -> d < 0);

        if (allPositive || allNegative)
            return List.of(new GeoPoint(this, intersections.getFirst().point));
        return null;
    }
}
