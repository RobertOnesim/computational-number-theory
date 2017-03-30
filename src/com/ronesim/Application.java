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
        Controller controller = new Controller();
        switch (args[0]) {
            case "reed-solomon":
                controller.reedSolomon();
                break;
            case "multiPrimeRSA":
                controller.multiPrimeRSA();
                break;
            case "multiPowerRSA":
                controller.multiPowerRSA();
                break;
            default:
                System.out.println("Incorrect application specified.");
        }
    }

}
