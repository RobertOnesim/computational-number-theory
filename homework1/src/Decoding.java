import java.math.BigInteger;
import java.util.*;

/**
 * Created by ronesim on 10.03.2017.
 */
class Decoding {
    private BigInteger prime;
    Decoding(BigInteger prime){
        this.prime = prime;
    }

    List<BigInteger> decode(List<BigInteger> encrypted, int s) {
        // generate all subsets |A| = k and compute the free coeff
        int N = encrypted.size();
        int k = N - 2 * s;
        List<Integer> superSet = new ArrayList<>();
        for (int index = 1; index <= N; index++)  superSet.add(index);
        List<Set<Integer>> finalSets = getSubsets(superSet, k);
        for (Set<Integer> set : finalSets) {
            System.out.print(set + " ");
            BigInteger fc = computeFc(set, encrypted);
            System.out.println(fc);
            // if (Objects.equals(fc, BigInteger.ZERO)) System.out.println(set);
        }
        return new ArrayList<>();
    }

    private BigInteger computeFc(Set<Integer> set, List<BigInteger> encrypted) {
        // prod elem is set
        HashMap<Integer, Integer> prodElems = new HashMap<>();
        int prodSet = 1;
        int totalProd = 1;
        for(Integer firstElem : set) {
            prodSet = prodSet * firstElem;
            int prodElem = 1;
            for (Integer secondElem : set) {
                if (!Objects.equals(firstElem, secondElem)) {
                    prodElem = prodElem * (secondElem - firstElem);
                }
            }
            totalProd *= prodElem;
            prodElems.put(firstElem, prodElem);
        }

        // compute fc using formula
        BigInteger numerator = BigInteger.ZERO;
        for(Integer firstElem : set) {
            BigInteger valueNumerator = BigInteger.valueOf(prodSet/firstElem);
            valueNumerator = valueNumerator.multiply(encrypted.get(firstElem-1));
            valueNumerator = valueNumerator.multiply(BigInteger.valueOf(totalProd/prodElems.get(firstElem)));
            numerator = numerator.add(valueNumerator).mod(prime);
        }

        return numerator.multiply(BigInteger.valueOf(totalProd).modInverse(prime)).mod(prime);
    }

    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current,List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }

    private static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        return res;
    }
}
