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

    /**
     *Returns the normal vector to the sphere at a given point.
     * @param p The point on the surface of the sphere
     * @return normal vector at the given point.
     */
    public Vector getNormal(Point p) {
        return p.subtract(this.center).normalize();
    }
}
