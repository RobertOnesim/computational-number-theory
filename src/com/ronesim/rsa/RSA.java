package com.ronesim.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 29.03.2017.
 */
public class RSA {
    private BigInteger message;
    private BigInteger p;
    private BigInteger q;
    private BigInteger r;
    private BigInteger multiPrimeN;
    private BigInteger multiPowerN;
    private BigInteger e;
    private BigInteger d;

    public RSA(BigInteger message) {
        Random rnd = new Random();
        this.message = message;
        //  generate p,q,r prime numbers
        this.p = BigInteger.probablePrime(512, rnd);
        this.q = BigInteger.probablePrime(512, rnd);
        this.r = BigInteger.probablePrime(512, rnd);

        this.multiPrimeN = p.multiply(q).multiply(r);
        this.multiPowerN = p.multiply(p).multiply(q);

        // public key is 2^^16 + 1
        this.e = new BigInteger(String.valueOf(65537));
        // private key e^-1 mod phi(multiPrimeN)
        this.d = keyGenerationPrivate();

    }

    private BigInteger keyGenerationPrivate() {
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)).multiply(r.subtract(BigInteger.ONE));
        return e.modPow(BigInteger.valueOf(-1), phi);
    }

    /**
     * Used for decryption
     *
     * @return a list of prime numbers (private keys)
     */
    public List<BigInteger> getPrimeList() {
        List<BigInteger> result = new ArrayList<>();
        result.add(p);
        result.add(q);
        result.add(r);
        return result;
    }

    public BigInteger getMessage() {
        return message;
    }

    public void setMessage(BigInteger message) {
        this.message = message;
    }

    public BigInteger getMultiPrimeN() {
        return multiPrimeN;
    }

    public void setMultiPrimeN(BigInteger multiPrimeN) {
        this.multiPrimeN = multiPrimeN;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getMultiPowerN() {
        return multiPowerN;
    }

    public void setMultiPowerN(BigInteger multiPowerN) {
        this.multiPowerN = multiPowerN;
    }

    @Override
    public String toString() {
        return "RSA{" + "\n" +
                "message=" + message + ",\n" +
                "p=" + p + ",\n" +
                "q=" + q + ",\n" +
                "r=" + r + ",\n" +
                "multiPrimeN=" + multiPrimeN + ",\n" +
                "e=" + e + ",\n" +
                "d=" + d + ",\n" +
                '}';
    }
}
