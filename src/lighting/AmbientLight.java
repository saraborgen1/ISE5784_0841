package lighting;

import primitives.*;

/**
 * Class to represent ambient lighting in a scene.
 * The class handles the intensity of the ambient light based on given color and attenuation factor.
 * The intensity is calculated as a product of the original color and the attenuation factor.
 * This class is immutable.
 */
public class AmbientLight {

    /**
     * The intensity of the ambient light
     */
    private final Color intensity;
    /**
     * Constant for no ambient light with black color and zero attenuation
     */
    public static AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * Constructor to initialize the ambient light with a color and an attenuation factor.
     *
     * @param ia The original color intensity.
     * @param ka The attenuation factor (as Double3).
     */
    public AmbientLight(Color ia, Double3 ka) {
        this.intensity = ia.scale(ka);
    }

    /**
     * Constructor to initialize the ambient light with a color and an attenuation factor.
     *
     * @param ia The original color intensity.
     * @param ka The attenuation factor (as double).
     */
    public AmbientLight(Color ia, double ka) {
        this.intensity = ia.scale(ka);
    }

    /**
     * Getter for the intensity of the ambient light.
     *
     * @return The intensity of the ambient light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
