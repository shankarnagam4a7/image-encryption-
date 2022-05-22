import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class imageEncryption {

    private static int[][] getImagePixelMatrix(String path) throws IOException {
        return getImgMatrix(path);
    }
    // Returns the pixel matrix of the image at the given path
    private static int[][] getImgMatrix(String path) throws IOException {
        try {
            File iFile = new File(path);
            System.out.println("File: " + iFile.getName());
            BufferedImage iImage = ImageIO.read(iFile);
            int[][] iArray = new int[iImage.getWidth()][iImage.getHeight()];
            System.out.println("Width: " + iImage.getWidth() + " Height: " + iImage.getHeight());
            for (int i = 0; i < iImage.getWidth(); i++) {
                for (int j = 0; j < iImage.getHeight(); j++) {
                    iArray[i][j] = (-1) * iImage.getRGB(i, j);
                }
            }
            return iArray;
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    // Writes the image at the given path with the given pixel matrix
    private static void writeImage(int[][] iArray, String path) throws IOException {
        try {
            // Create a buffered image with the given pixel matrix
            BufferedImage iImage = new BufferedImage(iArray.length, iArray[0].length, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < iArray.length; i++) {
                for (int j = 0; j < iArray[0].length; j++) {
                    // Set the pixel at the given coordinates to the given value
                    iImage.setRGB(i, j, iArray[i][j]);
                }
            }
            // Write the buffered image to the given path
            File oFile = new File(path);
            System.out.println("File: " + oFile.getName());
            ImageIO.write(iImage, "png", oFile);
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }




    // Shifts the columns of the given matrix by the given amount of columns
    private static int[][] shiftColumnMatrix(int[][] matrix, int shift) {
        int[][] shiftedColumnMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedColumnMatrix[i][j] = matrix[i][(j + shift) % matrix[0].length];
            }
        }
        return shiftedColumnMatrix;
    }

    // Shifts the rows of the given matrix by the given amount of rows
    private static int[][] shiftRowMatrix(int[][] matrix, int shift) {
        int[][] shiftedRowMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedRowMatrix[i][j] = matrix[(i + shift) % matrix.length][j];
            }
        }
        return shiftedRowMatrix;
    }

    // Shifts the columns and rows of the given matrix by the given amount of rows and columns
    private static int[][] shiftColumnRowMatrix(int[][] matrix, int shift) {
        int[][] shiftedMatrix = shiftColumnMatrix(matrix, shift);
        shiftedMatrix = shiftRowMatrix(shiftedMatrix, shift);
        return shiftedMatrix;
    }

    // Writes the given matrix to the given path
    private static void writeToFile(int[][] matrix, String path) throws IOException {
        try {
            FileWriter writer = new FileWriter(path);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    writer.write(matrix[i][j] + " ");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("File written successfully!");
        }
        catch (IOException e) {
            System.out.println("An error occurred." + e);
        }
    }


    // Returns the given file as a matrix of integers
    private static int[][] fileToMatrix(String path) throws IOException {
        try {
            // Read the file
            File iFile = new File(path);
            System.out.println("File: " + iFile.getName());
            // Create a buffered reader to read the file
            BufferedReader reader = new BufferedReader(new FileReader(iFile));
            // Create a string to store the file contents
            String line = reader.readLine();
            // Create a int array to store the file contents
            int[][] iArray = new int[line.split(" ").length][line.split(" ").length];
            int i = 0;
            while (line != null) {
                String[] lineArray = line.split(" ");
                for (int j = 0; j < lineArray.length; j++) {
                    iArray[i][j] = Integer.parseInt(lineArray[j]);

                }
                line = reader.readLine();
                i++;
            }
            reader.close();
            return iArray;
        }
        catch (IOException e) {
            System.out.println("File not found!");
            return null;
        }
    }

    //
    private static int[][] fileToMatrixAtGivenPoint(String path) throws IOException {
        try {
            File iFile = new File(path);
            System.out.println("File: " + iFile.getName());
            BufferedReader reader = new BufferedReader(new FileReader(iFile));
            String line = reader.readLine();
            int[][] iArray = new int[line.split("|").length][line.split("|").length];
            int i = 0;
            while (line != null) {
                String[] lineArray = line.split("|");
                for (int j = 0; j < lineArray.length; j++) {
                    iArray[i][j] = Integer.parseInt(lineArray[j]);

                }

                line = reader.readLine();
                i++;
            }
            reader.close();
            return iArray;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }


    // xorMatrix takes two matrices and returns the xor of the two matrices
    private static int[][] xorMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] xorMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                xorMatrix[i][j] = (matrix1[i][j] ^ matrix2[i][j]);
            }
        }
        return xorMatrix;
    }


    private static int[][] averageMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] averageMatrix = new int[matrix2.length][matrix2[0].length];
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                averageMatrix[i][j] = (matrix1[i][j] + matrix2[i][j]) / 2;
            }
        }
        return averageMatrix;
    }

    // getRedPixel returns the red pixel value of the given pixel
    private static int[][] getRedPixels(int[][] matrix) {
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


    private static int[][] getGreenPixels(int[][] matrix) {
        int[][] greenPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                greenPixels[i][j] = (matrix[i][j] >> 8) & 0xFF;
            }

        }
        return greenPixels;
    }



    private static int[][] getAlphaPixels(int[][] matrix) {
        int[][] alphaPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                alphaPixels[i][j] = (matrix[i][j] >> 24) & 0xFF;
            }

        }
        return alphaPixels;
    }




    private static int[][] XORing(int[][] matrix1, int[][] matrix2) {
        int[][] XORedMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                XORedMatrix[i][j] = matrix1[i][j] ^ matrix2[i][j];
            }
        }
        return XORedMatrix;
    }

    // addingWithNumbers adds the matrix with the given number and returns the result
    private static int[][] addingWithNumber(int[][] matrix, int number) {
        int[][] addedMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                addedMatrix[i][j] = matrix[i][j] + number;
            }
        }
        return addedMatrix;
    }


    private static String[][] intToString(int[][] matrix1, int[][] matrix2, int[][] matrix3, int[][] matrix4) {
        String[][] stringMatrix = new String[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                stringMatrix[i][j] = String.valueOf(matrix1[i][j]) + String.valueOf(matrix2[i][j]) + String.valueOf(matrix3[i][j]) + String.valueOf(matrix4[i][j]);
            }
        }
        return stringMatrix;
    }




    private static int[][] stringToInt(String[][] matrix1, String[][] matrix2, String[][] matrix3, String[][] matrix4) {
        int[][] intMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                intMatrix[i][j] = Integer.parseInt(matrix1[i][j]) + Integer.parseInt(matrix2[i][j]) + Integer.parseInt(matrix3[i][j]) + Integer.parseInt(matrix4[i][j]);
            }
        }
        return intMatrix;

    }



    private static int[][] stringToInt(String[][] matrix) {
        int[][] intMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                intMatrix[i][j] = Integer.parseInt(matrix[i][j]);
            }
        }
        return intMatrix;
    }



    private static int[][] subtractingWithNumber(int[][] matrix, int number) {
        int[][] subtractedMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                subtractedMatrix[i][j] = matrix[i][j] - number;
            }
        }
        return subtractedMatrix;
    }



    private static int[][] equalizeMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] equalMatrix = new int[matrix1.length][matrix1[0].length];
        if (matrix1.length > matrix2.length || matrix1[0].length > matrix2[0].length) {
            for (int i = 0; i < matrix2.length; i++) {
                for (int j = 0; j < matrix2[0].length; j++) {
                    equalMatrix[i][j] = matrix2[i][j];

                }
            }
        }
        else if (matrix1.length < matrix2.length || matrix1[0].length < matrix2[0].length) {
            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix1[0].length; j++) {
                    equalMatrix[i][j] = matrix1[i][j];
                }
            }
        }
        else {
            equalMatrix = matrix1;
        }
        return equalMatrix;
    }


    private static int[][] matrixMultiplication(int[][] iMatrix, int[][] rMatrix) {
        int[][] multipliedMatrix = new int[iMatrix.length][iMatrix[0].length];
        for (int i = 0; i < iMatrix.length; i++) {
            for (int j = 0; j < iMatrix[0].length; j++) {
                multipliedMatrix[i][j]=0;
                for (int k = 0; k < iMatrix[0].length; k++) {
                    multipliedMatrix[i][j] += iMatrix[i][k] * rMatrix[k][j];
                }
            }
        }
        return multipliedMatrix;
    }

    private static int[][] getBackMultipliedMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] multipliedMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                multipliedMatrix[i][j]=0;
                for (int k = 0; k < matrix1[0].length; k++) {
                            multipliedMatrix[i][j] += matrix1[i][k] / matrix2[j][k];
                }

            }

        }
        return multipliedMatrix;
    }


    private static float[][] logarithm(int[][] matrix) {
        float[][] logarithmMatrix = new float[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                logarithmMatrix[i][j] = (float) Math.log(matrix[i][j]);
            }
        }
        return logarithmMatrix;
    }


    private static int[][] antiLogarithm(float[][] matrix) {
        int[][] antiLogarithmMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                antiLogarithmMatrix[i][j] = (int) Math.exp(matrix[i][j]);
            }
        }
        return antiLogarithmMatrix;
    }


    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        rsaEncryption rsa = new rsaEncryption();


        int[][] inputImageArray1 = getImagePixelMatrix("input/input1.jpg");
        System.out.println("Input image uploaded");
        int[][] referenceImageArray1 =  getImagePixelMatrix("input/input3.jpg");
        System.out.println("Reference image uploaded");
        // making the input image and reference image equal in length
        int[][] inputImageArray = equalizeMatrix(inputImageArray1, referenceImageArray1);
        int[][] referenceImageArray = equalizeMatrix(referenceImageArray1, inputImageArray1);
        int[][] xorMatrix = xorMatrix(inputImageArray, referenceImageArray);



        int[][] redMatrix = getRedPixels(inputImageArray);
        int[][] greenMatrix = getGreenPixels(inputImageArray);
        int[][] blueMatrix = getBluePixels(inputImageArray);
        int[][] alphaMatrix = getAlphaPixels(inputImageArray);

        int[][] redMatrix1 = getRedPixels(referenceImageArray);
        int[][] greenMatrix1 = getGreenPixels(referenceImageArray);
        int[][] blueMatrix1 = getBluePixels(referenceImageArray);
        int[][] alphaMatrix1 = getAlphaPixels(referenceImageArray);

//        int[][] redDifferenceMatrix = getDifferenceMatrix(redMatrix, redMatrix1);
//        int[][] greenDifferenceMatrix = getDifferenceMatrix(greenMatrix, greenMatrix1);
//        int[][] blueDifferenceMatrix = getDifferenceMatrix(blueMatrix, blueMatrix1);
//        int[][] alphaDifferenceMatrix = getDifferenceMatrix(alphaMatrix, alphaMatrix1);
//
//        writeToFile(redDifferenceMatrix, "output/redDifferenceMatrix.txt");
//        writeToFile(greenDifferenceMatrix, "output/greenDifferenceMatrix.txt");
//        writeToFile(blueDifferenceMatrix, "output/blueDifferenceMatrix.txt");
//        writeToFile(alphaDifferenceMatrix, "output/alphaDifferenceMatrix.txt");
//
//
//        int[][] rsaEncryptedRedMatrix = rsa.rsaEncrypt(redDifferenceMatrix);
//        int[][] rsaEncryptedGreenMatrix = rsa.rsaEncrypt(greenDifferenceMatrix);
//        int[][] rsaEncryptedBlueMatrix = rsa.rsaEncrypt(blueDifferenceMatrix);
//        int[][] rsaEncryptedAlphaMatrix = rsa.rsaEncrypt(alphaDifferenceMatrix);
//
//        writeToFile(rsaEncryptedRedMatrix, "output/rsaEncryptedRedMatrix.txt");
//        writeToFile(rsaEncryptedGreenMatrix, "output/rsaEncryptedGreenMatrix.txt");
//        writeToFile(rsaEncryptedBlueMatrix, "output/rsaEncryptedBlueMatrix.txt");
//        writeToFile(rsaEncryptedAlphaMatrix, "output/rsaEncryptedAlphaMatrix.txt");
//
//
//
//        int[][] rsaDecryptedRedMatrix = rsa.rsaDecrypt(rsaEncryptedRedMatrix);
//        int[][] rsaDecryptedGreenMatrix = rsa.rsaDecrypt(rsaEncryptedGreenMatrix);
//        int[][] rsaDecryptedBlueMatrix = rsa.rsaDecrypt(rsaEncryptedBlueMatrix);
//        int[][] rsaDecryptedAlphaMatrix = rsa.rsaDecrypt(rsaEncryptedAlphaMatrix);
//            writeToFile(rsaDecryptedRedMatrix, "output/rsaDecryptedRedMatrix.txt");
//            writeToFile(rsaDecryptedGreenMatrix, "output/rsaDecryptedGreenMatrix.txt");
//            writeToFile(rsaDecryptedBlueMatrix, "output/rsaDecryptedBlueMatrix.txt");
//            writeToFile(rsaDecryptedAlphaMatrix, "output/rsaDecryptedAlphaMatrix.txt");



        int[][] xorDecryptedMatrix = xorMatrix(xorMatrix, referenceImageArray);


        //Create a image from the array
        BufferedImage encryptedImage = new BufferedImage(inputImageArray.length, inputImageArray[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < inputImageArray.length; i++) {
            for (int j = 0; j < inputImageArray[0].length; j++) {
                encryptedImage.setRGB(i, j, xorDecryptedMatrix[i][j]);

            }
        }

        writeToFile(xorDecryptedMatrix, "output/encrypted.txt");
        System.out.println("Encrypted image created");
        File oFile = new File("output/encrypted.png");
        ImageIO.write(encryptedImage, "png", oFile);
        System.out.println("Encrypted image saved");
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time taken is: "+ String.valueOf(elapsedTime) + " nanoseconds");
    }
}



