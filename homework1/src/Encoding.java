import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronesim on 09.03.2017.
 */
public class Encoding {
    private BigInteger message;
    private BigInteger prime;
    private int s; // number of errors

    Encoding(BigInteger message, BigInteger prime, int s) {
        this.message = message;
        this.prime = prime;
        this.s = s;
    }

    List<BigInteger> encode() {
        // represent message as a vector with (k-1) components over Zprime
        List<BigInteger> coeff = new ArrayList<>();
        while (!message.equals(BigInteger.ZERO)) {
            coeff.add(message.mod(prime));
            message = message.divide(prime);
        }

        // calculate N = k + 2*s
        int k = coeff.size() + 1;
        int N = k + 2 * s;

        // encode the message using polynomial P(x) = ak-1 * x^(k-1) + ...a1 * x; x = 1..n
        //  encrypted = [P(1), P(2), ... P(N)]
        List<BigInteger> encrypted = new ArrayList<>();
        for (int index = 1; index <= N; index++) {
            // compute P(index)
            BigInteger xValue = new BigInteger(String.valueOf(index));
            BigInteger pIndexValue = BigInteger.ZERO;
            for (int kIndex = 1; kIndex < k; kIndex++) {
                pIndexValue = pIndexValue.add(xValue.multiply(coeff.get(kIndex - 1))).mod(prime);
                xValue = xValue.multiply(xValue).mod(prime);
            }
            encrypted.add(pIndexValue);
        }
        return encrypted;
    }

    public BigInteger getMessage() {
        return message;
    }

    public void setMessage(BigInteger message) {
        this.message = message;
    }

    public BigInteger getPrime() {
        return prime;
    }

    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }
}
