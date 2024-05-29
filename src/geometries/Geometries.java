package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor for Geometries.
     */
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
        if (geometries != null) {
            this.geometries.addAll(List.of(geometries));
        }
    }

    /**
     * Finds the intersections of a given ray with the geometries contained within this object.
     * @param ray the Ray to intersect with the geometries
     * @return a list of Points where the ray intersects the geometries.
     * If there are no intersections, an empty list is returned.
     */
    public List<Point> findIntersections(Ray ray) {

        List<Point> pointsList = null;
        for (Intersectable intersectable : geometries) {
            List<Point> geometryIntersections = intersectable.findIntersections(ray);
            if (geometryIntersections != null) {
                if (pointsList == null) pointsList = new LinkedList<>();
                pointsList.addAll(geometryIntersections);
            }
        }
        return pointsList;
    }
}
