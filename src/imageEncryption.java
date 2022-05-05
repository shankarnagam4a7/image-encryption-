import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class imageEncryption {
    private static int[][] referenceImage(String path) throws IOException {
        return getInts(path);
    }

    private static int[][] getInts(String path) throws IOException {
        File iFile = new File(path);
        System.out.println("File: " + iFile.getName());
        BufferedImage iImage = ImageIO.read(iFile);
        int[][] iArray = new int[iImage.getWidth()][iImage.getHeight()];
        System.out.println("Width: " + iImage.getWidth() + " Height: " + iImage.getHeight());
        for (int i = 0; i < iImage.getWidth(); i++) {
            for (int j = 0; j < iImage.getHeight(); j++) {
                iArray[i][j] = iImage.getRGB(i, j);
            }
        }
        return iArray;
    }

    private static int[][] shiftColumnMatrix(int[][] matrix, int shift) {
        int[][] shiftedColumnMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedColumnMatrix[i][j] = matrix[i][(j + shift) % matrix[0].length];
            }
        }
        return shiftedColumnMatrix;
    }

    private static int[][] shiftRowMatrix(int[][] matrix, int shift) {
        int[][] shiftedRowMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedRowMatrix[i][j] = matrix[(i + shift) % matrix.length][j];
            }
        }
        return shiftedRowMatrix;
    }

    private static int[][] differenceMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] differenceMatrix = new int[matrix2.length][matrix2[0].length];
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                differenceMatrix[i][j] = matrix2[i][j] - matrix1[i][j];
            }
        }
        return differenceMatrix;
    }

    private static void writeToFile(int[][] matrix, String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                writer.write(matrix[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    private static int[][] getRedPixels(int[][] matrix, int[][] matrix2) {
        int[][] redPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                redPixels[i][j] = (matrix[i][j] >> 16) & 0xFF;
            }
        }
        return redPixels;
    }


    private static int[][] getBluePixels(int[][] matrix) {
        int[][] bluePixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                bluePixels[i][j] = matrix[i][j] & 0xFF;
            }
        }
        return bluePixels;
    }


    private static int[][] getGreenPixels(int[][] matrix, int[][] matrix2) {
        int[][] greenPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                greenPixels[i][j] = (matrix[i][j] >> 8) & 0xFF;
            }

        }
        return greenPixels;
    }



    private static int[][] getAlphaPixels(int[][] matrix, int[][] matrix2) {
        int[][] alphaPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                alphaPixels[i][j] = (matrix[i][j] >> 24) & 0xFF;
            }

        }
        return alphaPixels;
    }

    public static void main(String[] args) throws IOException {
        int[][] inputImageArray = referenceImage("input1.jpg");
        int[][] referenceImageArray = referenceImage("input3.jpg");
        if (inputImageArray.length == referenceImageArray.length || inputImageArray[0].length == referenceImageArray[0].length) {
//            System.out.println("Error: Images are not the same size");
//            System.out.println("Input Image Width: " + inputImageArray.length + " Input Image Height: " + inputImageArray[0].length);
//            System.out.println("Reference Image Width: " + referenceImageArray.length + " Reference Image Height: " + referenceImageArray[0].length);
        }
        System.out.println("Hello World!");
        int[][] encryptedImageArray = differenceMatrix( referenceImageArray, inputImageArray);
        BufferedImage encryptedImage = new BufferedImage(inputImageArray.length, inputImageArray[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < inputImageArray.length; i++) {
            for (int j = 0; j < inputImageArray[0].length; j++) {
                encryptedImage.setRGB(i, j, encryptedImageArray[i][j]);

            }
        }
        writeToFile(encryptedImageArray, "encrypted.txt");
        File oFile = new File("encrypted.png");
        ImageIO.write(encryptedImage, "png", oFile);
    }
}



