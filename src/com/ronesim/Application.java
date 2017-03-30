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
