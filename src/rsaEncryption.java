/**
 * <h1>RSA Encryption and Decryption</h1>
 * <p>
 *     This program implements RSA encryption and decryption.
 *     It uses the RSA algorithm to encrypt and decrypt messages.
 *     </p>
 */

public class rsaEncryption {
    /**
     * Constructor.
     * @parma <b>P</b>
     * @parma <b>Q</b>
     * @parma <b>E</b>
     *
     * @detailsAboutParameters
     *  P and Q are two large prime numbers <br>
     *  E is the prime number that is relatively prime to <b>PHI</b> <br>
     *  PHI is the totient of N i.e. Euler's phi function <b>(P-1)*(Q-1)</b>
     */
    int p,q,e;
    rsaEncryption(){
        p=3;
        q=11;
        e=3;
    }

    /**
     * <h2>RSA Encryption</h2>
     * @param m (message)
     * @return encrypted message
     * @details encrypts the message using RSA algorithm and returns the encrypted message in integer form (c)
     * @formula c =<code>m<sup>e</sup> mod n</code>
     */

    private int rsaEncrypt(int m) {
        int n = p * q;
        System.out.println("public key: "+n);
        long c = ((long) Math.pow(m, e))%n;
        System.out.println("rsa Encrypt : "+c);
        return (int) c;
    }


    protected int[][] rsaEncrypt(int[][] m) {
        int n = p * q;
        System.out.println("public key: "+n);
        int[][] c = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                c[i][j] = (rsaEncrypt(m[i][j]));
            }
        }
        return c;
    }


    protected int[][] rsaDecrypt(int[][] c) {
        int n = p * q;

        int d =  findPrivate();
        int[][] m = new int[c.length][c[0].length];

        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
//                long m1 = (long) Math.pow(c[i][j], d);
                m[i][j] = rsaDecrypt(d, c[i][j]);
            }
        }
        return m;
    }

    /**
     * <h2>RSA Decryption</h2>
     * <p>RSA Decryption function for encrypted message</p>
     * @param d (private key)
     * @param c (encrypted message)
     * @return Decrypted message
     * @details Decrypts the message using RSA algorithm and returns the Decrypted message in integer form (c)
     * @formula m =<code>c<sup>d</sup> mod n</code>  <br>
     *  Encrypt the message using RSA
     */

    private  int rsaDecrypt( int d, int c) {
        int n = p * q;
        long m = (long) Math.pow(c, d);
//        System.out.println("m after Math.pow(): "+m);
        m = m % n;
        System.out.println("RSA Decrypt: "+m);
        return (int) (m);
    }


    /**
     * <h2>Finding the Private Key</h2>
     * @return private key
     * @details find the private key using the formula
     * @details e is the prime number that is relatively prime to PHI
     * @details d is the private key
     * @details formula for finding the private key:if(<b>phi(n)*i +1/e==0</b> ) then <b>d=i</b>
     */
    protected int findPrivate() {
        int phi = (p - 1) * (q - 1);
        int d = 0;
        for(int i = 1; i < phi; i++) {
            d= (phi*i);
            d+=1;
            d/=e;
            if(d%1==0){
                System.out.println("Private Key: "+d);
                return d;
            }

        }
        System.out.println("Private Key: "+d);
        return d;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        rsaEncryption rsa = new rsaEncryption();
        int pKey = rsa.findPrivate();
        int c = rsa.rsaEncrypt(26);
        rsa.rsaDecrypt( pKey,c);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("=Time taken is: "+duration + " nanoseconds");
    }
}
