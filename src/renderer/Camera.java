package renderer;
import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class representing a camera in a 3D space.
 * This class uses the Builder design pattern for object construction.
 */
public class Camera implements Cloneable{

    private Point p0;
    private Vector vTo, vUp, vRight;
    private double width=0,height=0,distance=0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /**
     * Private Default constructor for the Camera class.
     * Initializes the camera's width, height, and distance to zero.
     */
    private Camera(){
        this.width=0;
        this.height=0;
        this.distance=0;
    }

    /**
     * Get a new Builder instance for Camera.
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through the center of a pixel on the view plane.
     * @param nX Number of columns (width resolution)
     * @param nY Number of rows (height resolution)
     * @param j Pixel's column index (horizontal coordinate)
     * @param i Pixel's row index (vertical coordinate)
     * @return the constructed ray
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        // Calculate the center of the view plane
        Point pc=p0.add(vTo.scale(distance));

        // Calculate the size of each pixel
        double rY=height/nY;
        double rX=width/nX;

        // Calculate the offset from the center of the view plane to the pixel (i, j)
        double xJ=(j-(nX-1)/2.0)*rX;
        double yI=-(i-(nY-1)/2.0)*rY;

        // Calculate the intersection point on the view plane
        Point pIJ=pc;
        if(!isZero(xJ))
            pIJ=pIJ.add(vRight.scale(xJ));
        if(!isZero(yI))
            pIJ=pIJ.add(vUp.scale(yI));

        // Construct and return the ray from the camera origin through the pixel center
        return new Ray(p0, pIJ.subtract(p0));
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

    /**
     *  Renders the image by tracing rays through each pixel.
     *  @throws UnsupportedOperationException as a placeholder for the method
     * @return the camera object for method chaining
     */
    public Camera renderImage() {
        // Get the number of pixels along the x-axis and y-axis
        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();

        // Loop over each pixel row (y-axis)
        for(int i=0;i< ny;i++)
            // Loop over each pixel column (x-axis)
            for(int j=0;j<nx;j++)
                // Cast a ray through the current pixel at (j, i)
                castRay(nx,ny,j,i);

        return this;
    }

    /**
     * Casts a ray through the specified pixel and determines its color.
     * @param nX the number of pixels along the x-axis
     * @param nY the number of pixels along the y-axis
     * @param j the x-coordinate of the pixel
     * @param i the y-coordinate of the pixel
     */
    private void castRay(int nX, int nY, int j, int i) {
        Ray ray = constructRay(nX,nY,j,i);
        Color color= rayTracer.traceRay(ray);;
        imageWriter.writePixel(j,i,color);
    }

    /**
     * Prints a grid on the image with the specified color and interval.
     * @param interval the interval between grid lines
     * @param color the color of the grid lines
     * @return the camera object for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        // Check if the image writer is initialized
        if (imageWriter == null)
            throw new MissingResourceException("Image writer was null", getClass().getName(), "");

        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();

        for (int j = 0; j < nx; j++) {
            for (int i = 0; i < ny; i++) {
                // Check if the current pixel is at the specified interval
                if (j % interval == 0 || i % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file.
     * @return the camera object for method chaining
     */
    public Camera writeToImage() {
        imageWriter.writeToImage();
        return this;
    }

    /**
     * Builder class for Camera, following the Builder design pattern.
     */
    public static class Builder{
        private final Camera camera=new Camera();

        private static final String MISSING_RESOURCE_MESSAGE = "Missing rendering data";
        private static final String CAMERA_CLASS_NAME = Camera.class.getSimpleName();

        /**
         * Sets the location of the camera.
         * @param point the location point
         * @return the builder instance
         */
        public Builder setLocation(Point point){
            camera.p0=point;
            return this;
        }

        /**
         * Sets the direction of the camera.
         * @param vTo the "forward" vector
         * @param vUp the "up" vector
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp){

            if ((vTo.dotProduct(vUp)!=0)){
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }

            camera.vTo=vTo.normalize();
            camera.vUp=vUp.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         * @param width the width of the view plane
         * @param height the height of the view plane
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the width or height are less than or equal to 0
         */
        public Builder setVpSize(double width, double height){
            if (width<0 || height<0){
                throw new IllegalArgumentException("Width and height must be greater than 0");
            }
            camera.width=width;
            camera.height=height;
            return this;
        }


        /**
         * Sets the distance from the camera to the view plane.
         * @param distance the distance from the camera to the view plane
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the distance is less than or equal to 0
         */
        public Builder setVpDistance(double distance){
            if (distance<0){
                throw new IllegalArgumentException("Distance must be greater than 0");
            }
            camera.distance=distance;
            return this;
        }

        /**
         * Sets the image writer for the camera.
         * @param imageWriter the image writer
         * @return the Builder instance for chaining
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         * @param rayTracer the ray tracer
         * @return the Builder instance for chaining
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Builds the Camera object.
         * @return the constructed Camera object
         */
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
            if (camera.imageWriter == null) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "imageWriter"
                );
            }
            if (camera.rayTracer == null) {
                throw new MissingResourceException(
                        MISSING_RESOURCE_MESSAGE,
                        CAMERA_CLASS_NAME,
                        "rayTracer"
                );
            }

            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            return camera;
        }
    }
}

