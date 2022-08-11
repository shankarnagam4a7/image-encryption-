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
     * @see #writeToFile(BigInteger[][], String)
     */

    // Returns the given file as a matrix of integers
        private BigInteger[][] fileToMatrix(String path, int length, int width) throws IOException {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            BigInteger[][] array = new BigInteger[length][width];
            int k=0;
            int i=0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineArray = line.split(" ");
                    for (int j = 0; j < array[i].length; j++) {
                        array[i][j] = new BigInteger(lineArray[j]);
                    }
                    i++;

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


    /***Converts the given int matrix to a BigInteger Matrix
     * @param matrix
     * @return BigInteger[][]
     *
     */

    protected BigInteger[][] intToBigInteger(int[][] matrix) {
        BigInteger[][] bigIntegerMatrix = new BigInteger[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                bigIntegerMatrix[i][j] = BigInteger.valueOf(matrix[i][j]);
            }
        }
        return bigIntegerMatrix;
    }


/***Converts the given BigInteger matrix to a int matrix
     * @param bigIntegerMatrix
     * @return int[][]
     * @see #intToBigInteger(int[][])
     */



    protected int[][] bigIntegerToInt(BigInteger[][] bigIntegerMatrix){
        int[][] intMatrix = new int[bigIntegerMatrix.length][bigIntegerMatrix[0].length];
        for (int i = 0; i < bigIntegerMatrix.length; i++) {
            for (int j = 0; j < bigIntegerMatrix[0].length; j++) {
                intMatrix[i][j] = bigIntegerMatrix[i][j].intValue();
            }
        }
        return intMatrix;
    }

    /***Coverts the given matrix into a file text format and writes it to the given path
     *
     * @param matrix - which is to be written to the file
     * @param fileName - the name of the file to be written to
     */
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

    /***
     * Converts the given matrix and encrypts using the RSA Encryption algorithm
     * @param matrix - The matrix to be encrypted
     * @param e - The public key exponent
     * @param n - The public key modulus
     * @return - The encrypted matrix
     * @see #rsaDecrypt(BigInteger[][], BigInteger, BigInteger)
     * @see RSA#rsaEncrypt(BigInteger, BigInteger, BigInteger)
     */
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

    /***
     * Converts the given matrix and decrypts using the RSA Decryption algorithm
     * @param matrix - The matrix to be decrypted
     * @param d - The private key exponent
     * @param n - The private key modulus
     * @return - The decrypted matrix
     * @see #rsaEncrypt(BigInteger[][], BigInteger, BigInteger)
     * @see RSA#rsaDecrypt(BigInteger, BigInteger, BigInteger)
     */

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


    /***
     * Converts the matrix negative values to positive values
     * @param matrix - The matrix to be converted
     * @return - The converted matrix
     */

    protected int[][] makeNegative(int[][] matrix) {
        int[][] negativeMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                negativeMatrix[i][j] = -matrix[i][j];
            }
        }
        return negativeMatrix;
    }

    /***
     * Multiplies the given matrix with the given scalar
     * @param matrix - The matrix to be multiplied
     * @param i - The scalar to be multiplied with
     * @return - The multiplied matrix
     */
    private BigInteger[][] multiplyWithNumber(BigInteger[][] matrix, int i) {
        BigInteger[][] multipliedMatrix = new BigInteger[matrix.length][matrix[0].length];
        for (int j = 0; j < matrix.length; j++) {
            for (int k = 0; k < matrix[0].length; k++) {
                multipliedMatrix[j][k] = matrix[j][k].multiply(BigInteger.valueOf(i));
            }
        }
        return multipliedMatrix;
    }

    /***
     * Multiplies the given matrix with the given scalar
     * @param matrix - The matrix to be multiplied
     * @param i - The scalar to be multiplied with
     * @return - The multiplied matrix
     */

    private BigInteger[][] getBackMultipliedMatrix(BigInteger[][] matrix, int i) {
        BigInteger[][] getBackMultipliedMatrix = new BigInteger[matrix.length][matrix[0].length];
        for (int j = 0; j < matrix.length; j++) {
            for (int k = 0; k < matrix[0].length; k++) {
                getBackMultipliedMatrix[j][k] = matrix[j][k].divide(BigInteger.valueOf(i));
            }
        }
        return getBackMultipliedMatrix;
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        Scanner sc = new Scanner(System.in);
        BigInteger p = new BigInteger("867079");
        BigInteger q = new BigInteger("712781");
        BigInteger n = new BigInteger("618037436699");
        BigInteger e = new BigInteger("907");
        BigInteger d = new BigInteger("565567542643");

        imageEncryption image = new imageEncryption();
/***
 * <h1>Input Image</h1>
 */


        //Encrypting the input image using RSA Algorithm
        int[][] inputImageMatrix = image.getImagePixelMatrix("./input/iImage_1.jpg");
        int length = inputImageMatrix.length;
        int width = inputImageMatrix[0].length;
        BigInteger[][] inputImageMatrixBigInteger = image.intToBigInteger(inputImageMatrix);
        BigInteger[][] encryptedImageMatrixBigInteger = image.rsaEncrypt(inputImageMatrixBigInteger, e, n);


        /***
         * <h1>Reference Image</h1>
         */

        //Encrypting the reference image using RSA Algorithm
        int[][] referenceImageMatrix = image.getImagePixelMatrix("./input/rImage.jpg");
        int rLength = referenceImageMatrix.length;
        int rWidth = referenceImageMatrix[0].length;
        if (length > rLength || width > rWidth) {
            System.out.println("Error: Input image is larger than reference image so selecting the reference image as the input image for encryption");
            System.exit(0);
        }
        BigInteger[][] referenceImageMatrixBigInteger = image.intToBigInteger(referenceImageMatrix);
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


        BigInteger[][] fileToMatrix = image.fileToMatrix("./output/encrypted.txt", length, width);
//        image.writeToFile(fileToMatrix, "./output/encrypted_1.txt");


        //Decrypting the encrypted image using Hybrid Algorithm
        //Inverse Round 1
        BigInteger[][] recoverMultipliedMatrix3 = image.multiplyWithNumber(fileToMatrix, (int)length/width );
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


        BigInteger[][] recoverXorMatrix = image.xorMatrix(recoverColumnShiftedMatrix, encryptedReferenceImageMatrixBigInteger);

        BigInteger[][] decryptedImageMatrixBigInteger = image.rsaDecrypt(recoverXorMatrix, d, n);
        int[][] decryptedImageMatrix = image.bigIntegerToInt(decryptedImageMatrixBigInteger);
        decryptedImageMatrix = image.makeNegative(decryptedImageMatrix);
        image.writeImage(decryptedImageMatrix, "./output/decryptedImage.jpg");



        System.out.println("Encrypted image created");
        File oFile = new File("output/encrypted.png");
//        ImageIO.write(encryptedImage, "png", oFile);
        System.out.println("Encrypted image saved");
        long stopTime = System.nanoTime();
//        long elapsedTime = stopTime - startTime;
        System.out.println( "Time taken: " + (stopTime - startTime) / 1000000000.0 + " seconds");
    }


}