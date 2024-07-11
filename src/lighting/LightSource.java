package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a scene.
 * This interface defines methods to retrieve the intensity of the light at a point,
 * and the direction vector from the light source to that point.
 */
public interface LightSource {

    /**
     * Returns the intensity of the light source at a given point.
     *
     * @param point The point at which to calculate the intensity.
     * @return The intensity of the light source at the given point.
     */
    Color getIntensity(Point point);

    /**
     * Returns the direction vector from the light source to a given point.
     *
     * @param point The point at which to calculate the direction vector.
     * @return The direction vector from the light source to the given point.
     */
    Vector getL(Point point);

    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point The point to calculate the distance to.
     * @return The distance from the light source to the given point.
     */
    double getDistance(Point point);
}
