package it.unisa.dia.gas.jpbc.android.benchmark;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class JPBCBenchmarkActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "JPBCBenchmarkActivity";

    protected AndroidBenchmark androidBenchmark;
    protected boolean running = false;

    protected BroadcastReceiver batteryLevelReceiver;
    protected int maxBattery = Integer.MIN_VALUE, minBattery = Integer.MAX_VALUE;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        // Init UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((TextView) findViewById(R.id.status)).setText("");
        findViewById(R.id.benchmark).setOnClickListener(this);

        // Init the rest
        initBenchmark();
//        initBatteryMonitor();

        Log.i(TAG, "onCreate.finished");
    }

    protected PairingParameters getParameters(String curve) {
        return PairingFactory.getInstance().loadParameters(curve);
    }

    protected void onStop() {
//        unregisterReceiver(batteryLevelReceiver);

        super.onStop();
    }

    public void onClick(View view) {
        Log.i(TAG, "onClick");

        if (running) {
            ((TextView) findViewById(R.id.status)).setText("Stopping...");

            stopBenchmark();
        } else {
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.status)).setText("Benchmarking...");
            ((Button) findViewById(R.id.benchmark)).setText("Stop");

            androidBenchmark.setIterations(
                    Integer.valueOf(((EditText) findViewById(R.id.iterations)).getText().toString())
            );
            benchmark();
        }

        Log.i(TAG, "onClick.finished");
    }


    protected void initBenchmark() {
        this.androidBenchmark = new AndroidBenchmark(10);
    }

    protected void benchmark() {
        running = true;
        Thread t = new Thread() {
            public void run() {
                Benchmark benchmark = androidBenchmark.benchmark(new String[]{
                        "assets/a.properties",
                        "assets/d159.properties",
                        "assets/d201.properties",
                        "assets/d224.properties"
                });

                //Send update to the main thread
                messageHandler.sendMessage(Message.obtain(messageHandler, (benchmark != null) ? 0 : 1, benchmark));
            }
        };
        t.start();
    }

    protected void stopBenchmark() {
        androidBenchmark.stop();
        running = false;
    }

    protected void initBatteryMonitor() {
        batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);

                if (rawlevel >= 0 && scale > 0) {
                    int level = (rawlevel * 100) / scale;
//                batterLevel.setText("Battery Level Remaining: " + level + "%");
                    Log.i(TAG, "Battery Level Remaining: " + level + "%");

                    if (level > maxBattery)
                        maxBattery = level;
                    if (level < minBattery)
                        minBattery = level;
                }

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }


    // Instantiating the Handler associated with the main thread.
    private Handler messageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ((TextView) findViewById(R.id.status)).setText("Benchmark Completed!");
                    ((Button) findViewById(R.id.benchmark)).setText("Benchmark");
                    findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    ((TextView) findViewById(R.id.status)).setText("Benchmark Stopped!");
                    ((Button) findViewById(R.id.benchmark)).setText("Benchmark");
                    findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                    break;
                default:
                    throw new IllegalStateException("Invalid state!!!");
            }

            // Store anyway the benchmark even if they are partial.

            // Store benchmark output
            PrintStream out = null;
            try {
                String state = Environment.getExternalStorageState();
                String where;

                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // Store data in the external storage
                    where = "(out)";
                    File file = new File(Environment.getExternalStorageDirectory(), "benchmark.out");
                    out = new PrintStream(new FileOutputStream(file));
                } else {
                    // Store date in the internal storage
                    where = "(in)";
                    out = new PrintStream(openFileOutput("benchmark.out", Context.MODE_WORLD_READABLE));
                }

                // Write batteries level

                out.print(((Benchmark) msg.obj).toHTML());
                out.flush();

                ((TextView) findViewById(R.id.status)).setText("Benchmark Stored! " + where);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);

                ((TextView) findViewById(R.id.status)).setText("Failed to store Benchmark!");
            } finally {
                if (out != null)
                    out.close();
            }

        }

    };
}
