package renderer;

import org.junit.jupiter.api.Test;

import primitives.*;
import renderer.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageWriterTest {

    @Test
    public void testImageCreationWithGrid() {
        // Image resolution
        int width = 800;
        int height = 500;
        // Grid dimensions
        int rows = 10;
        int columns = 16;

        // יצירת אובייקט ImageWriter
        ImageWriter imageWriter = new ImageWriter("grid_image", width, height);

        // הגדרת הצבעים
        Color yellow = new Color(255, 255, 0); // צבע צהוב
        Color red = new Color(255, 0, 0); // צבע אדום

        // מילוי הרקע בצבע צהוב
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imageWriter.writePixel(i, j, yellow);
            }
        }

        // ציור קווי הרשת בצבע אדום
        int rowHeight = height / rows;
        int colWidth = width / columns;

        // ציור קווים אופקיים
        for (int row = 0; row <= rows; row++) {
            int y = row * rowHeight;
            if (y < height) { // בדיקה שהאינדקס בגבולות
                for (int col = 0; col < width; col++) {
                    imageWriter.writePixel(col, y, red);
                }
            }
        }

        // ציור קווים אנכיים
        for (int col = 0; col <= columns; col++) {
            int x = col * colWidth;
            if (x < width) { // בדיקה שהאינדקס בגבולות
                for (int row = 0; row < height; row++) {
                    imageWriter.writePixel(x, row, red);
                }
            }
        }

        // כתיבת התמונה לקובץ
        imageWriter.writeToImage();
    }
}
