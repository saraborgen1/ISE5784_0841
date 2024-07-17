package renderer;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Simple implementation of a ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final Double3 INITIAL_K = Double3.ONE;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;


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

        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Calculates the color at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray      The ray that intersected the point.
     * @return The color at the intersection point.
     */
    private Color calcColor(Intersectable.GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint,ray,MAX_CALC_COLOR_LEVEL,INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * Recursively calculates the color at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray      The ray that intersected the point.
     * @param level    The current recursion level.
     * @param k        The current attenuation factor.
     * @return The color at the intersection point after considering local and global effects.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k){
        Color color = calcLocalEffects(geoPoint, ray,k);
        return 1 == level ? color
                : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculates the global effects (reflections and refractions) at the intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray      The ray that intersected the point.
     * @param level    The current recursion level.
     * @param k        The current attenuation factor.
     * @return The color contribution from global effects.
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        // Get the direction of the incident ray
        Vector v = ray.getDirection();
        // Get the normal vector at the intersection point
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);

        Material material = geoPoint.geometry.getMaterial();
        return calcGlobalEffect(constructRefractedRay(geoPoint.point, v,n),material.kT,level,k)
                .add(calcGlobalEffect(constructReflectedRay(geoPoint.point, v,n),material.kR,level,k));
    }

    /**
     * Calculates the global effect of reflection or refraction for a given ray.
     *
     * @param ray   The ray for which to calculate the global effect.
     * @param level The current recursion level.
     * @param k     The coefficient of the global effect.
     * @param kx    The coefficient of the global effect multiplied by the material's reflection or refraction coefficient.
     * @return The color representing the global effect.
     */
    private Color calcGlobalEffect(Ray ray,Double3 kx, int level, Double3 k) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background :calcColor(gp, ray, level-1, kkx))
                .scale(kx);
    }

    /**
     * Constructs a refracted ray based on the point of intersection, incident vector, and surface normal.
     * The refracted ray is created using the Snell's law.
     *
     * @param point The point of intersection.
     * @param v     The incident vector.
     * @param n     The surface normal.
     * @return A new Ray object representing the refracted ray.
     */
    private Ray constructRefractedRay(Point point,Vector v,Vector n) {
        return new Ray(point, n, v);
    }

    /**
     * Constructs a reflected ray based on the given point, incident vector, and surface normal.
     *
     * @param point The point of intersection.
     * @param v        The incident vector.
     * @param n        The surface normal.
     * @return The reflected ray.
     */
    private Ray constructReflectedRay(Point point,Vector v,Vector n) {
        double vn = alignZero(v.dotProduct(n));

        // If the dot product of v and n is zero, the incident vector is parallel to the surface normal,
        // resulting in no reflection. Return null in this case.
        if (vn == 0) {
            return null;
        }

        // Calculate the reflection direction using the formula: r = v - 2 * (v dot n) * n
        Vector r = v.subtract(n.scale(2 * vn));

        // Create a new ray with the reflected direction starting from the given point.
        return new Ray(point, n, r);
    }

    /**
     * Finds the closest intersection point between the given ray and the geometries in the scene.
     *
     * @param ray The ray for which to find the closest intersection.
     * @return The closest intersection point, or null if no intersection is found.
     */
    private GeoPoint findClosestIntersection(Ray ray){
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return ray.findClosestGeoPoint(intersections);
    }


    /**
     * Calculates the local lighting effects at the given intersection point.
     *
     * @param geoPoint The intersection point.
     * @param ray      The ray that intersected the point.
     * @return The color resulting from the local lighting effects.
     */
    private Color calcLocalEffects(Intersectable.GeoPoint geoPoint, Ray ray,Double3 k) {

        // Start with the emission color of the geometry
        Color color = geoPoint.geometry.getEmission();

        // Get the normal vector at the intersection point
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        // Get the direction of the incoming ray
        Vector v = ray.getDirection();

        double nv = alignZero(n.dotProduct(v));
        // If the dot product is zero, return black (no effect)
        if (isZero(nv))
            return color;

        // Get the material properties of the geometry at the intersection point
        Material material = geoPoint.geometry.getMaterial();


        // Iterate over each light source in the scene
        for (LightSource lightSource : scene.lights) {
            // Get the direction vector from the light source to the intersection point
            Vector l = lightSource.getL(geoPoint.point);
            double nl = alignZero(n.dotProduct(l));
            // Check if the light source contributes to the local effects
            if (nl * nv > 0){
                Double3 ktr=transparency(geoPoint,lightSource,l, n);
                if (!ktr.product(INITIAL_K).lowerThan(MIN_CALC_COLOR_K)) {
                    // Get the intensity of the light source at the intersection point
                    Color iL = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    // Add the diffusive and specular effects to the color
                    color = color.add(
                            iL.scale(calcDiffusive(material, nl)
                                    .add(calcSpecular(material, n, l, nl, v))
                            ));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the diffusive component of the lighting.
     *
     * @param material The material of the intersected geometry.
     * @param nl       The dot product of the normal and the light direction vectors.
     * @return The diffusive component of the lighting.
     */
    private Double3 calcDiffusive(Material material, double nl) {
        Double3 kD = material.kD;
        return kD.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular component of the lighting.
     *
     * @param material The material of the intersected geometry.
     * @param n        The normal vector at the intersection point.
     * @param l        The direction vector from the light source to the intersection point.
     * @param nl       The dot product of the normal and the light direction vectors.
     * @param v        The direction vector of the ray.
     * @return The specular component of the lighting.
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        // Calculate the reflection direction vector
        Vector r = l.subtract(n.scale(2 * nl)).normalize();
        // Calculate the dot product of the view direction and the reflection direction vectors
        double vr = alignZero(v.dotProduct(r));

        Double3 kS = material.kS;
        double specularFactor = Math.pow(Math.max(0, -vr), material.nShininess);

        // Calculate the specular component based on the specular factor
        return (kS.scale(specularFactor));
    }

    /**
     * Checks if the point is unshaded by any other geometry.
     *
     * @param geoPoint    The intersection point.
     * @param light The light source.
     * @param l     The direction vector from the light source to the intersection point.
     * @param n     The normal vector at the intersection point.
     * @param nl    The dot product of the normal and the light direction vectors.
     * @return True if the point is unshaded, false otherwise.
     */
    private boolean unshaded(
            GeoPoint geoPoint, LightSource light, Vector l, Vector n, double nl) {
        //הופכים את כיוון הוקטור
        Vector lightDirection = l.scale(-1).normalize();
        //מזיזים אפסילון בכיוון הנורמל
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA).normalize();
        Point point = geoPoint.point.add(epsVector);
        Ray ray = new Ray(point,n,lightDirection);

        //get the distance
        double maxDistance = light.getDistance(geoPoint.point);

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray, maxDistance);
        if (intersections == null)
            return true;

        for (var item : intersections){
            if (item.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)){
                return false;
            }
        }

        return true;
    }

    private Double3 transparency(GeoPoint geoPoint, LightSource light, Vector l, Vector n){
        //הופכים את כיוון הוקטור
        Vector lightDirection = l.scale(-1).normalize();
        //מזיזים אפסילון בכיוון הנורמל
        Vector epsVector = n.scale(n.dotProduct(l) < 0 ? DELTA : -DELTA).normalize();
        Point point = geoPoint.point.add(epsVector);

        Ray ray = new Ray(point,n,lightDirection);

        //get the distance
        double maxDistance = light.getDistance(geoPoint.point);

        Double3 ktr = Double3.ONE;

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray, maxDistance);
        if (intersections == null)
            return ktr;

        for (var item : intersections){
            // Check if the distance between the intersection point and the geometry is within the maximum distance to the light source
            if (point.distance(item.point) <= maxDistance) {
                // Multiply the transparency coefficient by the transparency factor of the intersected geometry's material
                ktr = ktr.product(item.geometry.getMaterial().kT);

                // If the transparency coefficient falls below the minimum calculation threshold, return a fully opaque value (0 transparency)
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }

        return ktr;
    }
}
