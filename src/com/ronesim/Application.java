package com.ronesim;

import java.io.IOException;

/**
 * Created by ronesim on 09.03.2017.
 */
public class Application {

    public static void main(String args[]) throws IOException {
        /* check what app to run
            1. reed-solomon
            2. RSA
            3. primality: type(solovay-strassen or lucas-lehmer)
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
            case "primality":
                controller.primalityTests(Integer.valueOf(args[1]), args[2]);
                break;
            default:
                System.out.println("Incorrect application specified.");
        }
    }

}
