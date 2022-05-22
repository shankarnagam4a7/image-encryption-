public class rsaEncryption {
    int p,q,e;
    rsaEncryption(){
        p=3;
        q=11;
        e=3;
    }

    private int rsaEncrypt(int m) {
        int n = p * q;
        System.out.println("public key: "+n);
        long c = (long) Math.pow(m, e);
        c = c % n;
        System.out.println("rsa Encrypt : "+c);
        return (int) ((Math.pow(m, e) % n));
    }

    protected int[][] rsaEncrypt(int[][] m) {
        int n = p * q;
        System.out.println("public key: "+n);
        int[][] c = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                c[i][j] = (int) ((Math.pow(m[i][j], e) % n));
            }
        }
        return c;
    }


    protected int[][] rsaDecrypt(int[][] c) {
        int n = p * q;

        int d = (int) findPrivate();
        int[][] m = new int[c.length][c[0].length];

        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                long m1 = (long) Math.pow(c[i][j], d);
                m[i][j] = (int) (m1 % n);
            }
        }
        return m;
    }

    private  int rsaDecrypt( int d, int c) {
        int n = p * q;
        long m = (long) Math.pow(c, d);
        System.out.println("m after Math.pow(): "+m);
        m = m % n;
        System.out.println("RSA Decrypt: "+m);
        return (int) (m);
    }


    protected double findPrivate() {
        int phi = (p - 1) * (q - 1);
        double d = 0;
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
        double pKey = rsa.findPrivate();
        int c = rsa.rsaEncrypt(3);
        rsa.rsaDecrypt( (int) pKey,c);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("=Time taken is: "+duration + " nanoseconds");

    }
}
