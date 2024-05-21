package geometries;
import primitives.*;

/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructs a new Cylinder with the given axis ray, radius, and height.
     * @param axis The central axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     * @param height The height of the cylinder.
     */
    public Cylinder(Ray axis, double radius, double height){
        super(axis, radius);
        this.height = height;
    }

    public Vector getNormal(Point p) {
        Point point = axis.getHead();
        Vector vector = axis.getDirection().normalize();

        // אם הנקודה נמצאת על הבסיס התחתון
        if (p.equals(point) || Util.isZero(p.subtract(point).dotProduct(vector))) {
            return vector.scale(-1);
        }

        // אם הנקודה נמצאת על הבסיס העליון
        if ( p.equals(point.add(vector.scale(height)))||
                Util.isZero(p.subtract(point.add(vector.scale(height))).dotProduct(vector)))
            return vector;

        // אם הנקודה נמצאת על המעטפת הקמורה
        return super.getNormal(p);
    }
}
