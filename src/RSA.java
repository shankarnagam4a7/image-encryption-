import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;


/**
 * <h1>RSA Encryption and Decryption using Big Integer</h1>
 */
public class RSA
{
    // P and Q are two large prime numbers
    protected BigInteger P;
    protected BigInteger Q;
    // N is the product of P and Q
    protected BigInteger N;
    // Phi is the totient of N (Euler's totient function) i.e. Euler's phi function (P-1)*(Q-1)
    protected BigInteger PHI;
    // E is the prime number that is relatively prime to PHI
    protected BigInteger e;
    // D is the multiplicative inverse of E mod PHI (E*D mod PHI = 1) Private key
    protected BigInteger d ;
    // The length of the key in bits
    protected int maxLength = 20;

    private Random R;


    /**
     *<h2>Constructor</h2>
     * @parma P prime number
     * @parma Q prime number
     * @parma E prime number
     * @parma D private key
     * @parma N product of P and Q
     * @parma PHI totient of N (Euler's totient function) i.e. Euler's phi function (P-1)*(Q-1)
     *
     * @detailsAboutParameters
     *  P and Q are two large prime numbers <br>
     *  E is the prime number that is relatively prime to PHI <br>
     *  D is the multiplicative inverse of E mod PHI (E<sup>D</sup> mod PHI = 1) Private key <br>
     *  N is the product of P and Q
     *  PHI is the totient of N (Euler's totient function) i.e. Euler's phi function (P-1)*(Q-1)
     */
    public RSA() {
        // Generate two large prime numbers
        R = new Random();
        // Generate P
        P = BigInteger.probablePrime(maxLength, R);
        System.out.println("P: " + P);
        // Generate Q
        Q = BigInteger.probablePrime(maxLength, R);
        System.out.println("Q: " + Q);
        // N = P*Q
        N = P.multiply(Q);
        // PHI = (P-1)*(Q-1)
        PHI = P.subtract(BigInteger.ONE).multiply(  Q.subtract(BigInteger.ONE));
        // Generate E (e) which is relatively prime to PHI
        e = BigInteger.probablePrime(maxLength / 2, R);
        // Generate D (d) which is the multiplicative inverse of E mod PHI
        while (PHI.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(PHI) < 0) {
            e.add(BigInteger.ONE);
        }
        // d = E^-1 mod PHI

        d = e.modInverse(PHI);
        System.out.println("Private key: " + d);
    }

    /**
     * <h2>Encrypts a message using the public key.</h2>
     * @param m
     * @param e
     * @param n
     * @formula c = m<sup>e</sup> mod n
     * @return Encrypted message (c)
     */
    protected BigInteger rsaEncrypt(BigInteger m, BigInteger e, BigInteger n) {
//        System.out.println("e: " + e);
        // modPow is the same as m^e mod n
        return m.modPow(e, n);
    }



    /**
     * <h2>Decrypts a message using the private key.</h2>
     * @param c
     * @param d
     * @param n
     * @formula m = c<sup>d</sup>mod n
     * @return Decrypted message (m)
     */
    protected BigInteger rsaDecrypt(BigInteger c, BigInteger d, BigInteger n) {
        //modPow is the same as c^d mod n

        return c.modPow(d, n);
    }

    /**
     * <h2>Encrypts a message using the public key.</h2>
     * @param m
     * @param e
     * @param n
     * @formula c = m<sup>e</sup> mod n
     * @see #rsaEncrypt(BigInteger[][], BigInteger, BigInteger)
     */
    protected BigInteger[][] rsaEncrypt(BigInteger[][] m, BigInteger e, BigInteger n) {
        BigInteger[][] c = new BigInteger[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                c[i][j] = rsaEncrypt(m[i][j], e, n);
            }
        }

        return c;
    }

    /**
     * <h2>Decrypts a message using the private key.</h2>
     * @param c
     * @param d
     * @param n
     * @formula m = c<sup>d</sup>mod n
     * @see #rsaDecrypt(BigInteger[][], BigInteger, BigInteger)
     */
    protected BigInteger[][] rsaDecrypt(BigInteger[][] c, BigInteger d, BigInteger n) {
        BigInteger[][] m = new BigInteger[c.length][c[0].length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                m[i][j] = rsaDecrypt(c[i][j], d, n);
            }
        }
        return m;
    }

    public RSA(BigInteger p , BigInteger q, BigInteger e, BigInteger d) {
        this.P = p;
        this.Q = q;
        this.e = e;
        this.d = d;
        this.N = p.multiply(q);
        this.PHI = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));


    }

    public static void main (String [] arguments) throws IOException
    {
        long startTime = System.nanoTime();
        RSA rsa = new RSA();
        System.out.println("E: " + rsa.e);
        System.out.println("D: " + rsa.d);
        System.out.println("N: " + rsa.N);

        DataInputStream input = new DataInputStream(System.in);
        System.out.println("Enter message you wish to send.");
        String inputString = input.readLine();
        BigInteger message = new BigInteger(String.valueOf(inputString));

        BigInteger encrypt = rsa.rsaEncrypt(message, rsa.e, rsa.N);
        System.out.println("Encrypted message: " + encrypt);

        BigInteger decrypt = rsa.rsaDecrypt(encrypt, rsa.d, rsa.N);
        System.out.println("Decrypted message: " + decrypt);
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + "nanoseconds");
        //time in seconds = time in milliseconds / 1000
    }
}