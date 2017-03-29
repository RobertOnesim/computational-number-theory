package com.ronesim.rsa;

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

    public Decrypt(BigInteger cryptoText, List<BigInteger> primes, BigInteger d) {
        this.cryptoText = cryptoText;
        this.primes = primes;
        this.d = d;
    }

    public BigInteger getDecryptedTextGarner() {
        // generate residues
        List<BigInteger> residues = new ArrayList<>();
        for (BigInteger prime : primes) {
            BigInteger dPrime = d.mod(prime.subtract(BigInteger.ONE));
            residues.add(cryptoText.modPow(dPrime, prime));
        }

        return garnerAlgorithm(residues);
    }

    public BigInteger getDecryptedTextLibrary(BigInteger n) {
        return cryptoText.modPow(d, n);
    }

    private BigInteger garnerAlgorithm(List<BigInteger> residues) {
        List<BigInteger> C = new ArrayList<>();
        for (int i = 1; i < primes.size(); i++) {
            BigInteger Ci = BigInteger.ONE;
            for (int j = 0; j < i; j++) {
                BigInteger u = primes.get(j).modInverse(primes.get(i));
                Ci = Ci.multiply(u).mod(primes.get(i));
            }
            C.add(Ci);
        }
        BigInteger preCalculatedProdPrimes = primes.get(0);
        BigInteger result = residues.get(0);
        for (int i = 1; i < primes.size(); i++) {
            BigInteger u = (residues.get(i).subtract(result)).multiply(C.get(i - 1)).mod(primes.get(i));
            result = result.add(u.multiply(preCalculatedProdPrimes));
            preCalculatedProdPrimes = preCalculatedProdPrimes.multiply(primes.get(i));
        }
        return result;
    }
}
