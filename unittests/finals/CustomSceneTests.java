package finals;

import geometries.Polygon;
import geometries.Geometry;
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

public class CustomSceneTests {
    private final Scene scene = new Scene("Brick Wall Test scene");

    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(800, 600)
            .setRayTracer(new SimpleRayTracer(scene));

    private final Material brickMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material mortarMaterial = new Material().setKD(0.5).setKS(0.5).setShininess(30);
    private final Material sphereMaterial = new Material().setKD(0.2).setKS(0.2).setShininess(10);
    private final Material lineMaterial = new Material().setKD(0.0).setKS(0.0).setShininess(0);

    public static final Color BRICK_COLOR = new Color(222, 184, 135);
    public static final Color MORTAR_COLOR = Color.BLACK;
    public static final Color WHITE_COLOR = new Color(255, 255, 255);
    public static final Color LINE_COLOR = Color.BLACK;
    public static final Color BLACK_COLOR = Color.BLACK;

    @Test
    public void testBrickWallImage() {
        int width = 800;
        int height = 600;
        int brickWidth = 160;
        int brickHeight = 50;
        int mortarThickness = 3;

        Geometry background = new Polygon(
                new Point(-width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, -height / 2.0, -100),
                new Point(width / 2.0, height / 2.0, -100),
                new Point(-width / 2.0, height / 2.0, -100)
        ).setEmission(MORTAR_COLOR).setMaterial(mortarMaterial);
        scene.geometries.add(background);

        for (int row = -height / 2; row < height / 2; row += brickHeight + mortarThickness) {
            boolean isOffsetRow = ((row + height / 2) / (brickHeight + mortarThickness)) % 2 == 1;
            int xOffset = isOffsetRow ? brickWidth / 2 : 0;

            if (isOffsetRow && xOffset > 0) {
                Geometry partialBrick = new Polygon(
                        new Point(-width / 2, row, 0),
                        new Point(-width / 2 + xOffset, row, 0),
                        new Point(-width / 2 + xOffset, row + brickHeight, 0),
                        new Point(-width / 2, row + brickHeight, 0)
                ).setEmission(BRICK_COLOR).setMaterial(brickMaterial);
                scene.geometries.add(partialBrick);
            }

            for (int col = -width / 2 + xOffset; col < width / 2; col += brickWidth + mortarThickness) {
                Geometry brick = new Polygon(
                        new Point(col, row, 0),
                        new Point(col + brickWidth, row, 0),
                        new Point(col + brickWidth, row + brickHeight, 0),
                        new Point(col, row + brickHeight, 0)
                ).setEmission(BRICK_COLOR).setMaterial(brickMaterial);
                scene.geometries.add(brick);
            }

            if (isOffsetRow && (width / 2 - xOffset) % (brickWidth + mortarThickness) != 0) {
                int partialWidth = (width / 2 - xOffset) % (brickWidth + mortarThickness);
                Geometry partialBrickRight = new Polygon(
                        new Point(width / 2 - partialWidth, row, 0),
                        new Point(width / 2, row, 0),
                        new Point(width / 2, row + brickHeight, 0),
                        new Point(width / 2 - partialWidth, row + brickHeight, 0)
                ).setEmission(BRICK_COLOR).setMaterial(brickMaterial);
                scene.geometries.add(partialBrickRight);
            }
        }

        int rectWidth = 150;
        int rectHeight = 250;
        int spacing = 50;

        // Middle rectangle with shapes
        addBlackRectangleWithInnerWhiteAndShapes(0, 0);

        // Triangle outline for the middle rectangle
        addTriangleOutline(0, rectHeight / 2.0 + 30, rectWidth);

        // Left rectangle with spheres
        addBlackRectangleWithInnerWhiteAndSpheres(-rectWidth - spacing, 0, false);

        // Triangle outline for the left rectangle
        addTriangleOutline(-rectWidth - spacing, rectHeight / 2.0 + 30, rectWidth);

        // Right rectangle with spheres
        addBlackRectangleWithInnerWhiteAndSpheres(rectWidth + spacing, 0, false);

        // Triangle outline for the right rectangle
        addTriangleOutline(rectWidth + spacing, rectHeight / 2.0 + 30, rectWidth);

        scene.setAmbientLight(new AmbientLight(MORTAR_COLOR, 0.15));
        scene.lights.addAll(
                List.of(
                        new DirectionalLight(new Color(100, 75, 50), new Vector(-1, -1, -1)),
                        new PointLight(new Color(125, 75, 0), new Point(-50, 50, 50)).setKL(0.0001).setKQ(0.0001),
                        new SpotLight(new Color(150, 100, 0), new Point(50, -50, 50), new Vector(-1, 1, -1)).setKL(0.0001).setKQ(0.0001)
                )
        );

        camera.setImageWriter(new ImageWriter("aMINIP1", width, height))
                .build()
                .renderImage()
                .writeToImage();
    }

    private void addBlackRectangleWithInnerWhiteAndShapes(double centerX, double centerY) {
        int rectWidth = 150;
        int rectHeight = 250;
        int innerRectWidth = rectWidth - 20;
        int innerRectHeight = rectHeight - 20;

        Geometry blackRectangle = new Polygon(
                new Point(centerX - rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY + rectHeight / 2.0, 10),
                new Point(centerX - rectWidth / 2.0, centerY + rectHeight / 2.0, 10)
        ).setEmission(Color.BLACK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blackRectangle);

        Geometry innerWhiteRectangle = new Polygon(
                new Point(centerX - innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1),
                new Point(centerX - innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1)
        ).setEmission(WHITE_COLOR).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(innerWhiteRectangle);

        addShapes(centerX, centerY, innerRectWidth, innerRectHeight);
    }

    private void addShapes(double centerX, double centerY, double width, double height) {
        double halfWidth = width / 2;
        double halfHeight = height / 2;

        // Adjusted dimensions for shapes to fit inside the inner white rectangle
        double shapeWidth = width * 0.8;
        double shapeHeight = height * 0.8;

        // צורת טרפז צהוב
        Geometry yellowTrapezoid = new Polygon(
                new Point(centerX - halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY + halfHeight, 10.2),
                new Point(centerX - halfWidth + shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX - halfWidth, centerY - halfHeight + shapeHeight / 2, 10.2)
        ).setEmission(new Color(255, 255, 150)).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(yellowTrapezoid);


        // צורת משולש ירוק
        Geometry greenTriangle = new Polygon(
                new Point(centerX - shapeWidth / 4, centerY - halfHeight, 10.2),
                new Point(centerX, centerY - halfHeight + shapeHeight / 4, 10.2),
                new Point(centerX + shapeWidth / 4, centerY - halfHeight, 10.2)
        ).setEmission(new Color(0, 200, 150)).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(greenTriangle);

        // צורת משולש ורוד (מוגדל)
        Geometry pinkTriangle = new Polygon(
                new Point(centerX + halfWidth - shapeWidth / 3, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY + halfHeight, 10.2),
                new Point(centerX + halfWidth, centerY - halfHeight, 10.2)
        ).setEmission(new Color(255, 150, 255)).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(pinkTriangle);

        // צורת טרפז כחול (מורחב פי 2 ומקוצר פי 2)
        Geometry blueTrapezoid = new Polygon(
                new Point(centerX - shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY + halfHeight, 10.2),
                new Point(centerX + shapeWidth / 8, centerY - halfHeight, 10.2),
                new Point(centerX - shapeWidth / 8, centerY - halfHeight, 10.2)
        ).setEmission(new Color(150, 150, 255)).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blueTrapezoid);
    }

    private void addBlackRectangleWithInnerWhiteAndSpheres(double centerX, double centerY, boolean addTriangles) {
        int rectWidth = 150;
        int rectHeight = 250;
        int innerRectWidth = rectWidth - 20;
        int innerRectHeight = rectHeight - 20;

        Geometry blackRectangle = new Polygon(
                new Point(centerX - rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY - rectHeight / 2.0, 10),
                new Point(centerX + rectWidth / 2.0, centerY + rectHeight / 2.0, 10),
                new Point(centerX - rectWidth / 2.0, centerY + rectHeight / 2.0, 10)
        ).setEmission(Color.BLACK).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(blackRectangle);

        Geometry innerWhiteRectangle = new Polygon(
                new Point(centerX - innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY - innerRectHeight / 2.0, 10.1),
                new Point(centerX + innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1),
                new Point(centerX - innerRectWidth / 2.0, centerY + innerRectHeight / 2.0, 10.1)
        ).setEmission(WHITE_COLOR).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(innerWhiteRectangle);

        if (addTriangles) {
            addTrianglePattern(centerX, centerY, innerRectWidth, innerRectHeight);
        } else {
            addSpheres(centerX, centerY, innerRectWidth, innerRectHeight);
        }
    }

    private void addSpheres(double centerX, double centerY, double width, double height) {
        Random rand = new Random();
        for (int i = 0; i < 250; i++) {
            double radius = 5 + rand.nextDouble() * 20;
            double offsetX = (rand.nextDouble() - 0.5) * (width - 2 * radius);
            double offsetY = (rand.nextDouble() - 0.5) * (height - 2 * radius);

            Color pastelColor = getRandomDarkerPastelColor();

            Geometry sphere = new Sphere(new Point(centerX + offsetX, centerY + offsetY, 10.2), radius)
                    .setEmission(pastelColor).setMaterial(sphereMaterial);
            scene.geometries.add(sphere);
        }
    }

    private void addTrianglePattern(double centerX, double centerY, double width, double height) {
        double triangleHeight = Math.sqrt(3) / 2 * (width / 10); // Adjust size based on desired scale
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        Color[] colors = { new Color(211, 211, 211), new Color(173, 216, 230), new Color(144, 238, 144),
                new Color(255, 182, 193), new Color(255, 255, 255) }; // Colors from the pattern

        for (double y = centerY - halfHeight; y < centerY + halfHeight; y += triangleHeight) {
            for (double x = centerX - halfWidth; x < centerX + halfWidth; x += width / 10) {
                addSingleTrianglePattern(x, y, width / 10, triangleHeight, colors);
            }
        }
    }

    private void addSingleTrianglePattern(double x, double y, double width, double height, Color[] colors) {
        Random rand = new Random();

        Geometry triangle1 = new Polygon(
                new Point(x, y, 10.2),
                new Point(x + width / 2, y + height, 10.2),
                new Point(x - width / 2, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)]).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle1);

        Geometry triangle2 = new Polygon(
                new Point(x, y + height * 2, 10.2),
                new Point(x + width / 2, y + height, 10.2),
                new Point(x - width / 2, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)]).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle2);

        Geometry triangle3 = new Polygon(
                new Point(x + width / 2, y, 10.2),
                new Point(x + width, y + height, 10.2),
                new Point(x, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)]).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle3);

        Geometry triangle4 = new Polygon(
                new Point(x - width / 2, y, 10.2),
                new Point(x - width, y + height, 10.2),
                new Point(x, y + height, 10.2)
        ).setEmission(colors[rand.nextInt(colors.length)]).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));
        scene.geometries.add(triangle4);
    }

    private void addTriangleOutline(double centerX, double centerY, int baseWidth) {
        double height = baseWidth * Math.sqrt(3) / 2;
        double thickness = 2;

        // Left trapezoid
        Geometry leftTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX - thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(LINE_COLOR).setMaterial(lineMaterial);
        scene.geometries.add(leftTrapezoid);

        // Right trapezoid
        Geometry rightTrapezoid = new Polygon(
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX, centerY + height / 2, 10),
                new Point(centerX + thickness, centerY + height / 2 - thickness, 10)
        ).setEmission(LINE_COLOR).setMaterial(lineMaterial);
        scene.geometries.add(rightTrapezoid);

        // Bottom trapezoid
        Geometry bottomTrapezoid = new Polygon(
                new Point(centerX - baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2, centerY - height / 2, 10),
                new Point(centerX + baseWidth / 2 - thickness, centerY - height / 2 + thickness, 10),
                new Point(centerX - baseWidth / 2 + thickness, centerY - height / 2 + thickness, 10)
        ).setEmission(LINE_COLOR).setMaterial(lineMaterial);
        scene.geometries.add(bottomTrapezoid);

        // Add small black sphere at the top of the triangle
        double sphereRadius = 5;
        addBlackSphereAtPoint(new Point(centerX, centerY + height / 2, 10.3), sphereRadius);
    }

    private void addBlackSphereAtPoint(Point center, double radius) {
        Geometry blackSphere = new Sphere(center, radius).setEmission(BLACK_COLOR).setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(10));
        scene.geometries.add(blackSphere);
    }

    private Color getRandomDarkerPastelColor() {
        Random rand = new Random();
        int red = 150 + rand.nextInt(106);
        int green = 150 + rand.nextInt(106);
        int blue = 150 + rand.nextInt(106);
        return new Color(red, green, blue);
    }
}
