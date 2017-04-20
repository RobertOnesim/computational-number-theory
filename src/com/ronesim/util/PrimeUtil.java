package com.ronesim.util;

/**
 * Created by ronesim on 20.04.2017.
 */
public class PrimeUtil {
    public int jacobiSymbol(long a, long n) {
        int ans = 0;
        if (a == 0) return 0;
        if (a == 1) return 1;
        long power = 0;
        while (a % 2 == 0) {
            power++;
            a = a >> 1;
        }

        if (power % 2 == 0) ans = 1;
        else
            switch ((int) (n % 8)) {
                case 1:
                    ans = 1;
                    break;
                case 3:
                    ans = -1;
                    break;
                case 5:
                    ans = -1;
                    break;
                case 7:
                    ans = 1;
                    break;
                default:
                    break;
            }
        if (n % 4 == 3 && a % 4 == 3) ans = ans * (-1);
        long n1 = n % a;
        if (a == 1) return ans;
        return ans * jacobiSymbol(n1, a);
    }

    public long expLogMod(long number, long power, long mod) {
        long ans = 1;
        for (int i = 0; (1 << i) <= power; ++i) { // all bits of power
            if (((1 << i) & power) > 0) // if byte i is 1 so we add n^(2^i) to solution
                ans = (ans * number) % mod;

            number = (number * number) % mod; // Multiply a by a to compute n^(2^(i+1))
        }
        return ans;
    }

    public boolean isPrime(long number) {
        if (number % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(number); i++)
            if (number % i == 0) return false;
        return true;
    }
}
