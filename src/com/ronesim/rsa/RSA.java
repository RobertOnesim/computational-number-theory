package com.ronesim.rsa;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 29.03.2017.
 */

public abstract class RSA {
    private BigInteger message;
    private BigInteger p;
    private BigInteger q;
    private BigInteger e;
    private BigInteger n;
    private BigInteger d;

    public RSA(BigInteger message) {
        Random rnd = new Random();
        this.message = message;

        //  generate p,q,r prime numbers
        this.p = BigInteger.probablePrime(512, rnd);
        this.q = BigInteger.probablePrime(512, rnd);

        // public key is 2^^16 + 1
        this.e = new BigInteger(String.valueOf(65537));
    }

    /**
     * Used for multi-prime decryption.
     * All prime factors of the modulus N.
     *
     * @return a list of prime numbers (private keys)
     */
    abstract public List<BigInteger> getMultiPrimeList();

    public BigInteger getMessage() {
        return message;
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

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }
}
