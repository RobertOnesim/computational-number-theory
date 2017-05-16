package com.ronesim.discreteLogarithm;

import com.ronesim.util.RSADecryptLib;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ronesim on 5/15/2017.
 */
public class DiscreteLogarithm {
    private BigInteger mod;
    private BigInteger nPollard;
    private BigInteger alphaPollard;
    private BigInteger betaPollard;

    private BigInteger nPohlig;
    private BigInteger alphaPohlig;
    private BigInteger betaPohlig;
    private HashMap<Integer, Integer> factorization;

    // Constructor used for Pollard's algorithm
    public DiscreteLogarithm(BigInteger mod, BigInteger alpha, BigInteger beta) {
        this.mod = mod;
        this.alphaPollard = alpha;
        this.betaPollard = beta;
        this.nPollard = mod.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
    }

    // Constructor used for Pohlig-Hellman algorithm
    public DiscreteLogarithm(BigInteger mod, BigInteger alpha, BigInteger beta, HashMap<Integer, Integer> factorization) {
        this.mod = mod;
        this.nPohlig = this.mod.subtract(BigInteger.ONE);
        this.nPollard = this.mod.subtract(BigInteger.ONE);
        this.alphaPohlig = alpha;
        this.betaPohlig = beta;
        this.factorization = factorization;
    }

    public BigInteger PollardAlgorithm(int a0, int b0) {
        if (alphaPollard.compareTo(betaPollard) == 0) return BigInteger.ONE;

        BigInteger x;
        BigInteger a;
        BigInteger b;
        if (a0 == 0 && b0 == 0) {
            x = BigInteger.ONE;
            a = BigInteger.ZERO;
            b = BigInteger.ZERO;
        } else {
            a = BigInteger.valueOf(a0);
            b = BigInteger.valueOf(b0);
            x = alphaPollard.pow(a0).multiply(betaPollard.pow(b0));
        }

        BigInteger X = x;
        BigInteger A = a;
        BigInteger B = b;
        for (; ; ) {
            // compute xi, ai, bi, x2i, a2i, b2i using previous values
            List<BigInteger> result = computeXAB(x, a, b);
            x = result.get(0);
            a = result.get(1);
            b = result.get(2);

            result = computeXAB(X, A, B);
            result = computeXAB(result.get(0), result.get(1), result.get(2));

            X = result.get(0);
            A = result.get(1);
            B = result.get(2);

            if (x.compareTo(X) == 0) {
                BigInteger r = b.subtract(B).mod(nPollard);
                if (r.compareTo(BigInteger.ZERO) == 0)
                    //throw new Error("Failure");
                    return PollardAlgorithm(1, 1);
                BigInteger d = gcd(r, nPollard);
                if (d.compareTo(BigInteger.ONE) == 0)
                    return r.modInverse(nPollard).multiply(A.subtract(a)).mod(nPollard);
                return (r.divide(d)).modInverse(nPollard.divide(d)).multiply(A.subtract(a).divide(d)).mod(nPollard.divide(d));
            }

        }
    }

    public BigInteger pohligHellmanAlgorithm() {
        List<BigInteger> primes = new ArrayList<>();
        List<BigInteger> residues = new ArrayList<>();
        for (int key : factorization.keySet()) {
            BigInteger gama = BigInteger.ONE;
            BigInteger l = BigInteger.ZERO;
            BigInteger q = BigInteger.ONE;
            BigInteger alphaBar = alphaPohlig.modPow(nPohlig.divide(BigInteger.valueOf(key)), mod);
            BigInteger xi = BigInteger.ZERO;
            for (int j = 0; j < factorization.get(key); j++) {
                if (j != 0) {
                    gama = gama.multiply(alphaPohlig.modPow(l.multiply(q), mod));
                    q = q.multiply(BigInteger.valueOf(key));
                }
                BigInteger betaBar = (betaPohlig.multiply(gama.modInverse(mod))).modPow(nPohlig.divide(q.multiply(BigInteger.valueOf(key))), mod);
                alphaPollard = alphaBar;
                betaPollard = betaBar;
                l = PollardAlgorithm(0, 0);
                xi = xi.add(l.multiply(q));
            }
            primes.add(q.multiply(BigInteger.valueOf(key)));
            residues.add(xi.mod(q.multiply(BigInteger.valueOf(key))));
        }
        return new RSADecryptLib().garnerAlgorithm(primes, residues);
    }

    private List<BigInteger> computeXAB(BigInteger x, BigInteger a, BigInteger b) {
        List<BigInteger> result = new ArrayList<>();
        int value = new Integer(String.valueOf(x.mod(BigInteger.valueOf(3))));
        switch (value) {
            case 0:
                result.add(x.multiply(x).mod(mod));
                result.add(a.multiply(BigInteger.valueOf(2)).mod(nPollard));
                result.add(b.multiply(BigInteger.valueOf(2)).mod(nPollard));
                break;
            case 1:
                result.add(x.multiply(betaPollard).mod(mod));
                result.add(a);
                result.add(b.add(BigInteger.ONE).mod(nPollard));
                break;
            case 2:
                result.add(x.multiply(alphaPollard).mod(mod));
                result.add(a.add(BigInteger.ONE).mod(nPollard));
                result.add(b);
        }
        return result;
    }

    private BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.compareTo(BigInteger.ZERO) == 0) return a;
        return gcd(b, a.mod(b));
    }
}
