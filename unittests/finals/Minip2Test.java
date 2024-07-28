package finals;

import geometries.Geometry;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import java.util.List;
import java.util.Random;

public class Minip2Test {
    // Create a new scene with the given name
    private final Scene scene = new Scene("Brick Wall Test scene");

    // Creating a Camera object with Anti-Aliasing
    private final Camera.Builder cameraWithAA = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            // Setting camera direction
            .setLocation(new Point(0, 0, 1000)) // Setting camera location
            .setVpDistance(1000) // Setting view plane distance
            .setVpSize(800, 600) // Setting view plane size
            .setRayTracer(new SimpleRayTracer(scene)) // Setting ray tracer
            .setNumberOfRays(18); // Enabling Anti-Aliasing by setting number of rays

    // Creating a Camera object without Anti-Aliasing
    private final Camera.Builder cameraWithoutAA = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            // Setting camera direction
            .setLocation(new Point(0, 0, 1000)) // Setting camera location
            .setVpDistance(1000) // Setting view plane distance
            .setVpSize(800, 600) // Setting view plane size
            .setRayTracer(new SimpleRayTracer(scene)) // Setting ray tracer
            .setNumberOfRays(1); // Disabling Anti-Aliasing by setting number of rays to 1

    //Creating a Camera object with multi-threading
    private final Camera.Builder cameraWithThreads = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            // Setting camera direction
            .setLocation(new Point(0, 0, 1000)) // Setting camera location
            .setVpDistance(1000) // Setting view plane distance
            .setVpSize(800, 600) // Setting view plane size
            .setRayTracer(new SimpleRayTracer(scene)) // Setting ray tracer
            .setNumberOfRays(18) // Enabling Anti-Aliasing by setting number of rays
            .setThreadsCount(3);// Setting number of threads for multi-threading

    // Define materials for different objects
    private final Material brickMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material mortarMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material lineMaterial = new Material().setKD(0.0).setKS(0.0).setShininess(0);
    private final Material sphereMaterial = new Material().setKD(0.2).setKS(0.2).setShininess(10);

    // Define colors for different objects
    public static final Color WHITE = new Color(java.awt.Color.WHITE);
    public static final Color BROWN = new Color(222, 184, 135);
    public static final Color YELLOW = new Color(java.awt.Color.YELLOW);
    public static final Color GREEN = new Color(java.awt.Color.GREEN);
    public static final Color PINK = new Color(java.awt.Color.PINK);
    public static final Color BLUE = new Color(java.awt.Color.BLUE);

    @Test
    public void testBrickWallImage() {
        // Define dimensions for the scene and bricks
        int width = 800;
        int height = 600;
        int brickWidth = 160;
        int brickHeight = 50;
        int mortarThickness = 3;

        // Adding a black background
        Geometry background = new Polygon(
                new Point(-width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, height / 2.0, -100),
                new Point(-width / 2.0, height / 2.0, -100)
        ).setEmission(Color.BLACK).setMaterial(mortarMaterial);
        scene.geometries.add(background);

        // Creating the brick wall
        createBrickWall(width, height, brickWidth, brickHeight, mortarThickness);

        // Setting dimensions for the picture frame
        int rectWidth = 150;
        int rectHeight = 250;
        int spacing = 50;

        // Add middle rectangle with shapes
        addFrame(0, 0);

        // Add triangle outline for the middle rectangle
        triangleOutline(0, rectHeight / 2.0 + 30, rectWidth);

        // Add left rectangle with spheres
        framedSpheres(-rectWidth - spacing, 0, false);

        // Add triangle outline for the left rectangle
        triangleOutline(-rectWidth - spacing, rectHeight / 2.0 + 30, rectWidth);

        // Add right rectangle with spheres
        framedSpheres(rectWidth + spacing, 0, false);

        // Add triangle outline for the right rectangle
        triangleOutline(rectWidth + spacing, rectHeight / 2.0 + 30, rectWidth);

        // Set ambient light and additional light sources for the scene
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0.15));
        scene.lights.addAll(
                List.of(
                        new DirectionalLight(
                                new Color(100, 75, 50), new Vector(-1, -1, -1)
                        ),
                        new PointLight(
                                new Color(125, 75, 0),
                                new Point(-50, 50, 50)
                        ).setKL(0.0001).setKQ(0.0001),
                        new SpotLight(
                                new Color(150, 100, 0),
                                new Point(50, -50, 50),
                                new Vector(-1, 1, -1)
                        ).setKL(0.0001).setKQ(0.0001),
                        new DirectionalLight(
                                new Color(200, 100, 50),
                                new Vector(1, -1, -1)
                        ),
                        new PointLight(
                                new Color(200, 100, 0),
                                new Point(50, 50, 50)
                        ).setKL(0.0001).setKQ(0.0001)
                )
        );

        // Render the image and write it to a file with Anti-Aliasing
        cameraWithAA.setImageWriter(new ImageWriter("minip2_with_aa", width, height))
                .build()
                .renderImage()
                .writeToImage();

        // Render the image and write it to a file without Anti-Aliasing
        cameraWithoutAA.setImageWriter(new ImageWriter("minip2_without_aa", width, height))
                .build()
                .renderImage()
                .writeToImage();

        // Render the image and write it to a file with multi-threading
        cameraWithThreads.setImageWriter(new ImageWriter("minip2_with_threads", width, height))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Creates a brick wall with the given dimensions.
     *
     * @param width           The total width of the wall.
     * @param height          The total height of the wall.
     * @param brickWidth      The width of a single brick.
     * @param brickHeight     The height of a single brick.
     * @param mortarThickness The thickness of the mortar between bricks.
     */
    private void createBrickWall
    (int width, int height, int brickWidth, int brickHeight, int mortarThickness) {
        for (int row = -height / 2; row < height / 2; row += brickHeight + mortarThickness) {
            boolean isOffsetRow = ((row + height / 2) / (brickHeight + mortarThickness)) % 2 == 1;
            int xOffset = isOffsetRow ? brickWidth / 2 : 0;

            // Add partial brick at the beginning of the offset row
            if (isOffsetRow && xOffset > 0) {
                addBrick(-width / 2, row, xOffset, brickHeight);
            }

            // Add full bricks
            int col;
            for (col = -width / 2 + xOffset; col < width / 2; col += brickWidth + mortarThickness) {
                addBrick(col, row, brickWidth, brickHeight);
            }

            // Add partial brick at the end of the offset row
            int remainingWidth = width / 2 - col;
            if (isOffsetRow && remainingWidth > 0) {
                addBrick(col, row, remainingWidth, brickHeight);
            }
        }
    }

    /**
     * Adds a single brick to the scene at the specified location.
     *
     * @param x           The x-coordinate of the brick's bottom-left corner.
     * @param y           The y-coordinate of the brick's bottom-left corner.
     * @param brickWidth  The width of the brick.
     * @param brickHeight The height of the brick.
     */
    private void addBrick(double x, double y, double brickWidth, double brickHeight) {
        Geometry brick = new Polygon(
                new Point(x, y, 0),
                new Point(x + brickWidth, y, 0),
                new Point(x + brickWidth, y + brickHeight, 0),
                new Point(x, y + brickHeight, 0)
        ).setEmission(BROWN).setMaterial(brickMaterial);
        scene.geometries.add(brick);
    }

    /**
     * Adds a black rectangle with an inner white rectangle and various shapes inside it.
     *
     * @param centerX The x-coordinate of the center of the rectangle.
     * @param centerY The y-coordinate of the center of the rectangle.
     */
    private void addFrame(double centerX, double centerY) {
        int rectWidth = 150;
        int rectHeight = 250;
        int innerRectWidth = rectWidth - 20;
        int innerRectHeight = rectHeight - 20;

        // Create the outer black rectangle
        Geometry blackRectangle = new Polygon(
                new Point(centerX - rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY + rectHeight / 2.0, 10),
                new Point(centerX - rectWidth / 2.0, centerY + rectHeight / 2.0, 10)
        ).setEmission(Color.BLACK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blackRectangle);

        // Create the inner white rectangle
        Geometry innerWhiteRectangle = new Polygon(
                new Point(centerX - innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1),
                new Point(centerX - innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1)
        ).setEmission(WHITE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(innerWhiteRectangle);

        // Add various shapes inside the inner white rectangle
        addShapes(centerX, centerY, innerRectWidth, innerRectHeight);
    }

    /**
     * Adds various shapes inside the specified area.
     *
     * @param centerX The x-coordinate of the center of the area.
     * @param centerY The y-coordinate of the center of the area.
     * @param width   The width of the area.
     * @param height  The height of the area.
     */
    private void addShapes(double centerX, double centerY, double width, double height) {
        double halfWidth = width / 2;
        double halfHeight = height / 2;

        // Adjusted dimensions for shapes to fit inside the inner white rectangle
        double shapeWidth = width * 0.8;
        double shapeHeight = height * 0.8;

        // Yellow trapezoid
        Geometry yellowTrapezoid = new Polygon(
                new Point(centerX - halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX - halfWidth, centerY - halfHeight + shapeHeight / 2, 10.2)
        ).setEmission(YELLOW).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(yellowTrapezoid);

        // Green triangle
        Geometry greenTriangle = new Polygon(
                new Point(centerX - shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX, centerY - halfHeight + shapeHeight / 4, 10.2),
                new Point(centerX + shapeWidth / 4, centerY - halfHeight, 10.2)
        ).setEmission(GREEN).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(greenTriangle);

        // Pink triangle (enlarged)
        Geometry pinkTriangle = new Polygon(
                new Point(centerX + halfWidth - shapeWidth / 3, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY - halfHeight, 10.2)
        ).setEmission(PINK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(pinkTriangle);

        // Blue trapezoid (extended 2x and shortened 2x)
        Geometry blueTrapezoid = new Polygon(
                new Point(centerX - shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY - halfHeight, 10.2),
                new Point(centerX - shapeWidth / 8, centerY - halfHeight, 10.2)
        ).setEmission(BLUE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blueTrapezoid);
    }

    /**
     * Adds a black rectangle with an inner white rectangle and spheres inside it.
     *
     * @param centerX      The x-coordinate of the center of the rectangle.
     * @param centerY      The y-coordinate of the center of the rectangle.
     * @param addTriangles Whether to add a triangle pattern inside the rectangle.
     */
    private void framedSpheres(double centerX, double centerY, boolean addTriangles) {
        int rectWidth = 150;
        int rectHeight = 250;
        int innerRectWidth = rectWidth - 20;
        int innerRectHeight = rectHeight - 20;

        // Create the outer black rectangle
        Geometry blackRectangle = new Polygon(
                new Point(centerX - rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY + rectHeight / 2.0, 10),
                new Point(centerX - rectWidth / 2.0, centerY + rectHeight / 2.0, 10)
        ).setEmission(Color.BLACK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blackRectangle);

        // Create the inner white rectangle
        Geometry innerWhiteRectangle = new Polygon(
                new Point(centerX - innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1),
                new Point(centerX - innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1)
        ).setEmission(WHITE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(innerWhiteRectangle);

        // Add either a triangle pattern or spheres inside the inner white rectangle
        if (addTriangles) {
            addTrianglePattern(centerX, centerY, innerRectWidth, innerRectHeight);
        } else {
            addSpheres(centerX, centerY, innerRectWidth, innerRectHeight);
        }
    }

    /**
     * Adds randomly colored spheres inside the specified area.
     *
     * @param centerX The x-coordinate of the center of the area.
     * @param centerY The y-coordinate of the center of the area.
     * @param width   The width of the area.
     * @param height  The height of the area.
     */
    private void addSpheres(double centerX, double centerY, double width, double height) {
        Random rand = new Random();
        for (int i = 0; i < 250; i++) {
            double radius = 5 + rand.nextDouble() * 20;
            double offsetX = (rand.nextDouble() - 0.5) * (width - 2 * radius);
            double offsetY = (rand.nextDouble() - 0.5) * (height - 2 * radius);

            Color pastelColor = getRandomDarkerPastelColor();

            // Create and add the sphere with a random color
            Geometry sphere = new Sphere(
                    new Point(centerX + offsetX, centerY + offsetY, 10.2), radius)
                    .setEmission(pastelColor).setMaterial(sphereMaterial);
            scene.geometries.add(sphere);
        }
    }

    /**
     * Adds a triangle pattern inside the specified area.
     *
     * @param centerX The x-coordinate of the center of the area.
     * @param centerY The y-coordinate of the center of the area.
     * @param width   The width of the area.
     * @param height  The height of the area.
     */
    private void addTrianglePattern(double centerX, double centerY, double width, double height) {
        double triangleHeight = Math.sqrt(3) / 2 * (width / 10); // Adjust size based on desired scale
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        Color[] colors = {WHITE, BLUE, GREEN, PINK, YELLOW}; // Colors from the pattern

        // Create and add the triangles to form the pattern
        for (double y = centerY - halfHeight; y < centerY + halfHeight; y += triangleHeight) {
            for (double x = centerX - halfWidth; x < centerX + halfWidth; x += width / 10) {
                addSingleTrianglePattern(x, y, width / 10, triangleHeight, colors);
            }
        }
    }

    /**
     * Adds a single triangle pattern using the specified coordinates and dimensions.
     *
     * @param x      The x-coordinate of the base of the triangle.
     * @param y      The y-coordinate of the base of the triangle.
     * @param width  The width of the triangle.
     * @param height The height of the triangle.
     * @param colors The array of colors to use for the triangles.
     */
    private void addSingleTrianglePattern
    (double x, double y, double width, double height, Color[] colors) {
        Random rand = new Random();

        // Create and add the first triangle
        Geometry triangle1 = new Polygon(
                new Point(x, y, 10.2),
                new Point(x + width / 2, y + height, 10.2),
                new Point(x - width / 2, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)])
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle1);

        // Create and add the second triangle
        Geometry triangle2 = new Polygon(
                new Point(x, y + height * 2, 10.2),
                new Point(x + width / 2, y + height, 10.2),
                new Point(x - width / 2, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)])
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle2);

        // Create and add the third triangle
        Geometry triangle3 = new Polygon(
                new Point(x + width / 2, y, 10.2),
                new Point(x + width, y + height, 10.2),
                new Point(x, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)])
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle3);

        // Create and add the fourth triangle
        Geometry triangle4 = new Polygon(
                new Point(x - width / 2, y, 10.2),
                new Point(x - width, y + height, 10.2),
                new Point(x, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)])
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle4);
    }

    /**
     * Adds a triangular outline around the specified center.
     *
     * @param centerX   The x-coordinate of the center of the triangle.
     * @param centerY   The y-coordinate of the center of the triangle.
     * @param baseWidth The width of the base of the triangle.
     */
    private void triangleOutline(double centerX, double centerY, int baseWidth) {
        double height = baseWidth * Math.sqrt(3) / 2;
        double thickness = 2;

        // Create and add the left trapezoid
        Geometry leftTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX - thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(leftTrapezoid);

        // Create and add the right trapezoid
        Geometry rightTrapezoid = new Polygon(
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX + thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(rightTrapezoid);

        // Create and add the bottom trapezoid
        Geometry bottomTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(bottomTrapezoid);

        // Add a small black sphere at the top of the triangle
        double sphereRadius = 5;
        addBlackSphereAtPoint(new Point(centerX, centerY + height / 2, 10.3), sphereRadius);
    }

    /**
     * Adds a black sphere at the specified point with the given radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    private void addBlackSphereAtPoint(Point center, double radius) {
        Geometry blackSphere = new Sphere(center, radius)
                .setEmission(Color.BLACK).setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(10));
        scene.geometries.add(blackSphere);
    }

    /**
     * Generates a random darker pastel color.
     *
     * @return A random darker pastel color.
     */
    private Color getRandomDarkerPastelColor() {
        Random rand = new Random();
        int red = 150 + rand.nextInt(106);
        int green = 150 + rand.nextInt(106);
        int blue = 150 + rand.nextInt(106);
        return new Color(red, green, blue);
    }
}
