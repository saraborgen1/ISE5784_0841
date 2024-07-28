package renderer;

import primitives.*;

import java.util.List;
import java.util.LinkedList;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class representing a camera in a 3D space.
 * This class uses the Builder design pattern for object construction.
 */
public class Camera implements Cloneable {

    private Point p0; // Camera location
    private Vector vTo, vUp, vRight; // Direction vectors
    private double width = 0, height = 0, distance = 0; // View plane dimensions and distance
    private ImageWriter imageWriter; // Image writer for the rendered image
    private RayTracerBase rayTracer; // Ray tracer for the scene
    private int numberOfRays = 1; // Default value for no anti-aliasing
    private int threadsCount = 0;

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * <ul>
     */
    private PixelManager pixelManager;

    /**
     * Private Default constructor for the Camera class.
     * Initializes the camera's width, height, and distance to zero.
     */
    private Camera() {
        this.width = 0;
        this.height = 0;
        this.distance = 0;
    }

    /**
     * Get a new Builder instance for Camera.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Calculates the point on the view plane corresponding to a given pixel.
     *
     * @param nX Number of columns (width resolution)
     * @param nY Number of rows (height resolution)
     * @param j  Pixel's column index (horizontal coordinate)
     * @param i  Pixel's row index (vertical coordinate)
     * @return the point on the view plane corresponding to the pixel
     */
    private Point calculateViewPlanePoint(int nX, int nY, int j, int i) {
        // Calculate the center point of the view plane
        Point pc = p0.add(vTo.scale(distance));

        // Calculate the height and width of each pixel
        double rY = height / nY;
        double rX = width / nX;

        // Calculate the offset of the pixel from the center of the view plane
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        // Calculate the point on the view plane corresponding to the pixel
        Point pIJ = pc;
        if (!isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));

        return pIJ;
    }

    /**
     * Constructs a ray through the center of a pixel on the view plane.
     *
     * @param nX Number of columns (width resolution)
     * @param nY Number of rows (height resolution)
     * @param j  Pixel's column index (horizontal coordinate)
     * @param i  Pixel's row index (vertical coordinate)
     * @return the constructed ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = calculateViewPlanePoint(nX, nY, j, i);
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Constructs a list of rays for anti-aliasing through a pixel on the view plane.
     * If the number of rays is less than or equal to 1, it returns a list containing only the central ray.
     *
     * @param nX Number of columns (width resolution)
     * @param nY Number of rows (height resolution)
     * @param j  Pixel's column index (horizontal coordinate)
     * @param i  Pixel's row index (vertical coordinate)
     * @return list of rays for anti-aliasing
     */
    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        // If numberOfRays is less than or equal to 1, return just the central ray
        if (numberOfRays <= 1) {
            return List.of(constructRay(nX, nY, j, i));
        }

        // Calculate the view plane point corresponding to the pixel
        Point pIJ = calculateViewPlanePoint(nX, nY, j, i);

        List<Ray> rays = new LinkedList<>();
        // Calculate the height and width of each pixel
        double rY = height / nY;
        double rX = width / nX;
        // Calculate the height and width of each sub-pixel
        double subPixelWidth = rX / numberOfRays;
        double subPixelHeight = rY / numberOfRays;

        // Loop over each sub-pixel to create sample rays
        for (int subY = 0; subY < numberOfRays; subY++) {
            for (int subX = 0; subX < numberOfRays; subX++) {
                // Calculate the offset of the sub-pixel from the center of the pixel
                double offsetX = (subX - (numberOfRays - 1) / 2.0) * subPixelWidth;
                double offsetY = (subY - (numberOfRays - 1) / 2.0) * subPixelHeight;
                // Calculate the center point of the sub-pixel
                Point subPixelCenter = pIJ.add(vRight.scale(offsetX)).add(vUp.scale(offsetY));
                // Create a new ray from the camera to the sub-pixel center
                rays.add(new Ray(p0, subPixelCenter.subtract(p0)));
            }
        }

        return rays;
    }

    /**
     * Renders the image by tracing rays through each pixel.
     * This method supports both single-threaded and multi-threaded rendering.
     * If threadsCount is greater than 0, multiple threads are created to process pixels in parallel,
     * improving performance by utilizing CPU resources more efficiently.
     * The PixelManager ensures each thread processes unique pixels.
     *
     * @return the camera object for method chaining
     */
    public Camera renderImage() {
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        pixelManager = new PixelManager(nY, nX, 100l);
        if (this.threadsCount == 0) {
            for (int i = 0; i < this.imageWriter.getNy(); i++) {
                for (int j = 0; j < this.imageWriter.getNx(); j++) {
                    this.castRay(this.imageWriter.getNx(), this.imageWriter.getNy(), j, i);
                }
            }
        }
        else { // see further... option 2
            var threads = new LinkedList<Thread>(); // list of threads
            while (this.threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    PixelManager.Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // cast ray through pixel (and color it – inside castRay)
                        castRays(nX, nY, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads) thread.start();
            // wait until all the threads have finished
            try { for (var thread : threads) thread.join(); }
            catch (InterruptedException ignore) {}
        }
        return this;
    }

    /**
     * Casts a single ray through a specific pixel to compute the color.
     *
     * @param nX The number of pixels in the x-axis of the view plane grid.
     * @param nY The number of pixels in the y-axis of the view plane grid.
     * @param j  The index of the pixel in the x-axis of the grid.
     * @param i  The index of the pixel in the y-axis of the grid.
     */
    private void castRay(int nX, int nY, int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * Casts multiple rays through a specific pixel to compute the color by tracing each
     * ray and performing anti-aliasing.
     *
     * @param nX The number of pixels in the x-axis of the view plane grid.
     * @param nY The number of pixels in the y-axis of the view plane grid.
     * @param j  The index of the pixel in the x-axis of the grid.
     * @param i  The index of the pixel in the y-axis of the grid.
     */
    private Color castRays(int nX, int nY, int j, int i) {
        List<Ray> rays = constructRays(nX, nY, j, i);
        Color color = Color.BLACK;
        for (Ray ray : rays) {
            color = color.add(rayTracer.traceRay(ray));
        }
        color = color.reduce(rays.size());
        return color;
    }

    /**
     * Prints a grid on the image with the specified color and interval.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return the camera object for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("Image writer was null", getClass().getName(), "");

        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();

        // Loop over each pixel and print grid lines at the specified interval
        for (int j = 0; j < nx; j++) {
            for (int i = 0; i < ny; i++) {
                if (j % interval == 0 || i % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }

        return this;
    }

    /**
     * Writes the rendered image to a file.
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    /**
     * Builder class for Camera, following the Builder design pattern.
     */
    public static class Builder {
        private static final String MISSING_RESOURCE_MESSAGE = "Missing rendering data";
        private static final String CAMERA_CLASS_NAME = Camera.class.getSimpleName();
        private final Camera camera = new Camera();

        /**
         * Sets the location of the camera.
         *
         * @param point the location point
         * @return the builder instance
         */
        public Builder setLocation(Point point) {
            camera.p0 = point;
            return this;
        }

        /**
         * Sets the direction of the camera.
         *
         * @param vTo the "forward" vector
         * @param vUp the "up" vector
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if ((vTo.dotProduct(vUp) != 0)) {
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the width or height are less than or equal to 0
         */
        public Builder setVpSize(double width, double height) {
            if (width < 0 || height < 0) {
                throw new IllegalArgumentException("Width and height must be greater than 0");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance from the camera to the view plane
         * @return the Builder instance for chaining
         * @throws IllegalArgumentException if the distance is less than or equal to 0
         */
        public Builder setVpDistance(double distance) {
            if (distance < 0) {
                throw new IllegalArgumentException("Distance must be greater than 0");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the image writer for the camera.
         *
         * @param imageWriter the image writer
         * @return the Builder instance for chaining
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param rayTracer the ray tracer
         * @return the Builder instance for chaining
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the number of rays for anti-aliasing.
         *
         * @param numberOfRays the number of rays
         * @return the Builder instance for chaining
         */
        public Builder setNumberOfRays(int numberOfRays) {
            if (numberOfRays < 1) {
                throw new IllegalArgumentException("Number of rays must be greater than 0");
            }
            camera.numberOfRays = numberOfRays;
            return this;
        }

        public Builder setThreadsCount(int threadsCount) {
            if (threadsCount > 4 || threadsCount < 0)
                throw new IllegalArgumentException("The number of threads must be between 1 and 4");
            camera.threadsCount = threadsCount;
            return this;
        }

        /**
         * Builds the Camera object.
         *
         * @return the constructed Camera object
         */
        public Camera build() {
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
    // Getters
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
}