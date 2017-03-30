package com.ronesim;

import com.ronesim.reedSolomon.Decoding;
import com.ronesim.reedSolomon.Encoding;
import com.ronesim.rsa.*;
import com.ronesim.util.RSAComparison;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by ronesim on 09.03.2017.
 */
public class Application {

    public static void main(String args[]) throws IOException {
        /* check what app to run
            1. reed-solomon
            2. RSA
        */
        switch (args[0]) {
            case "reed-solomon":
                reedSolomon();
                break;
            case "multiPrimeRSA":
                multiPrimeRSA();
                break;
            case "multiPowerRSA":
                multiPowerRSA();
                break;
            default:
                System.out.println("Incorrect application specified.");
        }
    }

    private static void reedSolomon() throws IOException {
        BigInteger prime = BigInteger.probablePrime(257, new Random());
        int s = 1;

        List<Integer> coeff = new ArrayList<>();
        File file = new File("./res/inputReedSolomon.txt");
        int IN_BUFFER_SIZE = 256;
        byte[] block = new byte[IN_BUFFER_SIZE];
        FileInputStream fis = new FileInputStream(file);
        System.out.println("Total file size to read (in bytes) : " + fis.available());
        while (fis.read(block) != -1) {
            ByteBuffer bb = ByteBuffer.wrap(block);
            coeff.add(bb.getInt());
        }

        Encoding encoding = new Encoding(coeff, prime, s);
        List<BigInteger> encrypted = encoding.encode();

        System.out.print("Encrypted message: ");
        System.out.println(Arrays.toString(encrypted.toArray()));

        //  z has s errors
        int position = (int) (Math.random() * (encrypted.size()));
        encrypted.set(position, encrypted.get(position).add(BigInteger.ONE).mod(prime));

        Decoding decoding = new Decoding(prime);
        List<BigInteger> decrypted = decoding.decode(encrypted, s);
        System.out.print("Decrypted message: ");
        System.out.println(Arrays.toString(decrypted.toArray()));
    }

    private static void multiPrimeRSA() {
        Random rnd = new Random();
        BigInteger message = BigInteger.probablePrime(512, rnd);
        RSA multiPrimeRSA = new MultiPrimeRSA(message);
        BigInteger cryptoText = new Encrypt().encrypt(message, multiPrimeRSA.getN(), multiPrimeRSA.getE());
        System.out.println(multiPrimeRSA.toString());
        System.out.println("Encrypted text is: " + cryptoText);

        // Start decryption
        List<BigInteger> primesMultiPrime = multiPrimeRSA.getMultiPrimeList();

        Decrypt decrypt = new Decrypt(cryptoText, primesMultiPrime, multiPrimeRSA.getD());
        BigInteger decryptedText = decrypt.getDecryptedTextGarner();

        System.out.println("Decrypted text is: " + decryptedText + "\n");

        // Compare time between Garner's algorithm and java BigInteger library computation
        comparison("multi-prime", "library");

    }

    private static void multiPowerRSA() {
        Random rnd = new Random();
        BigInteger message = BigInteger.probablePrime(512, rnd);
        RSA multiPowerRSA = new MultiPowerRSA(message);
        BigInteger cryptoText = new Encrypt().encrypt(message, multiPowerRSA.getN(), multiPowerRSA.getE());
        System.out.println(multiPowerRSA.toString());
        System.out.println("Encrypted text is: " + cryptoText);

        // Start decryption
        List<BigInteger> primesMultiPower = multiPowerRSA.getMultiPrimeList();

        Decrypt decrypt = new Decrypt(cryptoText, primesMultiPower, multiPowerRSA.getD(), multiPowerRSA.getE());
        BigInteger decryptedText = decrypt.getDecryptedTextMultiPower();
        System.out.println("Decrypted text is: " + decryptedText + "\n");

        comparison("multi-power", "library");

    }

    private static void comparison(String typeOne, String typeTwo) {
        int NUMBER_OF_INSTANCES = 100;
        System.out.println("Comparison between " + typeOne + " algorithm and " + typeTwo + " algorithm (" + NUMBER_OF_INSTANCES + " instances)");
        System.out.println("=============================================");

        RSAComparison rsaComparison = new RSAComparison(NUMBER_OF_INSTANCES, typeOne);

        final long startLib = System.nanoTime();
        // run using BigInteger library
        rsaComparison.run(typeTwo);
        final long endLib = System.nanoTime();
        System.out.println("Using " + typeTwo + " : " + ((endLib - startLib) / 1000000) + "ms");

        final long startPower = System.nanoTime();
        // run using BigInteger library
        rsaComparison.run(typeOne);
        final long endPower = System.nanoTime();
        System.out.println("Using " + typeOne + " algorithm: " + ((endPower - startPower) / 1000000) + "ms");
        System.out.println("Compare results: " + rsaComparison.compareResults());

    }

}
