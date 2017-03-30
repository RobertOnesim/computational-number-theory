package com.ronesim.rsa;

import java.math.BigInteger;

/**
 * Created by ronesim on 29.03.2017.
 */

/**
 * RSA encryption message ^ e mod N
 */
public class Encrypt {
    public BigInteger encrypt(BigInteger message, BigInteger n, BigInteger e) {
        return message.modPow(e, n);
    }
}
