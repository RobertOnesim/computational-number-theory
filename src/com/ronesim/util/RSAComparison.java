package com.ronesim.util;

import com.ronesim.rsa.*;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by ronesim on 30.03.2017.
 */

/**
 * Class used for comparison between multi-prime RSA, multi-power RSA and library RSA decryption.
 */
public class RSAComparison {
    private HashMap<RSA, BigInteger> rsaObjects = new HashMap<>();
    private List<BigInteger> libResult = new ArrayList<>();
    private List<BigInteger> algorithmResult = new ArrayList<>();

    public RSAComparison(int numberOfInstances, String type) {
        Random rnd = new Random();
        for (int i = 0; i < numberOfInstances; i++) {
            RSA rsaObject;
            switch (type) {
                case "multi-prime":
                    rsaObject = new MultiPrimeRSA(BigInteger.probablePrime(512, rnd));
                    break;
                case "multi-power":
                    rsaObject = new MultiPowerRSA(BigInteger.probablePrime(512, rnd));
                    break;
                default:
                    System.out.println("Invalid type");
                    return;
            }
            rsaObjects.put(rsaObject, new Encrypt().encrypt(rsaObject.getMessage(), rsaObject.getN(), rsaObject.getE()));
        }
    }

    public void run(String type) {
        for (Map.Entry<RSA, BigInteger> entry : rsaObjects.entrySet()) {
            RSA key = entry.getKey();
            BigInteger value = entry.getValue();
            Decrypt decryptLibrary;
            BigInteger decrypted;
            switch (type) {
                case "library":
                    decryptLibrary = new Decrypt(value, key.getMultiPrimeList(), key.getD());
                    decrypted = decryptLibrary.getDecryptedTextLibrary(key.getN());
                    libResult.add(decrypted);
                    break;
                case "multi-prime":
                    decryptLibrary = new Decrypt(value, key.getMultiPrimeList(), key.getD());
                    decrypted = decryptLibrary.getDecryptedTextGarner();
                    algorithmResult.add(decrypted);
                    break;
                case "multi-power":
                    decryptLibrary = new Decrypt(value, key.getMultiPrimeList(), key.getD(), key.getE());
                    decrypted = decryptLibrary.getDecryptedTextMultiPower();
                    algorithmResult.add(decrypted);
                    break;
                default:
                    System.out.println("Bad type");
                    break;
            }
        }
    }

    public boolean compareResults() {
        return libResult.equals(algorithmResult);
    }
}
