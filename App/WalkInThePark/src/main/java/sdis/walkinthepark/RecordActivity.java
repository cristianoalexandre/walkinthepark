package sdis.walkinthepark;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import sdis.walkinthepark.types.NavPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordActivity extends Activity {

    public static int MINIMUM_ACCEPTABLE_ACCURACY = 150;
    private long startRecordTime;
    private long finishRecordTime;
    private long lastRecordTime;
    private Thread refreshThread;
    private boolean recording = false;
    private Location lastLocation = null;
    private double distanceElapsed;
    private ArrayList<NavPoint> navpoints;
    private int locationCounter = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                // Called when a new location is found by the network location provider.

                if (lastLocation != null) {
                    if (recording && location.getAccuracy() < MINIMUM_ACCEPTABLE_ACCURACY) {
                        distanceElapsed += location.getSpeed(); /*/ (System.currentTimeMillis() / 1000 - lastRecordTime / 1000);*/
                        navpoints.add(new NavPoint(location));
                        System.out.println("=================");
                        System.out.println(navpoints.get(navpoints.size() - 1));
                        System.out.println("Distance elapsed: " + location.getSpeed());/* / (System.currentTimeMillis() / 1000 - lastRecordTime / 1000));*/
                        System.out.println("Accuracy: " + location.getAccuracy());
                    } else if (recording) {
                        System.out.println("+++++ Unacceptable Accuracy! -> " + location.getAccuracy() + " +++++");
                    }

                } else {
                    lastLocation = location;
                }

                lastRecordTime = System.currentTimeMillis();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public void toggleRecord(View v) {
        Button b = (Button) findViewById(R.id.record_button);

        if (recording) {
            finishRecord();
            b.setText("Record");
        } else {
            startRecord();
            b.setText("Finish");
        }
    }

    public void startRecord() {
        navpoints = new ArrayList<NavPoint>();
        distanceElapsed = 0;
        lastLocation = null;
        startRecordTime = System.currentTimeMillis();
        lastRecordTime = startRecordTime;
        recording = true;
        refreshThread = new RefreshThread();
        refreshThread.start();
    }

    public void finishRecord() {
        finishRecordTime = System.currentTimeMillis();
        recording = false;
    }

    public long getTimeElapsed() {
        return System.currentTimeMillis() - startRecordTime;
    }

    public void showTimeElapsed() {
        TextView timeElapsedView = (TextView) findViewById(R.id.time_elapsed);

        Date date = new Date(getTimeElapsed());
        DateFormat formatter = new SimpleDateFormat("mm:ss");

        timeElapsedView.setText(formatter.format(date));
    }

    public void showDistanceElapsed() {
        TextView distanceElapsedView = (TextView) findViewById(R.id.distance_elapsed);

        distanceElapsedView.setText(String.format("%.1f", distanceElapsed / 1000) + " km");
    }

    public void showCurrentSpeed() {
        if (!navpoints.isEmpty()) {
            TextView currentSpeedView = (TextView) findViewById(R.id.current_speed);

            currentSpeedView.setText(String.format("%.1f", (navpoints.get(navpoints.size() - 1).getInstantSpeed() * 3600) / 1000) + " km/h");
        }
    }

    private class RefreshThread extends Thread {

        @Override
        public void run() {
            for (; ; ) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showTimeElapsed();
                        showDistanceElapsed();
                        showCurrentSpeed();
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!recording)
                    break;
            }
        }
    }
}