package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Geometries class to represent a collection of geometric shapes
 * Implements the Composite design pattern
 * Class representing a collection of geometric shapes that are intersectable.
 */
public class Geometries extends Intersectable {

    private final List<Intersectable> geometries = new LinkedList<>();

    /** Default constructor for Geometries.     */
    public Geometries() {
    }

    /**
     * Constructor for Geometries initializing the list with given geometries.
     *
     * @param geometries List of geometries to add to the composite.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds given geometries to the composite.
     *
     * @param geometries Geometries to add.
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    /**
     * Finds geometric intersections of a given ray with the composite geometries.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of GeoPoint objects representing the intersections.
     * If there are no intersections, the list will be empty.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        List<GeoPoint> intersections = null;

        // Iterate through each geometry in the collection
        for (Intersectable geometry : geometries) {
            // Find the intersections of the ray with the current geometry
            List<GeoPoint> geoPoints = geometry.findGeoIntersections(ray);
            // If there are intersections
            if (geoPoints != null) {
                // If the intersections list is null, initialize it with the found intersections
                if (intersections == null) {
                    intersections = new ArrayList<>(geoPoints);
                }
                // Otherwise, add the found intersections to the existing list
                else {
                    intersections.addAll(geoPoints);
                }
            }
        }

        return intersections;
    }
}
