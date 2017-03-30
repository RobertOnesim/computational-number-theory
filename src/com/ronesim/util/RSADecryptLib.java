package com.ronesim.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronesim on 30.03.2017.
 */

/**
 * Class contains helper function used in RSA decryption.
 * 1. Garner's Algorithm - used in multi-prime RSA
 * 2. henselLifting - used in multi-power RSA
 */
public class RSADecryptLib {
    /**
     * Function is used to recover the plaintext (Mp) in Z/(p^2)Z, from a message which has been
     * computed in Z/pZ instead of Z/(p^2)Z
     * <p>
     * Mp = K0 + p * K1 mod (p^2)
     *
     * @param C  - cryptoText
     * @param Cp - cryptoText mod p^2
     * @param Dp - d mod p-1
     * @param p  - Factor p of the modulus N
     * @param e  - public key
     * @return Mp - plaintext M mod p^2
     */
    public BigInteger henselLifting(BigInteger C, BigInteger Cp, BigInteger Dp, BigInteger p, BigInteger e) {
        BigInteger eInvP = e.modInverse(p);
        BigInteger pSquare = p.multiply(p); // compute p^2
        BigInteger Mp = Cp.modPow(Dp.subtract(BigInteger.ONE), p);
        BigInteger K0 = Mp.multiply(Cp).mod(p);
        BigInteger A = (C.mod(pSquare)).subtract(K0.modPow(e, pSquare)).mod(pSquare);
        Mp = Mp.multiply(A).mod(pSquare);
        Mp = Mp.multiply(eInvP).mod(pSquare);
        Mp = Mp.add(K0).mod(pSquare);
        return Mp;
    }


    /**
     * Efficient method for determining the message using prime numbers and residues.
     *
     * @param primes   list of prime numbers (private keys) P1, P2...
     * @param residues of cryptoText^D(Pi) mod Pi where D(Pi) = D mod (Pi-1)
     * @return decrypted message
     */
    public BigInteger garnerAlgorithm(List<BigInteger> primes, List<BigInteger> residues) {
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
