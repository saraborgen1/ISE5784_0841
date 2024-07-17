package lighting;

import primitives.*;

/**
 * Represents a spotlight in a scene, which is a point light with a specific direction.
 * Extends the PointLight class and adds a direction property.
 */
public class SpotLight extends PointLight {

    /**
     * The direction of the spotlight.
     */
    private Vector direction;

    /**
     * Constructs a SpotLight with given intensity, position, and direction.
     *
     * @param intensity The intensity and color of the light.
     * @param position  The position of the light source in 3D space.
     * @param direction The direction of the spotlight.
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public SpotLight setPosition(Point position) {
        return (SpotLight) super.setPosition(position);
    }

    @Override
    public SpotLight setKC(double kC) {
        return (SpotLight) super.setKC(kC);
    }

    @Override
    public SpotLight setKL(double kL) {
        return (SpotLight) super.setKL(kL);
    }

    @Override
    public SpotLight setKQ(double kQ) {
        return (SpotLight) super.setKQ(kQ);
    }

    @Override
    public Color getIntensity(Point point) {
        return super.getIntensity(point)
                .scale(Math.max
                        (0, direction.dotProduct(super.getL(point)))
                );
    }
}
