package com.ronesim.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 30.03.2017.
 */
public class MultiPrimeRSA extends RSA {
    private BigInteger r;

    public MultiPrimeRSA(BigInteger message) {
        super(message);

        Random rnd = new Random();
        this.r = BigInteger.probablePrime(512, rnd);
        setN(getP().multiply(getQ()).multiply(r));

        // private key e^-1 mod phi(multiPrimeN)
        setD(keyGenerationPrivate());
    }

    @Override
    public List<BigInteger> getMultiPrimeList() {
        // Add all prime factors of the modulus N
        List<BigInteger> result = new ArrayList<>();
        result.add(getP());
        result.add(getQ());
        result.add(r);
        return result;
    }

    private BigInteger keyGenerationPrivate() {
        // phi(N) = phi(p*q*r) = (p - 1) * (q - 1) * (r - 1)
        BigInteger phi = (getP().subtract(BigInteger.ONE)).multiply(getQ().subtract(BigInteger.ONE)).multiply(r.subtract(BigInteger.ONE));
        return getE().modInverse(phi);
    }

    public BigInteger getR() {
        return r;
    }

    public void setR(BigInteger r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "MultiPrimeRSA{" + "\n" +
                "message=" + getMessage() + ",\n" +
                "p=" + getP() + ",\n" +
                "q=" + getQ() + ",\n" +
                "r=" + r + ",\n" +
                "n=" + getN() + ",\n" +
                "e=" + getE() + ",\n" +
                "d=" + getD() + ",\n" +
                '}';
    }
}
