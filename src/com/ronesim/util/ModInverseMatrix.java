package com.ronesim.util;
/**
 * Created by ronesim on 3/11/2017.
 */

import java.math.BigInteger;
import java.util.Arrays;

public class ModInverseMatrix {
    private int row;
    private int col;
    private BigInteger[][] data;
    private BigInteger mod;

    public ModInverseMatrix(BigInteger[][] dat, BigInteger mod) {
        this.data = dat;
        this.row = dat.length;
        this.col = dat[0].length;
        this.mod = mod;
    }

    private ModInverseMatrix(int row, int col, BigInteger mod) {
        this.row = row;
        this.col = col;
        data = new BigInteger[row][col];
        this.mod = mod;
    }

    // Take the transpose of the Matrix..
    private static ModInverseMatrix transpose(ModInverseMatrix matrix) {
        ModInverseMatrix transposedMatrix = new ModInverseMatrix(matrix.getCol(), matrix.getRow(), matrix.getMod());
        for (int i = 0; i < matrix.getRow(); i++) {
            for (int j = 0; j < matrix.getCol(); j++) {
                transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
            }
        }
        return transposedMatrix;
    }

    // All operations are using Big Integers... Not Modular of anything
    private static BigInteger determinant(ModInverseMatrix matrix) {

        if (matrix.size() == 1) {
            return matrix.getValueAt(0, 0);
        }
        if (matrix.size() == 2) {
            //return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1)) - (matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
            return (matrix.getValueAt(0, 0).multiply(matrix.getValueAt(1, 1))).subtract((matrix.getValueAt(0, 1).multiply(matrix.getValueAt(1, 0))));
        }
        BigInteger sum = new BigInteger("0");
        for (int i = 0; i < matrix.getCol(); i++) {
            sum = sum.add(changeSign(i).multiply(matrix.getValueAt(0, i).multiply(determinant(createSubMatrix(matrix, 0, i)))));
        }
        return sum;
    }

    private static BigInteger changeSign(int i) {
        if (i % 2 == 0) {
            return new BigInteger("1");
        } else {
            return new BigInteger("-1");
        }
    }

    private static ModInverseMatrix createSubMatrix(ModInverseMatrix matrix, int excluding_row, int excluding_col) {
        ModInverseMatrix mat = new ModInverseMatrix(matrix.getRow() - 1, matrix.getCol() - 1, matrix.getMod());
        int r = -1;
        for (int i = 0; i < matrix.getRow(); i++) {
            if (i == excluding_row) {
                continue;
            }
            r++;
            int c = -1;
            for (int j = 0; j < matrix.getCol(); j++) {
                if (j == excluding_col) {
                    continue;
                }
                mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
            }
        }
        return mat;
    }

    public ModInverseMatrix inverse(ModInverseMatrix matrix) {
        return (transpose(cofactor(matrix)).dc(determinant(matrix)));
    }

    private ModInverseMatrix cofactor(ModInverseMatrix matrix) {
        ModInverseMatrix mat = new ModInverseMatrix(matrix.getRow(), matrix.getCol(), matrix.getMod());
        for (int i = 0; i < matrix.getRow(); i++) {
            for (int j = 0; j < matrix.getCol(); j++) {
                mat.setValueAt(i, j, (changeSign(i).multiply(changeSign(j)).multiply(determinant(createSubMatrix(matrix, i, j)))).mod(mod));
            }
        }

        return mat;
    }

    private ModInverseMatrix dc(BigInteger d) {
        BigInteger inv = d.modInverse(mod);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                data[i][j] = (data[i][j].multiply(inv)).mod(mod);
            }
        }
        return this;
    }

    public BigInteger[][] getData() {
        return data;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private BigInteger getValueAt(int i, int j) {
        return data[i][j];
    }

    private void setValueAt(int i, int j, BigInteger value) {
        data[i][j] = value;
    }

    private int size() {
        return col;
    }

    private BigInteger getMod() {
        return mod;
    }

    @Override
    public String toString() {
        return "ModInverseMatrix{" +
                "row=" + row +
                ", col=" + col +
                ", data=" + Arrays.toString(data) +
                ", mod=" + mod +
                '}';
    }
}