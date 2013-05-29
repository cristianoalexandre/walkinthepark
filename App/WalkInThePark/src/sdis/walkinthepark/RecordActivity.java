package sdis.walkinthepark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sdis.walkinthepark.types.NavPoint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RecordActivity extends Activity {

    public static final int MINIMUM_ACCEPTABLE_ACCURACY = 200;
    private long startRecordTime;
    private long finishRecordTime;
    private long lastRecordTime;
    private Thread refreshThread;
    private boolean recording = false;
    private Location lastLocation = null;
    private double distanceElapsed;
    private ArrayList<NavPoint> navpoints;
    private GoogleMap mMap = null;
    private Marker currentPositionMarker;
    private ArrayList<Polyline> walkLines = new ArrayList<Polyline>();
    private ViewFlipper flipper;

    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_record);

	flipper = (ViewFlipper) findViewById(R.id.flipper);
	Button btn = (Button) findViewById(R.id.flip_me);

	btn.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View view) {
		flipper.showNext();
	    }
	});
	
	setUpMapIfNeeded();

	// Acquire a reference to the system Location Manager
	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
		// Called when a new location is found by the provider.
		final Location currentLocation = location;

		new Thread() {
		    @Override
		    public void run() {
			if (lastLocation != null) {
			    if (recording && currentLocation.getAccuracy() < MINIMUM_ACCEPTABLE_ACCURACY) {
				distanceElapsed += currentLocation.getSpeed();

				navpoints.add(new NavPoint(currentLocation));
				System.out.println("=================");
				System.out.println(navpoints.get(navpoints.size() - 1));
				System.out.println("Distance elapsed: " + currentLocation.getSpeed());
				System.out.println("Accuracy: " + currentLocation.getAccuracy());
			    } else if (recording) {
				System.out.println("+++++ Unacceptable Accuracy! -> " + currentLocation.getAccuracy()
					+ " +++++");
			    }
			} else {
			    lastLocation = currentLocation;
			}
		    }
		}.start();

		lastRecordTime = System.currentTimeMillis();
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }

	    public void onProviderEnabled(String provider) {
	    }

	    public void onProviderDisabled(String provider) {
	    }
	};

	// Register the listener with the Location Manager to receive location
	// updates
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
	    currentSpeedView.setText(String.format("%.1f",
		    (navpoints.get(navpoints.size() - 1).getInstantSpeed() * 3600) / 1000) + " km/h");
	}
    }

    private class RefreshThread extends Thread {

	@Override
	public void run() {
	    for (;;) {
		runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
			showTimeElapsed();
			showDistanceElapsed();
			showCurrentSpeed();
			if (navpoints.isEmpty() == false && mMap != null) {
			    System.out.println("Adding marker");
			    NavPoint nv = navpoints.get(navpoints.size() - 1);
			    if (currentPositionMarker == null) {
				currentPositionMarker = mMap.addMarker(new MarkerOptions().position(
					new LatLng(nv.getLatitude(), nv.getLongitude())).title("Marker"));
			    } else {
				currentPositionMarker.setPosition(new LatLng(nv.getLatitude(), nv.getLongitude()));
			    }

			    if (navpoints.size() >= 2) {
				NavPoint nv2 = navpoints.get(navpoints.size() - 2);
				walkLines.add(mMap.addPolyline(new PolylineOptions().add(new LatLng(nv2.getLatitude(),
					nv2.getLongitude()), new LatLng(nv.getLatitude(), nv.getLongitude()))));
			    }
			    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				    new LatLng(nv.getLatitude(), nv.getLongitude()), 15));
			}
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

    private void setUpMapIfNeeded() {
	// Do a null check to confirm that we have not already instantiated the
	// map.
	if (mMap == null) {
	    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    getFragmentManager().executePendingTransactions();
	    // Check if we were successful in obtaining the map.
	    if (mMap != null) {
		// The Map is verified. It is now safe to manipulate the map.

	    }
	}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
}