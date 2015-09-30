package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MapParameters;
import it.unisa.dia.gas.plaf.jpbc.util.collection.LatchHashMap;
import it.unisa.dia.gas.plaf.jpbc.util.io.disk.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMMapParameters extends MapParameters {

    protected CTL13MMSystemParameters parameters;
    protected Disk<ArraySector<BigInteger>> disk;


    public CTL13MMMapParameters(CTL13MMSystemParameters parameters) {
        this(new LatchHashMap<String, Object>(), parameters);
    }

    public CTL13MMMapParameters(Map<String, Object> values, CTL13MMSystemParameters parameters) {
        super(values);

        this.parameters = parameters;
        putObject("params", parameters);
    }

    public CTL13MMMapParameters(PairingParameters parameters) {
        this(new LatchHashMap<String, Object>(), new CTL13MMSystemParameters(parameters));
    }


    public BigInteger getBigInteger(String key) {
        return disk.getSector("header").getAt(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        return disk.getSector(key).getAt(index);
    }

    public void putBigInteger(String key, BigInteger value) {
        disk.getSector("header").setAt(key, value);
    }

    public void putBigIntegerAt(String key, int index, BigInteger value) {
        disk.getSector(key).setAt(index, value);
    }


    public void init() {
        try {
            int x0Length = (parameters.getEta() * parameters.getN() + 7) / 8;
            int gLength = (parameters.getAlpha() + 7) / 8;
            int pLength = (parameters.getEta() + 7) / 8;

            FileChannelDisk<ArraySector<BigInteger>> fileChannelDisk = new FileChannelDisk<ArraySector<BigInteger>>();
            fileChannelDisk
                    .addSector("header", new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, 5, "x0", "y", "pzt", "z", "zInv"))
                    .addSector("xsp", new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, parameters.getEll()))
                    .addSector("crtCoefficients", new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, parameters.getN()));

            for (int level = 1; level <= parameters.getKappa(); level++)
                fileChannelDisk.addSector("xs" + level, new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, parameters.getDelta() * 2));

            fileChannelDisk.addSector("gs", new ByteBufferLatchSoftRefBigIntegerArraySector(gLength, parameters.getN()))
                    .addSector("ps", new ByteBufferLatchSoftRefBigIntegerArraySector(pLength, parameters.getN()))
                    .addSector("zInvPow", new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, parameters.getKappa()))
                    .addSector("yPow", new ByteBufferLatchSoftRefBigIntegerArraySector(x0Length, parameters.getKappa() + 1))
                    .mapTo(toFileName());

            this.disk = fileChannelDisk;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void store() {
        try {
            disk.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load() {
        return load(toFileName());
    }

    public boolean load(String path) {
        try {
            if (!new File(path).exists())
                return false;

            int x0Length = (parameters.getEta() * parameters.getN() + 7) / 8;
            int gLength = (parameters.getAlpha() + 7) / 8;
            int pLength = (parameters.getEta() + 7) / 8;

            FileChannelDisk<ArraySector<BigInteger>> fileChannelDisk = new FileChannelDisk<ArraySector<BigInteger>>();
            fileChannelDisk.addSector("header", new ByteBufferSoftRefBigIntegerArraySector(x0Length, 5, "x0", "y", "pzt", "z", "zInv"))
                    .addSector("xsp", new ByteBufferSoftRefBigIntegerArraySector(x0Length, parameters.getEll()))
                    .addSector("crtCoefficients", new ByteBufferSoftRefBigIntegerArraySector(x0Length, parameters.getN()));

            for (int level = 1; level <= parameters.getKappa(); level++)
                fileChannelDisk.addSector("xs" + level, new ByteBufferSoftRefBigIntegerArraySector(x0Length, parameters.getDelta() * 2));

            fileChannelDisk.addSector("gs", new ByteBufferSoftRefBigIntegerArraySector(gLength, parameters.getN()))
                    .addSector("ps", new ByteBufferSoftRefBigIntegerArraySector(pLength, parameters.getN()))
                    .addSector("zInvPow", new ByteBufferSoftRefBigIntegerArraySector(x0Length, parameters.getKappa()))
                    .addSector("yPow", new ByteBufferSoftRefBigIntegerArraySector(x0Length, parameters.getKappa() + 1))
                    .mapTo(new FileInputStream(path).getChannel());

            putObject("params", parameters);
            putObject("x0", fileChannelDisk.getSector("header").getAt(0));
            putObject("y", fileChannelDisk.getSector("header").getAt(1));
            putObject("pzt", fileChannelDisk.getSector("header").getAt(2));
            putObject("z", fileChannelDisk.getSector("header").getAt(3));
            putObject("zInv", fileChannelDisk.getSector("header").getAt(4));

            this.disk = fileChannelDisk;

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String toFileName() {
        return String.format(
                "CTL13MM_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                parameters.getRho(), parameters.getDelta(),
                parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound());
    }
}