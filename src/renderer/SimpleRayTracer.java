package renderer;

import geometries.*;
import primitives.*;
import scene.*;


/**
 * Simple implementation of a ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a new SimpleRayTracer with the given scene.
     *
     * @param scene The scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray through the scene and returns the color of the intersection point.
     *
     * @param ray The ray to trace.
     * @return The color of the intersection point.
     */
    @Override
    public Color traceRay(Ray ray) {

        // Find the intersections of the ray with the geometries in the scene
        var intersections = scene.geometries.findGeoIntersections(ray);

        // If no intersections are found, return the background color of the scene
        return intersections == null
                ? scene.background
                // Otherwise, calculate the color at the closest intersection point
                : calcColor(ray.findClosestGeoPoint(intersections));
    }

    /**
     * Calculates the color at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @return The color at the intersection point.
     */
    private Color calcColor(Intersectable.GeoPoint geoPoint) {

        // Return the sum of the ambient light intensity and the emission of the geometry
        return scene.ambientLight.getIntensity()
                .add(geoPoint.geometry.getEmission());
    }
}
