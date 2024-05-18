package geometries;
import primitives.*;

/**
 * Class representing a sphere in 3D space.
 */
public class Sphere extends RadialGeometry{
    private final Point center;

    /**
     * Constructs a new Sphere with the given center point and radius.
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center,double radius) {
        super(radius);
        this.center = center;
    }

    public Vector getNormal(Point p) {
        return p.subtract(this.center).normalize();
    }
}
