import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 09.03.2017.
 */
public class App {
    private static int IN_BUFFER_SIZE = 256;

    public static void main(String args[]) throws IOException {
        BigInteger prime = BigInteger.probablePrime(257, new Random());
        int s = 1;

        List<Integer> coeff = new ArrayList<>();
        File file = new File("input.txt");
        byte[] block = new byte[IN_BUFFER_SIZE];
        FileInputStream fis = new FileInputStream(file);
        System.out.println("Total file size to read (in bytes) : " + fis.available());
        while (fis.read(block) != -1) {
            ByteBuffer bb = ByteBuffer.wrap(block);
            coeff.add(bb.getInt());
        }

        Encoding encoding = new Encoding(coeff, prime, s);
        List<BigInteger> encrypted = encoding.encode();

        System.out.print("Encrypted message: ");
        System.out.println(Arrays.toString(encrypted.toArray()));

        //  z has s errors
        int position = (int) (Math.random() * (encrypted.size()));
        encrypted.set(position, encrypted.get(position).add(BigInteger.ONE).mod(prime));

        Decoding decoding = new Decoding(prime);
        List<BigInteger> decrypted = decoding.decode(encrypted, s);
        System.out.print("Decrypted message: ");
        System.out.println(Arrays.toString(decrypted.toArray()));
    }

}
