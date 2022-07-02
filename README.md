# Image Encryption
 This project is done as a part of Mini-Project in my 6th Sem.
<br>
Implementation of Image Encryption using Hybrid Algorithm (<bold>AES Algorithm</bold> and <bold>RSA Algorithm</bold>.
<br>

 <h2>About the Algorithm</h2>
<br>
 The proposed algorithm is that we will be using a hybrid algorithm (AES Algorithm and RSA Algorithm). We take an two images as input, one is which needs to be encrypted and other is reference image through which we will encrypt the input image. We convert these two images into a 2D Array of pixel values. After getting the pixel values in the 2D Array, we encrypt using RSA Algorithm. After encrypting perform some of the techniques from the AES Algorithm (i.e., XORing, Column Shifting, Row Shifting, Multiplication) to manipulate the pixel values. For Decryption, we perform a similar operation in the backway to retrieve back the RSA Encrypted Pixel values. These pixels need to be decrypted using RSA Algorithm to get back the original input image
<br>
<h2>AES Implementation</h2>
<p>AES image encryption and decryption </p>

<a href="./src/aesEncryption.java">AES Encryption Implementation</a>
<br>
<h2>RSA Algorithm</h2>
<p>Initially, we implemented RSA Algorithm for smaller numbers. After reading pixel values of image, we realized this code doesn't meet our requirement. So we have implemented for larger number using BigInteger</p>
<p>For Smaller Number</p>
<p></p>
<a href="./src/rsaEncryption.java">RSA Algorithm Implementation</a>
<p>For Larger Numbers</p>
<p>This code works for large number.</p>
<a href="./src/RSA.java">RSA Algorithm Implementation</a>

<h2>Hybrid Algorithm</h2>
This contains the complete code implementation of the Hybrid Algorithm.
<a href="./src/imageEncryption.java">Hybrid Algorithm Implementation</a>