package geometries;
import primitives.*;

public class Plane {
    private final Point q;
    private final Vector normal;

    public Plane(Point p1, Point p2, Point p3) {
        this.normal=null;
        this.q=p1;
    }

    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    public Vector getNormal(){
        return normal;
    }

    public Vector getNormal(Point p){
        return normal;
    }
}
