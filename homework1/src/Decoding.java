import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ronesim on 10.03.2017.
 */
class Decoding {
    private BigInteger prime;

    Decoding(BigInteger prime) {
        this.prime = prime;
    }

    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
    }

    private static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<>(), res);
        return res;
    }

    List<BigInteger> decode(List<BigInteger> encrypted, int s) {
        List<BigInteger> decrypted = new ArrayList<>();
        // generate all subsets |A| = k and compute the free coeff
        int N = encrypted.size();
        int k = N - 2 * s;
        List<Integer> superSet = new ArrayList<>();
        for (int index = 1; index <= N; index++) superSet.add(index);
        List<Set<Integer>> finalSets = getSubsets(superSet, k);
        for (Set<Integer> set : finalSets) {
            BigInteger fc = computeFc(set, encrypted);
            BigInteger fck = computeFcKInversions(set, encrypted);
            System.out.println(set + " " + fc + " " + fck);
            if (Objects.equals(fc, BigInteger.ZERO)) { // found fc = 0 => start rebuild message
                System.out.println("Found fc = 0: " + set);
                decrypted = rebuildMessage(set, encrypted);
                break;
            }
        }
        return decrypted;
    }


    /* Ax = b where:
       A(matrix) = Vandermonde type matrix ex [[1,1,1],[1,2,4],[1,3,9]]
       x = vector of coeff
       b = resultVector from encrypted message
     */
    private List<BigInteger> rebuildMessage(Set<Integer> set, List<BigInteger> encrypted) {
        List<BigInteger> decrypted = new ArrayList<>();
        // build Vandermonde matrix (matrix)
        BigInteger[][] matrix = new BigInteger[set.size()][set.size()];
        for (int row = 0; row < set.size(); row++) matrix[row][0] = BigInteger.valueOf(1);
        int row = 0;
        for (Integer number : set) {
            matrix[row][1] = BigInteger.valueOf(number);
            row++;
        }
        for (row = 0; row < set.size(); row++)
            for (int col = 2; col < set.size(); col++)
                matrix[row][col] = matrix[row][col - 1].multiply(matrix[row][1]);

        // find A^-1
        ModInverseMatrix obj2 = new ModInverseMatrix(matrix, prime);
        ModInverseMatrix inverse2 = obj2.inverse(obj2);
        List<BigInteger> solVector = getSolVector(set, encrypted);

        // solve system A^-1 * b
        for (int i = 0; i < inverse2.getRow(); i++) {
            BigInteger coeff = BigInteger.ZERO;
            for (int j = 0; j < inverse2.getCol(); j++)
                coeff = coeff.add(inverse2.getData()[i][j].multiply(solVector.get(j)).mod(prime)).mod(prime);
            decrypted.add(coeff);
        }
        return decrypted;
    }

    private List<BigInteger> getSolVector(Set<Integer> set, List<BigInteger> encrypted) {
        return set.stream().map(number -> encrypted.get(number - 1)).collect(Collectors.toList());
    }

    private BigInteger computeFc(Set<Integer> set, List<BigInteger> encrypted) {
        // prod elem is set
        HashMap<Integer, BigInteger> prodElems = new HashMap<>();
        BigInteger prodSet = BigInteger.ONE;
        BigInteger totalProd = BigInteger.ONE;
        for (Integer firstElem : set) {
            prodSet = prodSet.multiply(BigInteger.valueOf(firstElem));
            BigInteger prodElem = BigInteger.ONE;
            for (Integer secondElem : set) {
                if (!Objects.equals(firstElem, secondElem)) {
                    prodElem = prodElem.multiply(BigInteger.valueOf(secondElem - firstElem));
                }
            }
            totalProd = totalProd.multiply(prodElem);
            prodElems.put(firstElem, prodElem);
        }

        // compute fc using formula
        BigInteger numerator = BigInteger.ZERO;
        for (Integer firstElem : set) {
            BigInteger valueNumerator = prodSet.divide(BigInteger.valueOf(firstElem)).mod(prime);
            valueNumerator = valueNumerator.multiply(encrypted.get(firstElem - 1)).mod(prime);
            valueNumerator = valueNumerator.multiply(totalProd.divide(prodElems.get(firstElem))).mod(prime);
            numerator = numerator.add(valueNumerator).mod(prime);
        }
        totalProd = totalProd.modInverse(prime);
        return numerator.multiply(totalProd).mod(prime);
    }

    private BigInteger computeFcKInversions(Set<Integer> set, List<BigInteger> encrypted) {
        BigInteger ans = BigInteger.ZERO;
        for (Integer firstElem : set) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (Integer secondElem : set) {
                if (!Objects.equals(secondElem, firstElem)) {
                    numerator = numerator.multiply(BigInteger.valueOf(secondElem).mod(prime)).mod(prime);
                    denominator = denominator.multiply(BigInteger.valueOf(secondElem - firstElem).mod(prime)).mod(prime);
                }
            }
            numerator = numerator.multiply(encrypted.get(firstElem - 1)).mod(prime);
            denominator = denominator.modInverse(prime);
            ans = ans.add(numerator.multiply(denominator).mod(prime)).mod(prime);
        }
        return ans;
    }
}
