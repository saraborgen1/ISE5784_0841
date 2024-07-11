package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;


/**
 * Interface representing an intersect object in 3D space.
 */
public abstract class Intersectable {

    /**
     * Finds intersections of a given ray with the intersectable object.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of intersection points. If there are no intersections, the list will be empty.
     */
    public final List<Point> findIntersections(Ray ray) {
        List<GeoPoint> geometries = findGeoIntersections(ray);
        return geometries == null ? null
                : geometries.stream()
                .map(gp -> gp.point)
                .toList();
    }

    /**
     * find all GeoPoints that intersect with a ray
     *
     * @param ray to find intersections with
     * @return list of intersection points
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
    }


    /**
     * Public method findGeoIntersections for finding GeoPoints of intersections
     * between the intersectable object and a given ray.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of GeoPoint objects representing the intersections.
     * If there are no intersections, the list will be empty.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray,double maxDistance) {
        return findGeoIntersectionsHelper(ray,maxDistance);
    }

    /**
     * Protected method findGeoIntersectionsHelper for finding GeoPoints of
     * intersections between the intersectable object and a given ray. This method
     * should be implemented in subclasses.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of GeoPoint objects representing the intersections.
     * If there are no intersections, the list will be empty.
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);


    /**
     * A passive data structure (PDS) representing a point of intersection of the ray with the shape
     * and the shape it intersects.
     */
    public static class GeoPoint {
        /**
         * The intersecting geometry.
         */
        public Geometry geometry;
        /**
         * The intersection point of the ray
         */
        public Point point;

        /**
         * Constructs a GeoPoint with the given geometry and point.
         *
         * @param geometry intersecting geometry.
         * @param point    intersection point of the ray.
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) obj;
            return Objects.equals(geometry, geoPoint.geometry) && Objects.equals(point, geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
