package finals;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.*;

import java.util.List;

public class FinalTests {
    // Create a new scene with the given name
    private final Scene scene = new Scene("A wall with a picture scene");

    // Initialize the camera with its builder
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(800, 600)
            .setRayTracer(new SimpleRayTracer(scene));

    // Define materials for different objects
    private final Material brickMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material mortarMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material lineMaterial = new Material().setKD(0.0).setKS(0.0).setShininess(0);

    // Define colors for different objects
    public static final Color WHITE = new Color(java.awt.Color.WHITE);
    public static final Color BROWN= new Color(222, 184, 135);
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

        // Create the background polygon for the mortar
        Geometry background = new Polygon(
                new Point(-width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, height / 2.0, -100),
                new Point(-width / 2.0, height / 2.0, -100)
        ).setEmission(Color.BLACK).setMaterial(mortarMaterial);
        scene.geometries.add(background);

        // Create the brick wall with alternating rows of bricks
        for (int row = -height / 2; row < height / 2; row += brickHeight + mortarThickness) {
            boolean isOffsetRow = ((row + height / 2) / (brickHeight + mortarThickness)) % 2 == 1;
            int xOffset = isOffsetRow ? brickWidth / 2 : 0;

            // Add partial brick at the beginning of the offset row
            if (isOffsetRow && xOffset > 0) {
                Geometry partialBrick = new Polygon(
                        new Point(-width / 2, row, 0),
                        new Point(-width / 2 + xOffset, row, 0),
                        new Point(-width / 2 + xOffset, row + brickHeight, 0),
                        new Point(-width / 2, row + brickHeight, 0)
                ).setEmission(BROWN).setMaterial(brickMaterial);
                scene.geometries.add(partialBrick);
            }

            // Add full bricks
            for (int col = -width / 2 + xOffset; col < width / 2; col += brickWidth + mortarThickness) {
                Geometry brick = new Polygon(
                        new Point(col, row, 0),
                        new Point(col + brickWidth, row, 0),
                        new Point(col + brickWidth, row + brickHeight, 0),
                        new Point(col, row + brickHeight, 0)
                ).setEmission(BROWN).setMaterial(brickMaterial);
                scene.geometries.add(brick);
            }

            // Add partial brick at the end of the offset row
            if (isOffsetRow && (width / 2 - xOffset) % (brickWidth + mortarThickness) != 0) {
                int partialWidth = (width / 2 - xOffset) % (brickWidth + mortarThickness);
                Geometry partialBrickRight = new Polygon(
                        new Point(width / 2 - partialWidth, row, 0),
                        new Point(width / 2, row, 0),
                        new Point(width / 2, row + brickHeight, 0),
                        new Point(width / 2 - partialWidth, row + brickHeight, 0)
                ).setEmission(BROWN).setMaterial(brickMaterial);
                scene.geometries.add(partialBrickRight);
            }
        }

        int rectWidth = 150;
        int rectHeight = 250;

        // Add middle rectangle with shapes
        addFrame(0, 0);

        // Add triangle outline for the middle rectangle
        triangleOutline(0, rectHeight / 2.0 + 30, rectWidth);

        
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
                                new Point(50, -50, 50), new Vector(-1, 1, -1)
                        ).setKL(0.0001).setKQ(0.0001)
                )
        );

        // Render the image and write it to a file
        camera.setImageWriter(new ImageWriter("miniproject1", width, height))
                .build()
                .renderImage()
                .writeToImage();
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
     * @param width The width of the area.
     * @param height The height of the area.
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

        // Pink triangle
        Geometry pinkTriangle = new Polygon(
                new Point(centerX + halfWidth - shapeWidth / 3, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY - halfHeight, 10.2)
        ).setEmission(PINK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(pinkTriangle);

        // Blue rectangle
        Geometry blueTrapezoid = new Polygon(
                new Point(centerX - shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY - halfHeight, 10.2),
                new Point(centerX - shapeWidth / 8, centerY - halfHeight, 10.2)
        ).setEmission(BLUE).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blueTrapezoid);
    }

    


    /**
     * Simulates a triangle attached to the picture which is hung on a nail.
     *
     * @param centerX The x-coordinate of the center of the triangle.
     * @param centerY The y-coordinate of the center of the triangle.
     * @param baseWidth The width of the base of the triangle.
     */
    private void triangleOutline(double centerX, double centerY, int baseWidth) {

        // Calculate the height of the triangle based on its base width
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
        blackSphere(new Point(centerX, centerY + height / 2, 10.3), sphereRadius);
    }

    /**
     * Adds a black sphere at the specified point with the given radius.
     * The sphere simulates a nail that holds the image
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
