package it.unisa.dia.gas.jpbc.android.benchmark;

/**
 * Angelo De Caro (jpbclib@gmail.com)
 */
public class Benchmark {
    protected static final String[] pairingBenchmarkNames = new String[]{
            "Pairing#pairing(in1, in2)",
            "Pairing#getPairingPreProcessingFromElement(in1)",
            "PairingPreProcessing#pairing(in2)",
    };

    protected static final String[] elementBenchmarkNames = new String[]{
            "Element#pow(BigInteger)",
            "Element#powZn(Element)",
            "Element#getElementPowPreProcessing()",
            "ElementPowPreProcessing#pow(BigInteger)",
            "ElementPowPreProcessing#powZn(Element)",
            "Element#mul(BigInteger)",
            "Element#setToRandom()"
    };

    protected static final String[] fieldNames = new String[]{
            "G1", "G2", "GT", "Zr"
    };

    private String[] curves;
    private double[][] pairingBenchmarks;
    private double[][][] elementBenchmarks;


    public Benchmark(String[] curves) {
        this.curves = curves;
        this.pairingBenchmarks = new double[3 + (5 * 4)][curves.length];
        this.elementBenchmarks = new double[fieldNames.length][elementBenchmarkNames.length][curves.length];
    }


    public double[][] getPairingBenchmarks() {
        return pairingBenchmarks;
    }

    public double[][][] getElementBenchmarks() {
        return elementBenchmarks;
    }


    public String toHTML(Benchmark... benchmarks) {
        StringBuffer buffer = new StringBuffer();

        buffer  .append("                <table>\n")
                .append("                    <tr>\n")
                .append("                        <th>Benchmark - Average Time (ms)</th>\n")
                .append("                        <th>Pairing Type</th>\n")
                .append("                    </tr>\n")
                .append("                    <tr>\n")
                .append("                        <th></th>\n");
        for (String curve : curves) {
            curve = curve.substring(curve.lastIndexOf('/')+1, curve.lastIndexOf('.'));

            buffer.append("                        <th><font style=\"font-weight: bold;color:green\">").append(curve).append("</font></th>\n");
        }
        buffer.append("                    </tr>\n");

        for (int row = 0; row < pairingBenchmarkNames.length; row++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">").append(pairingBenchmarkNames[row]).append("</font></th>\n");
            for (int col = 0; col < curves.length; col++) {
                buffer.append("                        <td>").append(pairingBenchmarks[row][col]);

                if (benchmarks != null) {
                    buffer.append(" (");
                    for (int i = 0; i < benchmarks.length; i++) {
                        Benchmark benchmark = benchmarks[i];
                        buffer.append(benchmark.getPairingBenchmarks()[row][col]);
                        if (i+1 < benchmarks.length)
                            buffer.append(", ");
                    }
                    buffer.append(")");
                }

                buffer.append("</td>\n");
            }
            buffer.append("                    </tr>\n");
        }

        for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\">\n")
                    .append("                            <font style=\"font-weight: bold;color:black\">Element Pow (").append(fieldNames[fieldIndex]).append(")</font>\n")
                    .append("                        </th>\n");
            for (int col = 0; col < curves.length; col++) {
                buffer.append("                        <td></td>\n");
            }
            buffer.append("                    </tr>\n");

            for (int row = 0; row < elementBenchmarkNames.length; row++) {
                buffer.append("                    <tr>\n")
                        .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">")
                        .append(elementBenchmarkNames[row])
                        .append("</font></th>\n");
                for (int col = 0; col < curves.length; col++) {
                    buffer.append("                        <td>").append(elementBenchmarks[fieldIndex][row][col]);

                    if (benchmarks != null) {
                        buffer.append(" (");
                        for (int i = 0; i < benchmarks.length; i++) {
                            Benchmark benchmark = benchmarks[i];
                            buffer.append(benchmark.getElementBenchmarks()[fieldIndex][row][col]);
                            if (i+1 < benchmarks.length)
                                buffer.append(", ");
                        }
                        buffer.append(")");
                    }

                    buffer.append("</td>\n");
                }
                buffer.append("                    </tr>\n");
            }
        }

        buffer.append("                </table>\n");

        return buffer.toString();
    }
}
