package geometries;
import primitives.*;

import java.util.List;

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

    /**
     * Returns the normal vector to the tube at a given point.
     * @param p The point on the surface of the tube.
     * @return The normal vector at the given point.
     */
    public Vector getNormal(Point p) {
        double t=axis.getDirection().dotProduct(p.subtract(axis.getHead()));
        Point o=axis.getHead().add(axis.getDirection().scale(t));
        return p.subtract(o).normalize();
    }

    public List<Point> findIntersections(Ray ray){
        return null;
    }
}
