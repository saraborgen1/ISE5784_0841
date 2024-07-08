package renderer;

import geometries.Intersectable.GeoPoint;
import geometries.*;
import lighting.*;
import primitives.*;
import primitives.Color;
import primitives.Point;
import scene.*;

import java.util.List;


import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Simple implementation of a ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;

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
                : calcColor(ray.findClosestGeoPoint(intersections), ray);
    }

    /**
     * Calculates the color at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray The ray that intersected the point.
     * @return The color at the intersection point.
     */
    private Color calcColor(Intersectable.GeoPoint geoPoint, Ray ray) {

        // Return the sum of the ambient light intensity and the emission of the geometry
        return scene.ambientLight.getIntensity()
                .add(calcLocalEffects(geoPoint, ray));
    }

    /**
     * Calculates the local lighting effects at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray The ray that intersected the point.
     * @return The color resulting from the local lighting effects.
     */
    private Color calcLocalEffects(Intersectable.GeoPoint geoPoint, Ray ray) {

        // Get the normal vector at the intersection point
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        // Get the direction of the incoming ray
        Vector v = ray.getDirection();

        double nv = alignZero(n.dotProduct(v));
        // If the dot product is zero, return black (no effect)
        if (isZero(nv))
            return Color.BLACK;

        // Get the material properties of the geometry at the intersection point
        Material material = geoPoint.geometry.getMaterial();
        // Start with the emission color of the geometry
        Color color = geoPoint.geometry.getEmission();

        // Iterate over each light source in the scene
        for (LightSource lightSource : scene.lights) {
            // Get the direction vector from the light source to the intersection point
            Vector l = lightSource.getL(geoPoint.point);
            double nl = alignZero(n.dotProduct(l));
            // Check if the light source contributes to the local effects
            if ((nl * nv > 0)&& unshaded(geoPoint,lightSource,l,n,nl)) {
                // Get the intensity of the light source at the intersection point
                Color iL = lightSource.getIntensity(geoPoint.point);
                // Add the diffusive and specular effects to the color
                color = color.add(
                        iL.scale(calcDiffusive(material, nl)
                                .add(calcSpecular(material, n, l, nl, v))
                ));
            }
        }
        return color;
    }

    /**
     * Calculates the diffusive component of the lighting.
     *
     * @param material The material of the intersected geometry.
     * @param nl The dot product of the normal and the light direction vectors.
     * @return The diffusive component of the lighting.
     */
    private Double3 calcDiffusive(Material material, double nl) {
        Double3 kD=material.kD;
        return kD.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular component of the lighting.
     *
     * @param material The material of the intersected geometry.
     * @param n The normal vector at the intersection point.
     * @param l The direction vector from the light source to the intersection point.
     * @param nl The dot product of the normal and the light direction vectors.
     * @param v The direction vector of the ray.
     * @return The specular component of the lighting.
     */
    private Double3 calcSpecular(Material material,Vector n,Vector l, double nl, Vector v){
        // Calculate the reflection direction vector
        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        // Calculate the dot product of the view direction and the reflection direction vectors
        double vr = alignZero(v.dotProduct(r));

        Double3 kS=material.kS;
        double specularFactor = Math.pow(Math.max(0, -vr), material.nShininess);

        // Calculate the specular component based on the specular factor
        return (kS.scale(specularFactor));
    }

    /**
     * Checks if the point is unshaded by any other geometry.
     *
     * @param gp The intersection point.
     * @param light The light source.
     * @param l The direction vector from the light source to the intersection point.
     * @param n The normal vector at the intersection point.
     * @param nl The dot product of the normal and the light direction vectors.
     * @return True if the point is unshaded, false otherwise.
     */
    private boolean unshaded(
            GeoPoint gp, LightSource light, Vector l, Vector n, double nl){
        //הופכים את כיוון הוקטור
        Vector lightDirection = l.scale(-1).normalize();
        //מזיזים אפסילון בכיוון הנורמל
        Vector epsVector = n.scale(nl<0? DELTA: -DELTA).normalize();
        Point point=gp.point.add(epsVector);
        Ray ray=new Ray(point,lightDirection);

        List<GeoPoint> intersections=scene.geometries.findGeoIntersections(ray);
        if (intersections==null)
            return true;

        // Check each intersection point to see if it occludes the light source
        for (GeoPoint intersection : intersections) {
            // Calculate d1 (distance from point to light source)
            double d1 = light.getDistance(intersection.point);
            // Calculate d2 (distance from original point of ray to intersection point)
            double d2 = intersection.point.distance(point);

            // If d1 is greater than d2, return false (not occluded)
            if (d1 > d2)
                return false;
        }

        return true;
    }
}
