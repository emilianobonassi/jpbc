package it.unisa.dia.gas.jpbc.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ComparisonBenchmarker implements Benchmarker {

    protected List<Benchmarker> benchmarkers;


    public ComparisonBenchmarker() {
        this.benchmarkers = new ArrayList<Benchmarker>();
    }

    public void addBenchmarker(Benchmarker benchmarker) {
        benchmarkers.add(benchmarker);
    }

    public Benchmark benchmark() {
        if (benchmarkers.size() <= 0)
            throw new IllegalStateException("No Benchmark set!");

        Benchmarker main = benchmarkers.get(0);
        JPBCBenchmark benchmark = (JPBCBenchmark) main.benchmark();

        for (int i = 1; i < benchmarkers.size(); i++) {
            benchmark.addBenchmark(benchmarkers.get(i).benchmark());
        }

        return benchmark;
    }

    public static void main(String[] args) {
        int iterations = Integer.parseInt(args[0]);
        String[] curves = Arrays.copyOfRange(args, 1, args.length);

        System.out.printf("JPBC Benchmark.%n");
        System.out.printf("#Iterations = %s%n", args[0]);
        for (String curve : curves) {
            System.out.printf("Curve = %s%n", curve);
        }

        ComparisonBenchmarker benchmarker = new ComparisonBenchmarker();
        benchmarker.addBenchmarker(new JPBCBenchmarker(iterations, curves, false));
        benchmarker.addBenchmarker(new JPBCBenchmarker(iterations, curves, true));
        Benchmark benchmark = benchmarker.benchmark();

        System.out.printf("Results: %n %s%n", benchmark.toHTML());
        System.out.printf("JPBC Benchmark. Finished.%n");
    }
}