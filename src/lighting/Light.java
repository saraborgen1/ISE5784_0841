package lighting;

import primitives.Color;

/**
 * Abstract class representing a light source. This class defines common
 * properties and behavior for all light sources.
 */
abstract class Light {

    /**
     * The intensity (color and brightness) of the light source.
     */
    protected Color intensity;

    /**
     * Constructs a new Light with the given intensity.
     *
     * @param intensity The intensity of the light source.
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light source.
     *
     * @return The intensity of the light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
