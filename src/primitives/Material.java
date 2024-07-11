package primitives;

/**
 * Class representing the material properties of a geometric object.
 */
public class Material {

    /**
     * Diffuse reflection coefficient (kD).
     * This coefficient determines how much the surface diffuses light.
     * Specular reflection coefficient (kS).
     * This coefficient determines how much the surface reflects light in a mirror-like manner.
     * Transparency attenuation coefficient (kT).
     * This coefficient determines how much the surface attenuates light transparency.
     * Reflection attenuation coefficient (kR).
     * This coefficient determines how much the surface attenuates light reflection.
     */
    public Double3
            kD = Double3.ZERO,
            kS = Double3.ZERO,
            kT = Double3.ZERO,
            kR = Double3.ZERO;


    /**
     * Shininess factor.
     * This factor determines the shininess of the surface for specular reflection.
     */
    public int nShininess = 1;

    /**
     * Sets the diffuse reflection coefficient (kD) using a Double3 value.
     *
     * @param kD The new diffuse reflection coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (kD) using a double value.
     *
     * @param kD The new diffuse reflection coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) using a Double3 value.
     *
     * @param kS The new specular reflection coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) using a double value.
     *
     * @param kS The new specular reflection coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient (kT) using a Double3 value.
     *
     * @param kT The new transparency attenuation coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient (kT) using a double value.
     *
     * @param kT The new transparency attenuation coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (kR) using a Double3 value.
     *
     * @param kR The new reflection attenuation coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (kR) using a double value.
     *
     * @param kR The new reflection attenuation coefficient.
     * @return The current Material object for chaining method calls.
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the shininess factor (nShininess).
     *
     * @param nShininess The new shininess factor.
     * @return The current Material object for chaining method calls.
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
