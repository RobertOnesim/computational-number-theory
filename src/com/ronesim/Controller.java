package com.ronesim;

import com.ronesim.discreteLogarithm.DiscreteLogarithm;
import com.ronesim.discreteLogarithm.PrimitiveRoot;
import com.ronesim.primalityTests.PrimalityTest;
import com.ronesim.reedSolomon.Decoding;
import com.ronesim.reedSolomon.Encoding;
import com.ronesim.rsa.*;
import com.ronesim.util.RSAComparison;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by ronesim on 30.03.2017.
 */
class Controller {
    void reedSolomon() throws IOException {
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

    void multiPrimeRSA() {
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

    void multiPowerRSA() {
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

    void primalityTests(int number, String type) {
        // security parameter
        int SECURITY_PARAM = 100;

        switch (type) {
            case "solovay-strassen":
                if (number < 3 || number % 2 == 0) throw new Error("Invalid number (odd and >=3)");
                // solovayStrassen
                PrimalityTest primalityTestSolvayStrassen = new PrimalityTest(number, SECURITY_PARAM);
                String primeStatusSS = primalityTestSolvayStrassen.solovayStrassen();
                System.out.println("Solovay-Strassen result: number " + number + " is " + primeStatusSS);
                break;
            case "lucas-lehmer":
                PrimalityTest primalityTestLucasLehmer = new PrimalityTest(number);
                String primeStatusLL = primalityTestLucasLehmer.lucasLehmerMersenne();
                System.out.println("Lucas-Lehmer result: number " + primalityTestLucasLehmer.getMersenneNumber() + " is " + primeStatusLL);
                break;
            default:
                throw new Error("Invalid type");
        }


    }


    private void comparison(String typeOne, String typeTwo) {
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

    void app4() {
        // Generate primitive root modulo p prime
        // p = 2 * q + 1, q prime
        BigInteger q = BigInteger.probablePrime(32, new Random());
        //BigInteger q = new BigInteger(String.valueOf(5));
        PrimitiveRoot primitiveRoot = new PrimitiveRoot(q.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE));
        System.out.println("Prime number is: " + primitiveRoot.getMod());
        BigInteger root = primitiveRoot.generateRoot();
        System.out.println("Primitive root is: " + root);
        List<BigInteger> primeFactorization = new ArrayList<>();
        primeFactorization.add(BigInteger.valueOf(2));
        primeFactorization.add(q);
        System.out.println("Verify primitive root: " + primitiveRoot.checkPrivateRoot(root, primeFactorization));

        //DiscreteLogarithm discreteLogarithm = new DiscreteLogarithm(primitiveRoot.getMod(), root, BigInteger.valueOf(34));
        DiscreteLogarithm discreteLogarithm = new DiscreteLogarithm(BigInteger.valueOf(383), BigInteger.valueOf(2), BigInteger.valueOf(228));
        System.out.println("Discrete Logarithm value (Pollard's algorithm): " + discreteLogarithm.PollardAlgorithm(0, 0));


        HashMap<Integer, Integer> factorization = new HashMap<Integer, Integer>();
        factorization.put(2, 1);
        factorization.put(5, 3);

        BigInteger p = new BigInteger("251");
        PrimitiveRoot primitiveRoot1 = new PrimitiveRoot(p);
        root = primitiveRoot1.generateRoot(factorization);
        System.out.println("Primitive root for each p prime: " + root);
        DiscreteLogarithm pohlingHellman = new DiscreteLogarithm(p, BigInteger.valueOf(71), BigInteger.valueOf(210), factorization);
        BigInteger ans = pohlingHellman.pohligHellmanAlgorithm();
        System.out.println("Discrete Logarithm value (Pohlig-Hellman's algorithm): " + ans);
    }
}
