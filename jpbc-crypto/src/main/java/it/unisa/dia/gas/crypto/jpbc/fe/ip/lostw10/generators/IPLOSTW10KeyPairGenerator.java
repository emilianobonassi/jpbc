package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.dpvs.DPVS;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private IPLOSTW10KeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (IPLOSTW10KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        IPLOSTW10Parameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());
        Element g = parameters.getG();
        int n = parameters.getN();
        int N = 2 * n + 3;

        Element sigma = pairing.pairing(g, g);

        Element[][] dualOrthonormalBases = DPVS.sampleRandomDualOrthonormalBases(param.getRandom(), pairing, g, N);

        // B
        Element[] B = new Vector[n + 2];
        System.arraycopy(dualOrthonormalBases[0], 0, B, 0, n);
        B[n] = dualOrthonormalBases[0][N-3];
        B[n + 1] = dualOrthonormalBases[0][N-1];

        // BStart
        Element[] BStar = new Vector[n + 2];
        System.arraycopy(dualOrthonormalBases[1], 0, BStar, 0, n);
        BStar[n] = dualOrthonormalBases[1][N-3];
        BStar[n + 1] = dualOrthonormalBases[1][N-2];

        return new AsymmetricCipherKeyPair(
            new IPLOSTW10PublicKeyParameters(parameters, B, sigma),
            new IPLOSTW10MasterSecretKeyParameters(parameters, BStar)
        );
    }


}