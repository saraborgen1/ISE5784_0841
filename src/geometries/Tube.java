package geometries;
import primitives.*;

public class Tube extends RadialGeometry{
    protected final Ray axis;

    public Tube(Ray axis,double radius) {
        super(radius);
        this.axis = axis;
    }

    public Vector getNormal(Point p) {
        return null;
    }
}
