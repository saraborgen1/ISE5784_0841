package lighting;

import primitives.*;

/**
 * Class representing a directional light source, which emits light in a specific direction
 * and does not have an attenuation factor over distance.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The direction of the light.
     */
    private Vector direction;

    /**
     * Constructs a new DirectionalLight with the given intensity and direction.
     *
     * @param intensity The intensity of the light source.
     * @param direction The direction of the light.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point point) {
        return intensity;
    }

    @Override
    public Vector getL(Point point) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
