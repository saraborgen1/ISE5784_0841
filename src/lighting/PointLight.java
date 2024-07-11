package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point light source in a scene.
 * Extends the abstract class Light and implements the LightSource interface.
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the light source in 3D space.
     */
    protected Point position;
    /**
     * Constant attenuation factor (kC) affecting the intensity of the light source.
     * This factor is independent of the distance from the light source.
     * Linear attenuation factor (kL) affecting the intensity of the light source.
     * This factor decreases the intensity linearly with the distance from the light source.
     * Quadratic attenuation factor (kQ) affecting the intensity of the light source.
     * This factor decreases the intensity quadratically with the distance from the light source.
     */
    private double kC = 1, kL = 0, kQ = 0;

    /**
     * Constructs a PointLight with given intensity and position.
     *
     * @param intensity The intensity and color of the light.
     * @param position  The position of the light source in 3D space.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the position of the point light source.
     *
     * @param position The new position of the light source.
     * @return This PointLight object for chaining method calls.
     */
    public PointLight setPosition(Point position) {
        this.position = position;
        return this;
    }

    /**
     * Sets the constant attenuation coefficient of the light source.
     *
     * @param kC The constant attenuation coefficient to set.
     * @return This PointLight object for chaining method calls.
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation coefficient of the light source.
     *
     * @param kL The linear attenuation coefficient to set.
     * @return This PointLight object for chaining method calls.
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation coefficient of the light source.
     *
     * @param kQ The quadratic attenuation coefficient to set.
     * @return This PointLight object for chaining method calls.
     */
    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }


    @Override
    public Color getIntensity(Point point) {
        double distance = position.distanceSquared(point);
        return intensity.scale(1 / (kC + kL * Math.sqrt(distance) + kQ * distance));
    }

    @Override
    public Vector getL(Point point) {

        // if the point is the same as the light source, return null
        if (point.equals(position))
            return null;

        // otherwise, return the normalized vector from the light source to the point
        return point.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

}
