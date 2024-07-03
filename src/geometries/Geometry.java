package geometries;

import primitives.*;

/** abstract class representing a geometric body in 3D space.*/
public abstract class Geometry extends Intersectable {

    /** The emission color of the geometry, initialized to black */
    protected Color emission = Color.BLACK;

    /** The material properties of the geometry, initialized to default material */
    private Material material = new Material();

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission The emission color to set.
     * @return the current geometry instance.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Calculates the normal vector to the geometry at a given point.
     *
     * @param point The point on the geometry.
     * @return The normal vector to the geometry at the given point.
     */
    public abstract Vector getNormal(Point point);

    /**
     * Returns the material properties of the geometry.
     *
     * @return the material properties.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties of the geometry.
     *
     * @param material The material properties to set.
     * @return the current geometry instance.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
