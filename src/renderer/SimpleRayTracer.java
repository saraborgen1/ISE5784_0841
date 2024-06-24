package renderer;

import primitives.*;
import scene.*;

import java.util.List;

/**
 * Simple implementation of a ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a new SimpleRayTracer with the given scene.
     * @param scene The scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray through the scene and returns the color of the intersection point.
     * @param ray The ray to trace.
     * @return The color of the intersection point.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null) {
            return scene.background;
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    /**
     * Calculates the color of the given intersection point.
     * @param point The intersection point.
     * @return The color of the intersection point.
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}
