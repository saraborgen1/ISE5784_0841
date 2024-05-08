package geometries;
import primitives.*;

public class Cylinder extends Tube {
    private final double height;

    public Cylinder(Ray axis, double radius, double height){
        super(axis, radius);
        this.height = height;
    }
}
