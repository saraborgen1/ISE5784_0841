package renderer;

import primitives.*;
import scene.Scene;

/**
 * Abstract base class for ray tracing algorithms.
 */
public abstract class RayTracerBase {

    protected Scene scene;

    /**
     * Constructs a new RayTracerBase with the given scene.
     *
     * @param scene The scene to be traced.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces the given ray through the scene and returns the color of the intersection point.
     *
     * @param ray The ray to trace.
     * @return The color of the intersection point.
     */
    public abstract Color traceRay(Ray ray);
}
