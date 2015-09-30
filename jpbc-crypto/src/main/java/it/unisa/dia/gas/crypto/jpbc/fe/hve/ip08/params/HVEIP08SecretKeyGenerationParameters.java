package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08SecretKeyGenerationParameters extends KeyGenerationParameters {
    private HVEIP08MasterSecretKeyParameters masterSecretKey;
    private int[] pattern;
    private boolean allStar;
    private int numNonStar;


    public HVEIP08SecretKeyGenerationParameters(HVEIP08MasterSecretKeyParameters masterSecretKey, int... pattern) {
        super(null, masterSecretKey.getParameters().getG().getField().getLengthInBytes());

        this.masterSecretKey = masterSecretKey;
        this.pattern = Arrays.copyOf(pattern, pattern.length);

        int numStar = 0;
        for (int i = 0; i < pattern.length; i++) {
            int patter = pattern[i];

            if (patter < 0)
                numStar++;
        }
        this.numNonStar = pattern.length - numStar;
        this.allStar = (numStar == pattern.length);
    }

    public HVEIP08MasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public int[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }

    public boolean isAllStar() {
        return allStar;
    }

    public boolean isStarAt(int index) {
        return pattern[index] < 0;
    }

    public int getPatternAt(int index) {
        return pattern[index];
    }

    public int getNumNonStar() {
        return numNonStar;
    }
}