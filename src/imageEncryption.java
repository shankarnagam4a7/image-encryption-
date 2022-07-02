import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;
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
                    iArray[i][j] = (-1)*  iImage.getRGB(i, j);
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
    private BigInteger[][] shiftColumnMatrix(BigInteger[][] matrix, int shift) {
        BigInteger[][] shiftedColumnMatrix = new BigInteger[matrix.length][matrix[0].length];
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
    private BigInteger[][] shiftRowMatrix(BigInteger[][] matrix, int shift) {
        BigInteger[][] shiftedRowMatrix = new BigInteger[matrix.length][matrix[0].length];
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
    private BigInteger[][] shiftColumnRowMatrix(BigInteger[][] matrix, int shift) {
        BigInteger[][] shiftedMatrix = shiftColumnMatrix(matrix, shift);
        shiftedMatrix = shiftRowMatrix(shiftedMatrix, shift);
        return shiftedMatrix;
    }
    private BigInteger[][] recoverShiftColumnRowMatrix(BigInteger[][] matrix, int shift) {
        BigInteger[][] shiftedMatrix = shiftRowMatrix(matrix, shift);
        shiftedMatrix = shiftColumnMatrix(shiftedMatrix, shift);
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
    private void writeToFile(BigInteger[][] matrix, String path) throws IOException {
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


        private BigInteger[][] fileToMatrix(String path, int length, int width) throws IOException {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            BigInteger[][] array = new BigInteger[length][width];
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArray = line.split(",");
                for (int i = 0; i < array.length; i++) {
                    for (int j = 0; j < array[i].length; j++) {
                        array[i][j] = new BigInteger(lineArray[j]);
                    }
                }
            }
            return array;
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

    private BigInteger[][] xorMatrix(BigInteger[][] matrix1, BigInteger[][] matrix2) {
        BigInteger[][] xorMatrix = new BigInteger[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                xorMatrix[i][j] = matrix1[i][j].xor(matrix2[i][j]);
            }
        }
        return xorMatrix;
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

    private  BigInteger[][] equalizeMatrix(BigInteger[][] matrix1, BigInteger[][] matrix2) {
        BigInteger[][] equalMatrix = new BigInteger[matrix1.length][matrix1[0].length];
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


    protected BigInteger[][] intToBigInteger(int[][] matrix) {
        BigInteger[][] bigIntegerMatrix = new BigInteger[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                bigIntegerMatrix[i][j] = BigInteger.valueOf(matrix[i][j]);
            }
        }
        return bigIntegerMatrix;
    }






    protected int[][] bigIntegerToInt(BigInteger[][] bigIntegerMatrix){
        int[][] intMatrix = new int[bigIntegerMatrix.length][bigIntegerMatrix[0].length];
        for (int i = 0; i < bigIntegerMatrix.length; i++) {
            for (int j = 0; j < bigIntegerMatrix[0].length; j++) {
                intMatrix[i][j] = bigIntegerMatrix[i][j].intValue();
            }
        }
        return intMatrix;
    }

    protected void matrixToFile(int[][] matrix, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    fw.write(matrix[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected BigInteger[][] rsaEncrypt(BigInteger[][] matrix, BigInteger e, BigInteger n) {
        System.out.println("E in Encrypt: "+e);
        System.out.println("n in Encrypt: "+n);
        BigInteger[][] encryptedMatrix = new BigInteger[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                encryptedMatrix[i][j] = matrix[i][j].modPow(e, n);;
            }
        }
        return encryptedMatrix;
    }

    protected BigInteger[][] rsaDecrypt(BigInteger[][] matrix, BigInteger d, BigInteger n) {

        BigInteger[][] decryptedMatrix = new BigInteger[matrix.length][matrix[0].length];

        System.out.println("d in Decrypt: "+d);
        System.out.println("n in Decrypt: "+n);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                decryptedMatrix[i][j] = (matrix[i][j].modPow(d, n));
            }
        }
        return decryptedMatrix;
    }



    protected int[][] makeNegative(int[][] matrix) {
        int[][] negativeMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                negativeMatrix[i][j] = -matrix[i][j];
            }
        }
        return negativeMatrix;
    }

    protected void writeMatirxToTextFile(int[][] matrix, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    fw.write(matrix[i][j] + ",");
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        Scanner sc = new Scanner(System.in);
        BigInteger p = new BigInteger("867079");
        BigInteger q = new BigInteger("712781");
        BigInteger n = new BigInteger("618037436699");
        BigInteger e = new BigInteger("907");
        BigInteger d = new BigInteger("565567542643");
        System.out.println("Int MAX Value"+Integer.MAX_VALUE);


        imageEncryption image = new imageEncryption();

        //Encrypting the input image using RSA Algorithm
        int[][] inputImageMatrix = image.getImagePixelMatrix("./input/iImage_3.jpg");
        int length = inputImageMatrix.length;
        int width = inputImageMatrix[0].length;
//        image.matrixToFile(inputImageMatrix, "./output/inputImageMatrix.txt");
        BigInteger[][] inputImageMatrixBigInteger = image.intToBigInteger(inputImageMatrix);
//        image.matrixToFile(inputImageMatrixBigInteger, "./output/inputImageMatrixBigInteger.txt");
        BigInteger[][] encryptedImageMatrixBigInteger = image.rsaEncrypt(inputImageMatrixBigInteger, e, n);




        //Encrypting the reference image using RSA Algorithm
        int[][] referenceImageMatrix = image.getImagePixelMatrix("./input/rImage_1.jpg");
        int rLength = referenceImageMatrix.length;
        int rWidth = referenceImageMatrix[0].length;
        if (length > rLength || width > rWidth) {
            System.out.println("Error: Input image is larger than reference image so selecting the reference image as the input image for encryption");
            System.exit(0);
        }
//        image.matrixToFile(referenceImageMatrix, "./output/referenceImageMatrix.txt");
        BigInteger[][] referenceImageMatrixBigInteger = image.intToBigInteger(referenceImageMatrix);
//        image.matrixToFile(referenceImageMatrixBigInteger, "./output/referenceImageMatrixBigInteger.txt");
        BigInteger[][] encryptedReferenceImageMatrixBigInteger = image.rsaEncrypt(referenceImageMatrixBigInteger, e, n);
        BigInteger[][] xorMatrix = image.xorMatrix(encryptedImageMatrixBigInteger, encryptedReferenceImageMatrixBigInteger);
        //Round 1
        BigInteger[][] columnShiftedMatrix = image.shiftColumnMatrix(xorMatrix, (int)length/width);
        BigInteger[][] rowShiftedMatrix = image.shiftRowMatrix(columnShiftedMatrix, (int)width/length);
        BigInteger[][] multipliedMatrix = image.multiplyWithNumber(rowShiftedMatrix, (int)length/width);
        //Round 2
        BigInteger[][] colShiftedMatrix1 = image.shiftColumnMatrix(multipliedMatrix, (int)length/width + length%10);
        BigInteger[][] rowShiftedMatrix1 = image.shiftRowMatrix(colShiftedMatrix1, (int)length/width + length%10);
        BigInteger[][] multipliedMatrix1 = image.multiplyWithNumber(rowShiftedMatrix1, (int)length/width);
        //Round 3
        BigInteger[][] colShiftedMatrix2 = image.shiftColumnMatrix(multipliedMatrix1, (int)length/width + length%20);
        BigInteger[][] rowShiftedMatrix2 = image.shiftRowMatrix(colShiftedMatrix2, (int)length/width + length%20);
        BigInteger[][] multipliedMatrix2 = image.multiplyWithNumber(rowShiftedMatrix2, (int)length/width);
        //Round 4
        BigInteger[][] colShiftedMatrix3 = image.shiftColumnMatrix(multipliedMatrix2, (int)length/width + length%30);
        BigInteger[][] rowShiftedMatrix3 = image.shiftRowMatrix(colShiftedMatrix3, (int)length/width + length%30);
        BigInteger[][] multipliedMatrix3 = image.multiplyWithNumber(rowShiftedMatrix3, (int)length/width);





            //Row deals with length and column deals with width
        int[][] multipliedMatrixInt = image.bigIntegerToInt(multipliedMatrix3);

        image.writeToFile(multipliedMatrix3, "./output/encrypted.txt");
        System.out.println("Encrypted Image Matrix Saved to output/encrypted.txt");
//        BigInteger[][] fileToMatrix = image.fileToMatrix("./output/encrypted.txt", length, width);

//        fileToMatrix = image.fileToMatrix("./output/encrypted.txt", length, width);
//        assert fileToMatrix != null;
//        fileToMatrix = multipliedMatrix3;
//        BigInteger[][] fileToMatrixBigInteger = image.intToBigInteger(fileToMatrix);


//        System.out.println("Enter 1 to decrypt the image or 0 to exit");
//        int exit = sc.nextInt();
//        if (exit==0)
//            System.exit(0);



        //Decrypting the encrypted image using Hybrid Algorithm
        //Inverse Round 1
        BigInteger[][] recoverMultipliedMatrix3 = image.multiplyWithNumber(multipliedMatrix3, (int)length/width );
        BigInteger[][] recoverRowShiftedMatrix3 = image.shiftRowMatrix(recoverMultipliedMatrix3, length - (int)length/width - length%30);
        BigInteger[][] recoverColShiftedMatrix3 = image.shiftColumnMatrix(recoverRowShiftedMatrix3, width - (int)length/width - length%30);
        //Inverse Round 2
        BigInteger[][] recoverMultipliedMatrix2 = image.multiplyWithNumber(recoverColShiftedMatrix3, (int)length/width );
        BigInteger[][] recoverRowShiftedMatrix2 = image.shiftRowMatrix(recoverMultipliedMatrix2, length - (int)length/width - length%20);
        BigInteger[][] recoverColShiftedMatrix2 = image.shiftColumnMatrix(recoverRowShiftedMatrix2, width - (int)length/width - length%20);
        //Inverse Round 3
        BigInteger[][] recoverMultipliedMatrix1 = image.getBackMultipliedMatrix(recoverColShiftedMatrix2, (int)length/width);
        BigInteger[][] recoverRowShiftedMatrix1 = image.shiftRowMatrix(recoverMultipliedMatrix1, length-(int)length/width - length%10);
        BigInteger[][] recoverColShiftedMatrix1 = image.shiftColumnMatrix(recoverRowShiftedMatrix1, width-(int)length/width - length%10);
        //Inverse Round 4
        BigInteger[][] recoverMultipliedMatrix = image.getBackMultipliedMatrix(recoverColShiftedMatrix1, (int)length/width);
        BigInteger[][] recoverRowShiftedMatrix = image.shiftRowMatrix(recoverMultipliedMatrix, length-(int)(width/length));
        BigInteger[][] recoverColumnShiftedMatrix = image.shiftColumnMatrix(recoverRowShiftedMatrix, width-(int)(length/length));

//




        BigInteger[][] recoverXorMatrix = image.xorMatrix(recoverColumnShiftedMatrix, encryptedReferenceImageMatrixBigInteger);

        BigInteger[][] decryptedImageMatrixBigInteger = image.rsaDecrypt(recoverXorMatrix, d, n);
        int[][] decryptedImageMatrix = image.bigIntegerToInt(decryptedImageMatrixBigInteger);
        decryptedImageMatrix = image.makeNegative(decryptedImageMatrix);
        image.writeImage(decryptedImageMatrix, "./output/decryptedImage.jpg");


        image.matrixToFile(decryptedImageMatrix, "./output/decryptedImage.txt");

        System.out.println("Encrypted image created");
        File oFile = new File("output/encrypted.png");
//        ImageIO.write(encryptedImage, "png", oFile);
        System.out.println("Encrypted image saved");
        long stopTime = System.nanoTime();
//        long elapsedTime = stopTime - startTime;
        System.out.println( "Time taken: " + (stopTime - startTime) / 1000000000.0 + " seconds");
    }

    private BigInteger[][] multiplyWithNumber(BigInteger[][] addedMatrix, int i) {
        BigInteger[][] multipliedMatrix = new BigInteger[addedMatrix.length][addedMatrix[0].length];
        for (int j = 0; j < addedMatrix.length; j++) {
            for (int k = 0; k < addedMatrix[0].length; k++) {
                multipliedMatrix[j][k] = addedMatrix[j][k].multiply(BigInteger.valueOf(i));
            }
        }
        return multipliedMatrix;
    }

    private BigInteger[][] getBackMultipliedMatrix(BigInteger[][] multipliedMatrix, int i) {
        BigInteger[][] getBackMultipliedMatrix = new BigInteger[multipliedMatrix.length][multipliedMatrix[0].length];
        for (int j = 0; j < multipliedMatrix.length; j++) {
            for (int k = 0; k < multipliedMatrix[0].length; k++) {
                getBackMultipliedMatrix[j][k] = multipliedMatrix[j][k].divide(BigInteger.valueOf(i));
            }
        }
        return getBackMultipliedMatrix;
    }

}