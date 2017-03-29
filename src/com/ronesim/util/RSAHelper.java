package com.ronesim.util;

import com.ronesim.rsa.Decrypt;
import com.ronesim.rsa.Encrypt;
import com.ronesim.rsa.RSA;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ronesim on 30.03.2017.
 */
public class RSAHelper {
    private HashMap<RSA, BigInteger> rsaObjects = new HashMap<>();

    public RSAHelper(int numberOfInstances) {
        Random rnd = new Random();
        for (int i = 0; i < numberOfInstances; i++) {
            RSA rsaObject = new RSA(BigInteger.probablePrime(512, rnd));
            rsaObjects.put(rsaObject, new Encrypt().encrypt(rsaObject.getMessage(), rsaObject.getMultiPrimeN(), rsaObject.getE()));
        }
    }

    public void run(String type) {
        for (Map.Entry<RSA, BigInteger> entry : rsaObjects.entrySet()) {
            RSA key = entry.getKey();
            BigInteger value = entry.getValue();
            Decrypt decryptLibrary = new Decrypt(value, key.getPrimeList(), key.getD());
            BigInteger decrypted;
            switch (type) {
                case "library":
                    decrypted = decryptLibrary.getDecryptedTextLibrary(key.getMultiPrimeN());
                    break;
                case "garner":
                    decrypted = decryptLibrary.getDecryptedTextGarner();
                    break;
                default:
                    System.out.println("Bad type");
                    break;
            }
        }
    }
}
