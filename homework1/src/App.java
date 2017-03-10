import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 09.03.2017.
 */
public class App {
    public static void main(String args[]) {
        // BigInteger message = new BigInteger(528, new Random());
        // BigInteger prime = BigInteger.probablePrime(257, new Random());
        BigInteger message = new BigInteger(String.valueOf(29));
        BigInteger prime = new BigInteger(String.valueOf(11));
        int s = 1;
        System.out.println("Message: " + message);

        Encoding encoding = new Encoding(message, prime, s);
        List<BigInteger> encrypted = encoding.encode();

        System.out.print("Encrypted message: ");
        System.out.println(Arrays.toString(encrypted.toArray()));

        //  z has s errors
        //int position = (int) (Math.random() * (encrypted.size()));
        int position = 2;
        encrypted.set(position, new BigInteger(257, new Random()).mod(prime));
        System.out.println(position);
        Decoding decoding = new Decoding(prime);
        List<BigInteger> decrypted = decoding.decode(encrypted, s);
    }
}
