package com.ronesim.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronesim on 30.03.2017.
 */
public class MultiPowerRSA extends RSA {
    public MultiPowerRSA(BigInteger message) {
        super(message);
        setN(getP().multiply(getP()).multiply(getQ()));

        // private key e^-1 mod phi(multiPowerN)
        setD(keyGenerationPrivate());
    }

    @Override
    public List<BigInteger> getMultiPrimeList() {
        // Add all prime factors of the modulus N
        List<BigInteger> result = new ArrayList<>();
        result.add(getP());
        result.add(getQ());
        return result;
    }

    private BigInteger keyGenerationPrivate() {
        // phi(N) = phi(p^2 * q) = p * (p - 1) * (q - 1)
        BigInteger phi = getP().multiply((getP().subtract(BigInteger.ONE)).multiply(getQ().subtract(BigInteger.ONE)));
        return getE().modInverse(phi);
    }

    @Override
    public String toString() {
        return "MultiPowerRSA{" + "\n" +
                "message=" + getMessage() + ",\n" +
                "p=" + getP() + ",\n" +
                "q=" + getQ() + ",\n" +
                "n=" + getN() + ",\n" +
                "e=" + getE() + ",\n" +
                "d=" + getD() + ",\n" +
                '}';
    }
}
