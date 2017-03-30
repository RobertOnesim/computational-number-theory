package com.ronesim.rsa;

import com.ronesim.util.RSADecryptLib;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronesim on 30.03.2017.
 */
public class Decrypt {
    private BigInteger cryptoText;
    private List<BigInteger> primes;
    private BigInteger d;
    private BigInteger e; // used for multi-power decryption
    private RSADecryptLib rsaDecryptLib = new RSADecryptLib();

    /**
     * Used for Multi-prime decryption.
     *
     * @param cryptoText - text which should be decrypted
     * @param primes     - list of all prime factors
     * @param d          - private key
     */
    public Decrypt(BigInteger cryptoText, List<BigInteger> primes, BigInteger d) {
        this.cryptoText = cryptoText;
        this.primes = primes;
        this.d = d;
    }

    /**
     * Used for Multi-power decryption.
     *
     * @param cryptoText - text which should be decrypted
     * @param primes     - list of all prime factors
     * @param d          - private key
     * @param e          - public key
     */
    public Decrypt(BigInteger cryptoText, List<BigInteger> primes, BigInteger d, BigInteger e) {
        this.cryptoText = cryptoText;
        this.primes = primes;
        this.d = d;
        this.e = e;
    }

    /**
     * Multi-power RSA decryption using CRT and Hensel's lifting lemma.
     *
     * @return decrypted message
     */
    public BigInteger getDecryptedTextMultiPower() {
        BigInteger Dp = d.mod(primes.get(0).subtract(BigInteger.ONE));
        BigInteger Dq = d.mod(primes.get(1).subtract(BigInteger.ONE));

        BigInteger pSquare = primes.get(0).multiply(primes.get(0));
        BigInteger pSquareInvQ = pSquare.modInverse(primes.get(1));

        BigInteger Cp = cryptoText.mod(pSquare);
        BigInteger Cq = cryptoText.mod(primes.get(1));
        BigInteger Mp = rsaDecryptLib.henselLifting(cryptoText, Cp, Dp, primes.get(0), e);
        BigInteger Mq = Cq.modPow(Dq, primes.get(1));

        return Mp.add((pSquare.multiply((Mq.subtract(Mp)).multiply(pSquareInvQ)).mod(primes.get(1))));

    }

    /**
     * Multi-prime RSA decryption using CRT and Garner's algorithm.
     *
     * @return decrypted message
     */
    public BigInteger getDecryptedTextGarner() {
        // generate residues
        List<BigInteger> residues = new ArrayList<>();
        for (BigInteger prime : primes) {
            BigInteger dPrime = d.mod(prime.subtract(BigInteger.ONE));
            residues.add(cryptoText.modPow(dPrime, prime));
        }

        return rsaDecryptLib.garnerAlgorithm(primes, residues);
    }

    /**
     * RSA decryption using BigInteger Library
     *
     * @param n - public key
     * @return decrypted message
     */
    public BigInteger getDecryptedTextLibrary(BigInteger n) {
        return cryptoText.modPow(d, n);
    }

}
