package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13ReKeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13RecodeParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13RecKeyPairGenerator implements CipherParametersGenerator {
    private WTORGVW13ReKeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (WTORGVW13ReKeyPairGenerationParameters) keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        Element rk = params.getTargetPk().getRight().mul(
                params.getRightPk().getRight()
        ).powZn(params.getLeftSk().getT()).getImmutable();

        return new WTORGVW13RecodeParameters(params.getParameters(), rk);
    }


}
