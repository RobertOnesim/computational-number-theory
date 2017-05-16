package com.ronesim.discreteLogarithm;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 5/15/2017.
 */
public class PrimitiveRoot {
    private BigInteger mod;

    public PrimitiveRoot(BigInteger mod) {
        this.mod = mod;
    }

    // Algorithm for mod = 2 * q + 1
    public BigInteger generateRoot() {
        BigInteger gama = randomBigInteger(mod.subtract(BigInteger.valueOf(2)));
        return gama.multiply(gama).multiply(BigInteger.valueOf(-1)).mod(mod);
    }

    // Algorithm for every mod prime number
    public BigInteger generateRoot(HashMap<Integer, Integer> factorization) {
        Random rnd = new Random();
        BigInteger alpha;
        boolean ok;
        do {
            ok = true;
            alpha = new BigInteger(mod.bitLength(), rnd).mod(mod);
            for (Integer key : factorization.keySet()) {
                BigInteger exp = mod.subtract(BigInteger.ONE).divide(BigInteger.valueOf(key));
                if (alpha.modPow(exp, mod).compareTo(BigInteger.ONE) == 0) {
                    ok = false;
                    break;
                }
            }
        } while (!ok);
        return alpha;
    }

    public boolean checkPrivateRoot(BigInteger root, List<BigInteger> primeFactorization) {
        for (BigInteger aPrimeFactorization : primeFactorization) {
            BigInteger exp = mod.subtract(BigInteger.ONE).divide(aPrimeFactorization);
            if (root.modPow(exp, mod).compareTo(BigInteger.ONE) == 0)
                return false;
        }
        return true;
    }

    private BigInteger randomBigInteger(BigInteger number) {
        Random rnd = new Random();
        BigInteger result;
        do {
            result = new BigInteger(number.bitLength(), rnd);
        } while (result.compareTo(BigInteger.valueOf(1)) <= 0 || result.compareTo(number) > 0);
        return result;
    }

    public BigInteger getMod() {
        return mod;
    }
}
