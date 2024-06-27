package primitives;

import java.util.List;

import geometries.Intersectable.GeoPoint;

/**
 * Class representing a ray in 3D space.
 */
public class Ray {

    private final Point head;
    private final Vector direction;

    /**
     * Constructs a new Ray with the given head (starting point) and direction.
     *
     * @param head      The starting point of the Ray.
     * @param direction The direction Vector of the Ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return The direction vector.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns the head (starting point) of the ray.
     *
     * @return The head point.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Calculates a point along the ray's direction at a parameter t.
     *
     * @param t The parameter indicating the distance from the ray's head.
     * @return The point along the ray's direction at distance t from the head.
     */
    public Point getPoint(double t) {
        return Util.isZero(t) ? head : head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the ray's head from a list of intersection points.
     *
     * @param intersections List of intersection points.
     * @return The closest intersection point, or null if the list is empty or null.
     */
    public Point findClosestPoint(List<Point> intersections) {
        return intersections == null ? null
                : findClosestGeoPoint(intersections.stream()
                .map(p -> new GeoPoint(null, p))
                .toList())
                .point;
    }

    /**
     * Finds the closest GeoPoint to the ray's head from a list of GeoPoints.
     *
     * @param intersections List of GeoPoints.
     * @return The closest GeoPoint, or null if the list is empty or null.
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> intersections) {

        // If the list is null or empty, return null
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }

        GeoPoint closestPoint = intersections.getFirst();
        double distance = head.distance(closestPoint.point);

        // Iterate through each point in the list
        for (GeoPoint geoPoint : intersections) {
            double tempDistance = head.distance(geoPoint.point);
            // If the distance is shorter than the current closest distance, update the closest point
            if (tempDistance < distance) {
                distance = tempDistance;
                closestPoint = geoPoint;
            }
        }

        return closestPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
}
