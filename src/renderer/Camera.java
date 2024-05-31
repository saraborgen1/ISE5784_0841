package renderer;
import primitives.*;

import java.util.MissingResourceException;

public class Camera implements Cloneable{

    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width=0,height=0,distance=0;

    private Camera(){

    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i){
        return null;
    }


    //getters
    public Point getP0() {
        return p0;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    //לבדוק עם מישהי
    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Should never happen, since we implement Cloneable");
        }
    }



    public static class Builder{
        private final Camera camera;

        private static final String MISSING_RESOURCE_MESSAGE = "Missing rendering data";
        private static final String CAMERA_CLASS_NAME = Camera.class.getSimpleName();


        public Builder(){
            this.camera=new Camera();
        }

        public Builder setLocation(Point point){
            camera.p0=point;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp){

            //לשאול אם צריך להוסיף בדיקה של 0
            if ((vTo.dotProduct(vUp)!=0)){
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }

            camera.vTo=vTo.normalize();
            camera.vUp=vUp.normalize();
            return this;
        }

        public Builder setVpSize(double width, double height){
            if (width<0 || height<0){
                throw new IllegalArgumentException("Width and height must be greater than 0");
            }
            camera.width=width;
            camera.height=height;
            return this;
        }

        public Builder setVpDistance(double distance){
            if (distance<0){
                throw new IllegalArgumentException("Distance must be greater than 0");
            }
            camera.distance=distance;
            return this;
        }

        public Camera build(){
            if (camera.p0 == null) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "p0"
                );
            }
            if (camera.vTo == null) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "vTo"
                );
            }
            if (camera.vUp == null) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "vUp"
                );
            }
            if (camera.width == 0) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "width"
                );
            }
            if (camera.height == 0) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "height"
                );
            }
            if (camera.distance == 0) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "distance"
                );
            }

            // נוודא שכל הוקטורים מנורמלים
            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            return camera.clone();
        }
    }

}

