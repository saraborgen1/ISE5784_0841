package finals;

import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.*;

import java.util.List;

public class FinalTests {
    // Creating a new scene with the name "A wall with a picture scene"
    private final Scene scene = new Scene("A wall with a picture scene");

    // Creating a Camera object with Anti-Aliasing
    private final Camera.Builder cameraWithAA = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // Setting camera direction
            .setLocation(new Point(0, 0, 1000)) // Setting camera location
            .setVpDistance(1000) // Setting view plane distance
            .setVpSize(800, 600) // Setting view plane size
            .setRayTracer(new SimpleRayTracer(scene)) // Setting ray tracer
            .setNumberOfRays(18); // Enabling Anti-Aliasing by setting number of rays

    // Creating a Camera object without Anti-Aliasing
    private final Camera.Builder cameraWithoutAA = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // Setting camera direction
            .setLocation(new Point(0, 0, 1000)) // Setting camera location
            .setVpDistance(1000) // Setting view plane distance
            .setVpSize(800, 600) // Setting view plane size
            .setRayTracer(new SimpleRayTracer(scene)) // Setting ray tracer
            .setNumberOfRays(1); // Disabling Anti-Aliasing by setting number of rays to 1


    // Materials for the brick wall
    private final Material brickMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material mortarMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material lineMaterial = new Material().setKD(0.0).setKS(0.0).setShininess(0);

    // Colors for the scene
    public static final Color WHITE = new Color(java.awt.Color.WHITE);
    public static final Color BROWN = new Color(222, 184, 135);
    public static final Color YELLOW = new Color(java.awt.Color.YELLOW);
    public static final Color GREEN = new Color(java.awt.Color.GREEN);
    public static final Color PINK = new Color(java.awt.Color.PINK);
    public static final Color BLUE = new Color(java.awt.Color.BLUE);

    @Test
    public void testBrickWallImage() {
        // Setting dimensions for the scene
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

        // Adding the picture frame and its contents
        addFrame(0, 0);
        triangleOutline(0, rectHeight / 2.0 + 30, rectWidth);

        // Setting ambient light and other light sources
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
                                new Point(50, -50, 50), new Vector(-1, 1, -1)
                        ).setKL(0.0001).setKQ(0.0001)
                )
        );

        // Rendering the image with the camera and writing it to an image file
        cameraWithAA.setImageWriter(new ImageWriter("minip1", width, height))
                .build()
                .renderImage()
                .writeToImage();

        // Rendering the image without Anti-Aliasing and writing it to an image file
        cameraWithoutAA.setImageWriter(new ImageWriter("minip1_without_aa", width, height))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Creates a brick wall with the given dimensions.
     *
     * @param width The total width of the wall.
     * @param height The total height of the wall.
     * @param brickWidth The width of a single brick.
     * @param brickHeight The height of a single brick.
     * @param mortarThickness The thickness of the mortar between bricks.
     */
    private void createBrickWall
    (int width, int height, int brickWidth, int brickHeight, int mortarThickness) {
        // Loop over each row
        for (int row = -height / 2; row < height / 2; row += brickHeight + mortarThickness) {
            // Determine if the row should be offset
            boolean isOffsetRow = ((row + height / 2) / (brickHeight + mortarThickness)) % 2 == 1;
            int xOffset = isOffsetRow ? brickWidth / 2 : 0;

            // Add a half brick at the beginning of the offset row
            if (isOffsetRow && xOffset > 0) {
                addBrick(-width / 2, row, xOffset, brickHeight);
            }

            // Loop over each column in the row
            for (int col = -width / 2 + xOffset; col < width / 2; col += brickWidth + mortarThickness) {
                addBrick(col, row, brickWidth, brickHeight);
            }

            // Calculate the partial brick width at the end of the offset row if necessary
            int partialWidth = (width / 2 - xOffset) % (brickWidth + mortarThickness);
            if (isOffsetRow && partialWidth != 0) {
                addBrick(width / 2 - partialWidth, row, partialWidth, brickHeight);
            }
        }
    }

    /**
     * Adds a single brick to the scene at the specified location.
     *
     * @param x The x-coordinate of the brick's bottom-left corner.
     * @param y The y-coordinate of the brick's bottom-left corner.
     * @param brickWidth The width of the brick.
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
     * Adds a frame with shapes to the scene.
     *
     * @param centerX The x-coordinate of the frame's center.
     * @param centerY The y-coordinate of the frame's center.
     */
    private void addFrame(double centerX, double centerY) {
        int rectWidth = 150;
        int rectHeight = 250;
        int innerRectWidth = rectWidth - 20;
        int innerRectHeight = rectHeight - 20;

        // Adding the outer black rectangle of the frame
        Geometry blackRectangle = new Polygon(
                new Point(centerX - rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY + rectHeight / 2.0, 10),
                new Point(centerX - rectWidth / 2.0, centerY + rectHeight / 2.0, 10)
        ).setEmission(Color.BLACK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blackRectangle);

        // Adding the inner white rectangle of the frame
        Geometry innerWhiteRectangle = new Polygon(
                new Point(centerX - innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1),
                new Point(centerX - innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1)
        ).setEmission(WHITE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(innerWhiteRectangle);

        // Adding shapes inside the frame
        addShapes(centerX, centerY, innerRectWidth, innerRectHeight);
    }

    /**
     * Adds shapes inside the frame.
     *
     * @param centerX The x-coordinate of the center of the frame.
     * @param centerY The y-coordinate of the center of the frame.
     * @param width The width of the frame.
     * @param height The height of the frame.
     */
    private void addShapes(double centerX, double centerY, double width, double height) {
        double halfWidth = width / 2;
        double halfHeight = height / 2;

        double shapeWidth = width * 0.8;
        double shapeHeight = height * 0.8;

        // Adding a yellow trapezoid
        Geometry yellowTrapezoid = new Polygon(
                new Point(centerX - halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX - halfWidth, centerY - halfHeight + shapeHeight / 2, 10.2)
        ).setEmission(YELLOW).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(yellowTrapezoid);

        // Adding a green triangle
        Geometry greenTriangle = new Polygon(
                new Point(centerX - shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX, centerY - halfHeight + shapeHeight / 4, 10.2),
                new Point(centerX + shapeWidth / 4, centerY - halfHeight, 10.2)
        ).setEmission(GREEN).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(greenTriangle);

        // Adding a pink triangle
        Geometry pinkTriangle = new Polygon(
                new Point(centerX + halfWidth - shapeWidth / 3, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY - halfHeight, 10.2)
        ).setEmission(PINK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(pinkTriangle);

        // Adding a blue trapezoid
        Geometry blueTrapezoid = new Polygon(
                new Point(centerX - shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY - halfHeight, 10.2),
                new Point(centerX - shapeWidth / 8, centerY - halfHeight, 10.2)
        ).setEmission(BLUE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blueTrapezoid);
    }

    /**
     * Adds a triangle outline to the scene.
     *
     * @param centerX The x-coordinate of the triangle's center.
     * @param centerY The y-coordinate of the triangle's center.
     * @param baseWidth The width of the triangle's base.
     */
    private void triangleOutline(double centerX, double centerY, int baseWidth) {
        double height = baseWidth * Math.sqrt(3) / 2;
        double thickness = 2;

        // Adding the left trapezoid of the triangle outline
        Geometry leftTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX - thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(leftTrapezoid);

        // Adding the right trapezoid of the triangle outline
        Geometry rightTrapezoid = new Polygon(
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX + thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(rightTrapezoid);

        // Adding the bottom trapezoid of the triangle outline
        Geometry bottomTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10)
        ).setEmission(Color.BLACK).setMaterial(lineMaterial);
        scene.geometries.add(bottomTrapezoid);

        // Adding a black sphere at the top of the triangle
        double sphereRadius = 5;
        blackSphere(new Point(centerX, centerY + height / 2, 10.3), sphereRadius);
    }

    /**
     * Adds a black sphere to the scene.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    private void blackSphere(Point center, double radius) {
        Geometry blackSphere = new Sphere(center, radius).setEmission(Color.BLACK)
                .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(10));
        scene.geometries.add(blackSphere);
    }
}
