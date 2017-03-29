package com.ronesim;

import com.ronesim.reedSolomon.Decoding;
import com.ronesim.reedSolomon.Encoding;
import com.ronesim.rsa.Decrypt;
import com.ronesim.rsa.Encrypt;
import com.ronesim.rsa.RSA;
import com.ronesim.util.RSAHelper;

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
            case "RSA":
                multiPrimeRSA();
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
        RSA rsa = new RSA(message);
        BigInteger cryptoText = new Encrypt().encrypt(message, rsa.getMultiPrimeN(), rsa.getE());
        System.out.println(rsa.toString());
        System.out.println("Encrypted text is: " + cryptoText);

        // Start decryption
        List<BigInteger> primes = rsa.getPrimeList();

        Decrypt decrypt = new Decrypt(cryptoText, primes, rsa.getD());
        BigInteger decryptedText = decrypt.getDecryptedTextGarner();

        System.out.println("Decrypted text is: " + decryptedText + "\n");

        // Compare time between Garner's algorithm and java BigInteger library computation
        int NUMBER_OF_INSTANCES = 100;
        System.out.println("Comparison between Garner's algorithm and BigInteger library (" + NUMBER_OF_INSTANCES + " instances)");
        System.out.println("=============================================");

        RSAHelper rsaHelper = new RSAHelper(NUMBER_OF_INSTANCES);

        final long startLib = System.nanoTime();
        // run using BigInteger library
        rsaHelper.run("library");
        final long endLib = System.nanoTime();
        System.out.println("Using Library: " + ((endLib - startLib) / 1000000) + "ms");

        final long startGarner = System.nanoTime();
        // run using BigInteger library
        rsaHelper.run("garner");
        final long endGarner = System.nanoTime();
        System.out.println("Using Garner's Algorithm: " + ((endGarner - startGarner) / 1000000) + "ms");

    }

}
