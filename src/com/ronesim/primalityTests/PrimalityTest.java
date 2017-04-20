package com.ronesim.primalityTests;

import com.ronesim.util.PrimeUtil;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by ronesim on 20.04.2017.
 */
public class PrimalityTest {
    private int number;
    private int securityParam;
    private int s; // user for Lucas-Lehmer primality test for Mersenne numbers
    private BigInteger MersenneNumber;

    public PrimalityTest(int number, int securityParam) {
        this.number = number;
        this.securityParam = securityParam;
    }

    public PrimalityTest(int s) {
        this.s = s;
        this.MersenneNumber = new BigInteger(String.valueOf(BigInteger.valueOf(2).pow(s))).subtract(BigInteger.ONE);
    }

    public String solovayStrassen() {
        Random rnd = new Random();
        PrimeUtil primeUtil = new PrimeUtil();
        for (int index = 1; index <= securityParam; index++) {
            // choose random integer between 2 and number - 2
            int randomNum = rnd.nextInt(number - 3) + 2;
            // compute r = randomNum ^(number-1)/2 mod number
            long r = primeUtil.expLogMod(randomNum, (number - 1) / 2, number);
            if (r != 1 && r != number - 1) return "composite";
            // compute Jacobi symbol
            int jacobi = primeUtil.jacobiSymbol(randomNum, number);
            if (jacobi == -1) jacobi += number;
            if (r % number != jacobi % number) return "composite";
        }
        return "prime";
    }

    public String lucasLehmerMersenne() {
        PrimeUtil primeUtil = new PrimeUtil();
        if (!primeUtil.isPrime(s)) return "composite";
        BigInteger u = BigInteger.valueOf(4);
        for (int k = 1; k <= s - 2; k++) {
            BigInteger aux = reductionModMersenne(u);
            u = aux.multiply(aux).subtract(BigInteger.valueOf(2)).mod(MersenneNumber);
        }
        if (u.equals(BigInteger.ZERO)) return "prime";
        return "composite";
    }

    private BigInteger reductionModMersenne(BigInteger num) {
        BigInteger mod = new BigInteger(String.valueOf(BigInteger.valueOf(2).pow(s)));
        BigInteger q = num.divide(mod);
        BigInteger r = num.mod(mod);
        while (q.compareTo(BigInteger.ZERO) == 1) {
            r = r.add(q.mod(mod));
            q = q.divide(mod);
        }
        r = r.mod(MersenneNumber);
        return r;

    }

    public BigInteger getMersenneNumber() {
        return MersenneNumber;
    }
}
