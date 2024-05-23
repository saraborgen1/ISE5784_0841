package geometries;

/**
 * Abstract class representing a radial geometry in 3D space.
 */
    public abstract class RadialGeometry implements Geometry{
    protected double radius;

    /**
     * Constructs a new RadialGeometry with the given radius.
     * @param radius The radius of the radial geometry.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
