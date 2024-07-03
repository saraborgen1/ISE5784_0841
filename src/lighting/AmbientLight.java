package lighting;

import primitives.*;

/**
 * Class to represent ambient lighting in a scene.
 * The class handles the intensity of the ambient light based on given color and attenuation factor.
 * The intensity is calculated as a product of the original color and the attenuation factor.
 * This class is immutable.
 * extends from Light.
 */
public class AmbientLight extends Light {


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
        super(ia.scale(ka));
    }

    /**
     * Constructor to initialize the ambient light with a color and an attenuation factor.
     *
     * @param ia The original color intensity.
     * @param ka The attenuation factor (as double).
     */
    public AmbientLight(Color ia, double ka) {
        super(ia.scale(ka));
    }


}
