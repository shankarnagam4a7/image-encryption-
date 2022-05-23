import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;



public class imageEncryption {
    /**
     *
     * @param path - The path to the image to be encrypted
     * @return -  2-dimensional array of the image specified by the path
     * @throws IOException - Throws an IOException if the image at the given path cannot be read
     * @details BufferedImage.getRGB() returns an integer representing the color of the pixel at the given coordinates.
     *@details The integer is in the form of 0xAARRGGBB.
     *         The first 8 bits represent the alpha value, the next 8 bits represent the red value,
     *         the next 8 bits represent the green value, and the last 8 bits represent the blue value.
     *@details The alpha value is the transparency of the pixel.
     *@details The red value is the amount of red in the pixel.
     *@details The green value is the amount of green in the pixel.
     *@details The blue value is the amount of blue in the pixel.
     */

    private  int[][] getImagePixelMatrix(String path) throws IOException {
        return getImgMatrix(path);
    }
    // Returns the pixel matrix of the image at the given path
    private int[][] getImgMatrix(String path) throws IOException {
        try {
            File iFile = new File(path);
            System.out.println("File: " + iFile.getName());
            BufferedImage iImage = ImageIO.read(iFile);
            int[][] iArray = new int[iImage.getWidth()][iImage.getHeight()];
            System.out.println("Width: " + iImage.getWidth() + " Height: " + iImage.getHeight());
            for (int i = 0; i < iImage.getWidth(); i++) {
                for (int j = 0; j < iImage.getHeight(); j++) {
                    iArray[i][j] =  iImage.getRGB(i, j);
                }
            }
            return iArray;
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    /**
     * Creates a new image with the given matrix and saves it to the given path
     * @param iArray - The 2-dimensional array of the image to be encrypted
     * @param path - The path to the image to be encrypted
     * @throws IOException - Throws an IOException if the image at the given path cannot be read
     * @details BufferedImage.setRGB() sets the color of the pixel at the given coordinates.
     * @details File - The file to be written to.
     * @details ImageIO.write - Writes an image to a file in a specified format.
     *
     */

    // Writes the image at the given path with the given pixel matrix
    private void writeImage(int[][] iArray, String path) throws IOException {
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


    /**
     * Shift the given matrix by the given amount of  columns
     * @param matrix - The matrix to be shifted
     * @param shift - The amount of columns to shift the matrix by
     * @return - The shifted matrix
     *
     */

    // Shifts the columns of the given matrix by the given amount of columns
    private int[][] shiftColumnMatrix(int[][] matrix, int shift) {
        int[][] shiftedColumnMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedColumnMatrix[i][j] = matrix[i][(j + shift) % matrix[0].length];
            }
        }
        return shiftedColumnMatrix;
    }

    /**
     * Shift the given matrix by the given amount of rows
     * @param matrix - The matrix to be shifted
     * @param shift - The amount of rows to shift the matrix by
     * @return - The shifted matrix
     */

    // Shifts the rows of the given matrix by the given amount of rows
    private int[][] shiftRowMatrix(int[][] matrix, int shift) {
        int[][] shiftedRowMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                shiftedRowMatrix[i][j] = matrix[(i + shift) % matrix.length][j];
            }
        }
        return shiftedRowMatrix;
    }

    /**
     * Rotate the given matrix by the given amount of degrees
     * @param matrix - The matrix to be rotated
     * @param shift - The amount of degrees to rotate the matrix by
     * @return - The rotated matrix
     * @see #shiftRowMatrix(int[][], int)
     * @see #shiftColumnMatrix(int[][], int)
     */

    // Shifts the columns and rows of the given matrix by the given amount of rows and columns
    private int[][] shiftColumnRowMatrix(int[][] matrix, int shift) {
        int[][] shiftedMatrix = shiftColumnMatrix(matrix, shift);
        shiftedMatrix = shiftRowMatrix(shiftedMatrix, shift);
        return shiftedMatrix;
    }

    /**
     * Write the given matrix to the given path as a text file
     * @param matrix - The matrix to be written
     * @param path - The path to write the matrix to
     * @throws IOException - If the file cannot be written
     * @details FileWriter - Writes to a file
     */

    // Writes the given matrix to the given path
    private void writeToFile(int[][] matrix, String path) throws IOException {
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

    /**
     * Read the given path and return the matrix as an int[][]
     * @param path - The path to read the matrix from
     * @return - The matrix as an int[][]
     * @throws IOException - If the file cannot be read
     * @details FileReader - Reads from a file
     * @see #writeToFile(int[][], String)
     */

    // Returns the given file as a matrix of integers
    private int[][] fileToMatrix(String path) throws IOException {
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

    /**
     * Returns 2-dimensional array matrix of integers by reading text file specified at given path
     * @param path - The path to read the matrix from
     * @return - The matrix as an int[][]
     * @throws IOException - If the file cannot be read
     * @details FileReader - Reads from a file
     */
    private int[][] fileToMatrixAtGivenPoint(String path) throws IOException {
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


    /**
     * Performing the XORing operation on the two given matrices and returning the result
     * @param matrix1 - The first matrix to XOR (i.e. the input image matrix)
     * @param matrix2 - The second matrix to XOR (i.e. the reference image matrix)
     * @return - The XORed matrix as an int[][]
     * @details XOR Operation Works as follows:
     * @details 1. Convert the matrices (pixel values stored as int) to binary format
     * @details 2. Perform the XOR operation on each of bit with corresponding bit in the other matrix
     * @details 3. The output is high(1), if anyone or both of the bits are high, low(0) otherwise
     * @details 4. Convert the binary matrix to decimal format
     *
     */

    // xorMatrix takes two matrices and returns the xor of the two matrices
    private int[][] xorMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] xorMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                xorMatrix[i][j] = (matrix1[i][j] ^ matrix2[i][j]);
            }
        }
        return xorMatrix;
    }

    /**
     * Finding the Average of the given matrix and returning the result
     * @param matrix1 - The matrix to find the average of
     * @param matrix2 - The matrix to find the average of
     * @return - The average of the two matrices as an int
     * @details Average of the two matrices is calculated as follows: (matrix1 + matrix2) / 2
     */

    private int[][] averageMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] averageMatrix = new int[matrix2.length][matrix2[0].length];
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                averageMatrix[i][j] = (matrix1[i][j] + matrix2[i][j]) / 2;
            }
        }
        return averageMatrix;
    }

    /**
     * Getting the value of Red Color each pixel in the given matrix
     *
     * @param matrix - The matrix to get the red color of each pixel
     * @return - The red color of each pixel in the given matrix as an int[][]
     * @details Red color of each pixel is calculated as follows: (matrix[i][j] & 0xFF0000) >> 16
     * @details The value of red color is stored in the first 16 bits of the pixel
     *
     */

    // getRedPixel returns the red pixel value of the given pixel
    private int[][] getRedPixels(int[][] matrix) {
        int[][] redPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                redPixels[i][j] = (matrix[i][j] >> 16) & 0xFF;
            }
        }
        return redPixels;
    }

    /**
     * Getting the value of Blue Color each pixel in the given matrix
     * @param matrix - The matrix to get the blue color of each pixel
     * @return - The blue color of each pixel in the given matrix as an int[][]
     * @details Blue color of each pixel is calculated as follows: (matrix[i][j] & 0xFF) >> 0
     */

    private int[][] getBluePixels(int[][] matrix) {
        int[][] bluePixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                bluePixels[i][j] = matrix[i][j] & 0xFF;
            }
        }
        return bluePixels;
    }

    /**
     * Getting the value of Green Color each pixel in the given matrix
     * @param matrix - The matrix to get the green color of each pixel
     * @return - The green color of each pixel in the given matrix as an int[][]
     * @details Green color of each pixel is calculated as follows: (matrix[i][j] & 0xFF00) >> 8
     */


    private int[][] getGreenPixels(int[][] matrix) {
        int[][] greenPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                greenPixels[i][j] = (matrix[i][j] >> 8) & 0xFF;
            }

        }
        return greenPixels;
    }


    /**
     * Getting the value of Alpha Color each pixel in the given matrix
     * @param matrix - The matrix to get the alpha color of each pixel
     * @return - The alpha color of each pixel in the given matrix as an int[][]
     * @details Alpha color of each pixel is calculated as follows: (matrix[i][j] & 0xFF000000) >> 24
     */

    private int[][] getAlphaPixels(int[][] matrix) {
        int[][] alphaPixels = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                alphaPixels[i][j] = (matrix[i][j] >> 24) & 0xFF;
            }

        }
        return alphaPixels;
    }

    /**
     * Adding the given matrix with the given number
     * @param matrix - The matrix to add the given number to
     * @param number - The number to add to the given matrix
     * @return - The matrix with the given number added to it
     *
     */
    // addingWithNumbers adds the matrix with the given number and returns the result
    private  int[][] addingWithNumber(int[][] matrix, int number) {
        int[][] addedMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                addedMatrix[i][j] = matrix[i][j] + number;
            }
        }
        return addedMatrix;
    }

    /**
     * Converting the given matrix to a String and returning it
     * @param matrix1 - The matrix to convert to a String (i.e. Red Color of each pixel)
     * @param matrix2 - The matrix to convert to a String (i.e. Green Color of each pixel)
     * @param matrix3 - The matrix to convert to a String (i.e. Blue Color of each pixel)
     * @param matrix4 - The matrix to convert to a String (i.e. Alpha Color of each pixel)
     * @return - The matrix converted to a String
     * @details The matrix is converted to a String as follows:
     *         "Red Color of each pixel: " + matrix1[i][j] + "\n" + "Green Color of each pixel: " + matrix2[i][j] + "\n" + "Blue Color of each pixel: " + matrix3[i][j] + "\n" + "Alpha Color of each pixel: " + matrix4[i][j] + "\n"
     */

    private  String[][] intToString(int[][] matrix1, int[][] matrix2, int[][] matrix3, int[][] matrix4) {
        String[][] stringMatrix = new String[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                stringMatrix[i][j] = String.valueOf(matrix1[i][j]) + String.valueOf(matrix2[i][j]) + String.valueOf(matrix3[i][j]) + String.valueOf(matrix4[i][j]);
            }
        }
        return stringMatrix;
    }


    /**
     * Converting the given matrix to a int and returning it
     * @param matrix1 - The matrix to convert to a int (i.e. Red Color of each pixel)
     * @param matrix2 - The matrix to convert to a int (i.e. Green Color of each pixel)
     * @param matrix3  - The matrix to convert to a int (i.e. Blue Color of each pixel)
     * @param matrix4 - The matrix to convert to a int (i.e. Alpha Color of each pixel)
     * @return - The matrix converted to a int
     * @details The matrix is converted to a int as follows:
     *        matrix1[i][j] + matrix2[i][j] + matrix3[i][j] + matrix4[i][j]
     */

    private  int[][] stringToInt(String[][] matrix1, String[][] matrix2, String[][] matrix3, String[][] matrix4) {
        int[][] intMatrix = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                intMatrix[i][j] = Integer.parseInt(matrix1[i][j]) + Integer.parseInt(matrix2[i][j]) + Integer.parseInt(matrix3[i][j]) + Integer.parseInt(matrix4[i][j]);
            }
        }
        return intMatrix;

    }



    private  int[][] stringToInt(String[][] matrix) {
        int[][] intMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                intMatrix[i][j] = Integer.parseInt(matrix[i][j]);
            }
        }
        return intMatrix;
    }



    private  int[][] subtractingWithNumber(int[][] matrix, int number) {
        int[][] subtractedMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                subtractedMatrix[i][j] = matrix[i][j] - number;
            }
        }
        return subtractedMatrix;
    }

    /**
     * Equalizing the input matrix using the reference matrix and returning the equalized matrix
     * @param matrix1 - The matrix to equalize
     * @param matrix2 - The reference matrix
     * @return - The equalized matrix
     * @details The matrix is equalized as follows:
     * Checks the width and height of the matrix and the reference matrix.
     * If the width and height of the matrix and the reference matrix are the same, the matrix is equalized.
     * If the width and height of the matrix and the reference matrix are not the same, the matrix is not equalized.
     */



    private  int[][] equalizeMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] equalMatrix = new int[matrix1.length][matrix1[0].length];
        if (matrix1.length > matrix2.length || matrix1[0].length > matrix2[0].length) {
            for (int i = 0; i < matrix2.length; i++) {
                for (int j = 0; j < matrix2[0].length; j++) {
                    equalMatrix[i][j] = matrix2[i][j];

                }
            }
        }
        else {
            equalMatrix = matrix1;
        }
        return equalMatrix;
    }

    /**
     * Multiplies the input matrix with the reference matrix and returning the multiplied matrix
     *
     * @param iMatrix - The matrix to multiply
     * @param rMatrix - The reference matrix
     * @return - The multiplied matrix
     * @details The matrix is multiplied as follows:
     *  Formula: multipliedMatrix[i][j]= matrix1[i][k] * matrix2[k][j]
     */

    private  int[][] matrixMultiplication(int[][] iMatrix, int[][] rMatrix) {
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


    private  int[][] getBackMultipliedMatrix(int[][] matrix1, int[][] matrix2) {
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


    private  float[][] logarithm(int[][] matrix) {
        float[][] logarithmMatrix = new float[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                logarithmMatrix[i][j] = (float) Math.log(matrix[i][j]);
            }
        }
        return logarithmMatrix;
    }


    protected int[][] antiLogarithm(float[][] matrix) {
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
        aesEncryption aes = new aesEncryption();
        imageEncryption image = new imageEncryption();



        int[][] inputImageArray1 = image.getImagePixelMatrix("input/iImage_1.jpg");
//            System.out.println(inputImageArray1[0][0]);

        System.out.println("Input image uploaded");
        int[][] referenceImageArray1 =  image.getImagePixelMatrix("input/rImage.jpg");
        System.out.println("Reference image uploaded");
//         making the input image and reference image equal in length
        int[][] inputImageArray = image.equalizeMatrix(inputImageArray1, referenceImageArray1);
        int[][] referenceImageArray = image.equalizeMatrix(referenceImageArray1, inputImageArray1);
        int[][] xorMatrix = image.xorMatrix(inputImageArray, referenceImageArray);
        int[][] rsaEncryptedMatrix = rsa.rsaEncrypt(xorMatrix);
        System.out.println("RSA Encryption done");
        image.writeToFile(rsaEncryptedMatrix, "output/rsaEncrypted_1.png");




//        int[][] redMatrix = getRedPixels(inputImageArray);
//        int[][] greenMatrix = getGreenPixels(inputImageArray);
//        int[][] blueMatrix = getBluePixels(inputImageArray);
//        int[][] alphaMatrix = getAlphaPixels(inputImageArray);
//
//        int[][] redMatrix1 = getRedPixels(referenceImageArray);
//        int[][] greenMatrix1 = getGreenPixels(referenceImageArray);
//        int[][] blueMatrix1 = getBluePixels(referenceImageArray);
//        int[][] alphaMatrix1 = getAlphaPixels(referenceImageArray);

//        int[][] redDifferenceMatrix = getDifferenceMatrix(redMatrix, redMatrix1);
//        int[][] greenDifferenceMatrix = getDifferenceMatrix(greenMatrix, greenMatrix1);
//        int[][] blueDifferenceMatrix = getDifferenceMatrix(blueMatrix, blueMatrix1);
//        int[][] alphaDifferenceMatrix = getDifferenceMatrix(alphaMatrix, alphaMatrix1);

//        writeToFile(redDifferenceMatrix, "output/redDifferenceMatrix.txt");
//        writeToFile(greenDifferenceMatrix, "output/greenDifferenceMatrix.txt");
//        writeToFile(blueDifferenceMatrix, "output/blueDifferenceMatrix.txt");
//        writeToFile(alphaDifferenceMatrix, "output/alphaDifferenceMatrix.txt");


//        int[][] rsaEncryptedRedMatrix = rsa.rsaEncrypt(redDifferenceMatrix);
//        int[][] rsaEncryptedGreenMatrix = rsa.rsaEncrypt(greenDifferenceMatrix);
//        int[][] rsaEncryptedBlueMatrix = rsa.rsaEncrypt(blueDifferenceMatrix);
//        int[][] rsaEncryptedAlphaMatrix = rsa.rsaEncrypt(alphaDifferenceMatrix);

//        writeToFile(rsaEncryptedRedMatrix, "output/rsaEncryptedRedMatrix.txt");
//        writeToFile(rsaEncryptedGreenMatrix, "output/rsaEncryptedGreenMatrix.txt");
//        writeToFile(rsaEncryptedBlueMatrix, "output/rsaEncryptedBlueMatrix.txt");
//        writeToFile(rsaEncryptedAlphaMatrix, "output/rsaEncryptedAlphaMatrix.txt");



//        int[][] rsaDecryptedRedMatrix = rsa.rsaDecrypt(rsaEncryptedRedMatrix);
//        int[][] rsaDecryptedGreenMatrix = rsa.rsaDecrypt(rsaEncryptedGreenMatrix);
//        int[][] rsaDecryptedBlueMatrix = rsa.rsaDecrypt(rsaEncryptedBlueMatrix);
//        int[][] rsaDecryptedAlphaMatrix = rsa.rsaDecrypt(rsaEncryptedAlphaMatrix);
//            writeToFile(rsaDecryptedRedMatrix, "output/rsaDecryptedRedMatrix.txt");
//            writeToFile(rsaDecryptedGreenMatrix, "output/rsaDecryptedGreenMatrix.txt");
//            writeToFile(rsaDecryptedBlueMatrix, "output/rsaDecryptedBlueMatrix.txt");
//            writeToFile(rsaDecryptedAlphaMatrix, "output/rsaDecryptedAlphaMatrix.txt");



//        int[][] xorDecryptedMatrix = xorMatrix(xorMatrix, referenceImageArray);


        //Create image from the array
//        BufferedImage encryptedImage = new BufferedImage(inputImageArray.length, inputImageArray[0].length, BufferedImage.TYPE_INT_RGB);
//        for (int i = 0; i < inputImageArray.length; i++) {
//            for (int j = 0; j < inputImageArray[0].length; j++) {
//                encryptedImage.setRGB(i, j, xorDecryptedMatrix[i][j]);
//
//            }
//        }

//        writeToFile(xorDecryptedMatrix, "output/encrypted.txt");
        System.out.println("Encrypted image created");
        File oFile = new File("output/encrypted.png");
//        ImageIO.write(encryptedImage, "png", oFile);
        System.out.println("Encrypted image saved");
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time taken is: "+ String.valueOf(elapsedTime) + " nanoseconds");
    }
}



