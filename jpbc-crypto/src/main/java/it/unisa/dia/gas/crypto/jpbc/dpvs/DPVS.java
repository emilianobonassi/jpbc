package it.unisa.dia.gas.crypto.jpbc.dpvs;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.pairing.product.ProductPairing;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DPVS {

    public static Element[][] sampleRandomDualOrthonormalBases(SecureRandom random, Pairing pairing, Element g, int N) {
        Pairing vectorPairing = new ProductPairing(random, pairing, N);

        // Generate canonical base
        Vector[] canonicalBase = new Vector[N];
        for (int i = 0; i < N; i++) {
            canonicalBase[i] = (Vector) vectorPairing.getG1().newZeroElement();
            canonicalBase[i].getAt(i).set(g);
        }

        // Sample a uniform transformation
        Element[][] linearTransformation = sampleUniformTransformation(pairing.getZr(), N);

        // Generate base B
        Element[] B = new Vector[N];
        for (int i = 0; i < N; i++) {
            B[i] = canonicalBase[0].duplicate().mulZn(linearTransformation[i][0]);
            for (int j = 1; j < N; j++) {
                B[i].add(canonicalBase[j].duplicate().mulZn(linearTransformation[i][j]));
            }
        }

        // Generate base B*
        linearTransformation = invert(ElementUtils.transpose(linearTransformation));

        Element[] BStar = new Vector[N];
        for (int i = 0; i < N; i++) {
            BStar[i] = canonicalBase[0].duplicate().mulZn(linearTransformation[i][0]);
            for (int j = 1; j < N; j++) {
                BStar[i].add(canonicalBase[j].duplicate().mulZn(linearTransformation[i][j]));
            }
        }

        return new Element[][]{B, BStar};
    }


    public static Element[][] sampleUniformTransformation(Field field, int n) {
        Element[][] matrix = new Element[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = field.newRandomElement();
            }
        }

        return matrix;
    }

    public static Element[][] invert(Element[][] matrix) {
        int n = matrix.length;

        Element[][] tempArray = new Element[n][2 * n];

        Element[][] result = new Element[n][n];

        ElementUtils.copyArray(tempArray, matrix, n, n, 0, 0);
        tempArray = invertArray(tempArray, n);
        ElementUtils.copyArray(result, tempArray, n, 2 * n, 0, n);

        return result;
    }

    public static Element[][] invertArray(Element[][] D, int n) {
        Field field = D[0][0].getField();

        // init the reduction matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j + n] = field.newZeroElement();
            }
            D[i][i + n] = field.newOneElement();
        }

        // perform the reductions
        int n2 = 2 * n;
        for (int i = 0; i < n; i++) {
            Element alpha = D[i][i].duplicate();

            if (alpha.isZero()) {
                throw new IllegalArgumentException("Singular matrix, cannot invert");
            } else {
                // normalize the vector
                for (int j = 0; j < n2; j++) {
                    D[i][j].div(alpha);
                }

                // subtract the vector from all other vectors to zero the
                // relevant matrix elements in the current column
                for (int k = 0; k < n; k++) {
                    if ((k - i) != 0) {
                        Element beta = D[k][i].duplicate();
                        for (int j = 0; j < n2; j++) {
                            D[k][j].sub(beta.duplicate().mul(D[i][j]));
                        }
                    }
                }
            }
        }
        return D;
    }

}
