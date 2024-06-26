package renderer;

import org.junit.jupiter.api.Test;

import primitives.*;
import renderer.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.Color.*;


public class ImageWriterTest {

    @Test
    public void testImageCreationWithGrid() {
        // Image resolution
        int width = 800;
        int height = 500;
        // Grid dimensions
        int rows = 10;
        int columns = 16;

        ImageWriter imageWriter = new ImageWriter("grid_image", width, height);

        Color yellow = new Color(YELLOW);
        Color red = new Color(RED);

        // background fill
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                imageWriter.writePixel(j, i, yellow);
            }
        }

        // Drawing the grid lines
        int rowHeight = height / rows;
        int colWidth = width / columns;

        // Draw horizontal lines
        for (int row = 0; row <= rows; row++) {
            int y = row * rowHeight;
            if (y < height) {
                for (int x = 0; x < width; x++) {
                    imageWriter.writePixel(x, y, red);
                }
            }
        }

        // Draw vertical lines
        for (int col = 0; col <= columns; col++) {
            int x = col * colWidth;
            if (x < width) { // בדיקה שהאינדקס בגבולות
                for (int y = 0; y < height; y++) {
                    imageWriter.writePixel(x, y, red);
                }
            }
        }

        imageWriter.writeToImage();
    }
}
