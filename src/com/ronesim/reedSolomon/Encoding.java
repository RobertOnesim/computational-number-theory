package com.ronesim.reedSolomon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ronesim on 09.03.2017.
 */
public class Encoding {
    private List<Integer> coeff;
    private BigInteger prime;
    private int s; // number of errors

    public Encoding(List<Integer> coeff, BigInteger prime, int s) {
        this.coeff = coeff;
        this.prime = prime;
        this.s = s;
    }

    public List<BigInteger> encode() {
        System.out.println("Coeff: " + Arrays.toString(coeff.toArray()));
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
                pIndexValue = pIndexValue.add(xValue.multiply(BigInteger.valueOf(coeff.get(kIndex - 1))).mod(prime)).mod(prime);
                xValue = xValue.multiply(BigInteger.valueOf(index)).mod(prime);
            }
            encrypted.add(pIndexValue);
        }
        return encrypted;
    }
}
