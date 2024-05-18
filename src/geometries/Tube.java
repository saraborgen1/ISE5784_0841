package geometries;
import primitives.*;

/**
 * Class representing a tube in 3D space.
 */
public class Tube extends RadialGeometry{
    protected final Ray axis;

    /**
     * Constructs a new Tube with the given axis ray and radius.
     * @param axis The central axis ray of the tube.
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis,double radius) {
        super(radius);
        this.axis = axis;
    }

    public Vector getNormal(Point p) {
        double t=axis.getDirection().dotProduct(p.subtract(axis.getHead()));
        Point o=p.add(axis.getDirection().scale(t));
        return p.subtract(o).normalize();
    }
}
