package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import java.io.*;
import java.util.Random;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEAttributes {

    public static byte[] attributesToByteArray(HVEIP08Parameters parameters, int[] attributes) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(result);

        try {
            for (int i = 0; i < parameters.getN(); i++) {
                switch (parameters.getAttributeLengthInBytesAt(i)) {
                    case 1:
                        os.writeByte((byte) attributes[i]);
                        break;
                    case 2:
                        os.writeShort((short) attributes[i]);
                        break;
                    case 3:
                    case 4:
                        os.writeInt(attributes[i]);
                        break;
                    default:
                        throw new IllegalArgumentException("bytes length per attribute cannot be larger than 4.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toByteArray();
    }

    public static int[] byteArrayToAttributes(HVEIP08Parameters parameters, byte[] buffer) {
        DataInputStream is = new DataInputStream(new ByteArrayInputStream(buffer));

        try {
            int[] result = new int[parameters.getN()];
            for (int i = 0; i < result.length; i++) {
                switch (parameters.getAttributeLengthInBytesAt(i)) {
                    case 1:
                        result[i] = is.readUnsignedByte();
                        break;
                    case 2:
                        result[i] = is.readUnsignedShort();
                        break;
                    case 4:
                        result[i] = is.readInt();
                        break;
                    default:
                        throw new IllegalArgumentException("bytes length per attribute cannot be larger than 4.");
                }
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] randomBinaryAttributes(Random random, int n) {
        int[] attrs = new int[n];

        for (int i = 0; i < attrs.length; i++)
            attrs[i] = random.nextBoolean() ? 1 : 0;

        return attrs;
    }
}
