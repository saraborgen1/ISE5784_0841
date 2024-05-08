package geometries;
import primitives.*;

public class Sphere extends RadialGeometry{
    private final Point center;

    public Sphere(Point center,double radius) {
        super(radius);
        this.center = center;
    }

    public Vector getNormal(Point p) {
        return null;
    }
}
