
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.security.*;
import java.util.ArrayList;
import javax.crypto.Cipher;


/**
 * <h1>AES Encryption</h1>
 * This class is used to encrypt and decrypt images using AES.
 * The encryption and decryption is done using the AES algorithm.
 * The key is used to encrypt and decrypt the image.
 *
 */

public class aesEncryption {

    public static void main(String[] args) {
        aesEncryption aes = new aesEncryption();
        long startTime = System.nanoTime();
        // Get input file
        File inputFile = new File("output/pixels.txt");
        // Get output file
        File encryptedFile = new File("output/text.png");
        // Get text file to decrypt
        File decryptedFile = new File("output/decrypted-text.txt");
        try {
            System.out.println("Processing the image...");
            // Upload the image
            BufferedImage image = ImageIO.read(new File("input/iImage.jpg"));
            System.out.println("Image uploaded");
            // Get image width and height
            int width = image.getWidth();
            int height = image.getHeight();
            // calculate the size of the image
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable using PixelGrabber
            //PixelGrabber used to grab the image data i.e. the pixels
            PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            // This method blocks until all pixels are retrieved
            pgb.grabPixels();

            // Write pixels to CSV file
            aes.writeTextFile("./output/pixels.txt", pixels, width);

            // fileProcessor is used to encrypt and decrypt the image data based on the Cipher Mode
            aes.fileProcessor(Cipher.ENCRYPT_MODE,"1234567890123456",inputFile,encryptedFile);


            // It's supposed that user modifies pixels file here

            // Here fileProcessor is used to decrypt the image data based on the Cipher Mode
            aes.fileProcessor(Cipher.DECRYPT_MODE,"1234567890123456",encryptedFile,decryptedFile);

            // Read pixels from CSV
            int[] parsedPixels = aes.readTextFile("./output/decrypted-text.txt", width, height);

            // Convert pixels to image and save
            aes.textToImage("./output/output.jpeg", width, height, parsedPixels);

        } catch (Exception exc) {
            // Handle exception
            System.out.println("Interrupted: " + exc.getMessage());
        }
        // Calculate the time taken to encrypt and decrypt the image
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time taken is: "+ String.valueOf(elapsedTime) + " nanoseconds");
    }


    /**
     * This method is used to encrypt and decrypt the image data based on the Cipher Mode
     * @param path - path of the image
     * @param data - data to be encrypted or decrypted
     * @param width - width of the image
     * @details - This method is used to write the image data to a file and read the data from the file
     * @return - does not return anything but it writes the data to a file
     */

    // Write pixels to CSV file and save it
    protected void writeTextFile(String path, int[] data, int width) throws IOException {
        try {
            // Create a new file with the given path
            FileWriter f = new FileWriter(path);
            // Write pixel info to file, comma separated
            for (int i = 0; i < data.length; i++) {
                // Write the pixel info to file
                String s = Integer.toString(data[i]);

                f.write(s + ", ");
                // If the pixel is not the last one, add a comma
                if (i % width == 0) f.write(System.lineSeparator());
            }
            f.close();
            System.out.println("File written successfully!");
        } catch (Exception exc) {
            System.out.printf("Error: %s", exc.getMessage());
        }

    }

    /**
     * This method is used to read the image data from a file
     * @param path - path of the image
     * @param width - width of the image
     * @param height - height of the image
     * @details - This method is used to read the image data from a file
     * @return - returns the image data
     */

    // Read pixels from CSV file and store in 'pixels' variable
    protected int[] readTextFile(String path, int width, int height) throws IOException {
        System.out.println("Processing text file...");
        // Create a new file with the given path
        BufferedReader csv = new BufferedReader(new FileReader(path));
        int[] data = new int[width * height];

        // Retrieves array of pixels as strings
        String[] stringData = parseCSV(csv);

        // Converts array of pixels to ints
        for(int i = 0; i < stringData.length; i++) {
            String num = stringData[i];
            data[i] = Integer.parseInt(num.trim());
        }
        return data;
    }

    /**
     * This method is used to convert the image data to an image
     * @param cipherMode - cipher mode used to encrypt or decrypt the image data (AES)
     * @param key - key used to encrypt or decrypt the image data (AES)
     * @param inputFile - path of the image
     * @param outputFile - path of the image
     *
     */

    // fileProcessor takes in the Cipher Mode, the key and the input file and encrypted file to encrypt or decrypt the image
    protected  void fileProcessor(int cipherMode,String key,File inputFile,File outputFile){
        try {
            // Create a new cipher with the given key and cipher mode
            // The key is used to encrypt or decrypt the image
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // Initialize the cipher with the given cipher mode and key
            cipher.init(cipherMode, secretKey);

            // Read the input file
            FileInputStream inputStream = new FileInputStream(inputFile);
            // Create a new byte array to store the input file
            byte[] inputBytes = new byte[(int) inputFile.length()];
            // Read the input file into the byte array
            inputStream.read(inputBytes);
            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
            // Close the input and output streams
            inputStream.close();
            outputStream.close();

        } catch (Exception exc) {
            System.out.printf("Error: %s", exc.getMessage());
//            e.printStackTrace();
        }
    }


    // parseCSV takes in a BufferedReader and returns an array of strings
    protected  String[] parseCSV(BufferedReader csv) throws IOException {

        ArrayList<String> fileData = new ArrayList<>();
        String row;

        // Fulfills 'fileData' with list of rows
        while ((row = csv.readLine()) != null) fileData.add(row);

        // Joins 'fileData' values into single 'joinedRows' string
        StringBuilder joinedRows = new StringBuilder();
        for(String line : fileData) joinedRows.append(line);

        // Splits 'joinedRows' by comma and returns array of pixels
        return joinedRows.toString().split(", ");
    }


    // textToImage takes in the path to the text file and the width and height of the image to be created and creates an image
    /**
     * This method is used to convert the text file to an image
     * @param path - path of the text file
     * @param width - width of the image
     * @param height - height of the image
     * @param data - array of pixels
     * @throws IOException - exception thrown if the file is not found
     * @throws FileNotFoundException - exception thrown if the file is not found
     *
     */
    protected  void textToImage(String path, int width, int height, int[] data) throws IOException {
        // MemoryImageSource takes in an array of pixels and the width and height of the image to be created
        MemoryImageSource mis = new MemoryImageSource(width, height, data, 0, width);
        // Creates a new image with the given MemoryImageSource
        Image im = Toolkit.getDefaultToolkit().createImage(mis);
        // Creates a new BufferedImage with the given image and the given width, height and type of image (TYPE_INT_RGB)
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Draws the image to the BufferedImage
        bufferedImage.getGraphics().drawImage(im, 0, 0, null);
        // Writes the BufferedImage to the given path
        ImageIO.write(bufferedImage, "jpg", new File(path));
        System.out.println("Done! Checkout the result in the image folder.");
    }
}
