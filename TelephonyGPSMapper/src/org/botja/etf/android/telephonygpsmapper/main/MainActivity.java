package org.botja.etf.android.telephonygpsmapper.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.CellLocation;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.botja.etf.android.telephonygpsmapper.data.Data;
import org.botja.etf.android.telephonygpsmapper.data.DataPacket;
import org.botja.etf.android.telephonygpsmapper.data.Kml;
import org.botja.etf.android.telephonygpsmapper.main.R;
import org.botja.etf.android.telephonygpsmapper.sensory.IOnLocationChanged;
import org.botja.etf.android.telephonygpsmapper.sensory.IOnSignalStrengthsChanged;
import org.botja.etf.android.telephonygpsmapper.sensory.LocationSensor;
import org.botja.etf.android.telephonygpsmapper.sensory.TelephonySensor;
import org.joda.time.DateTime;

public class MainActivity extends Activity {

	private Button start, stop;

	/** The start button listener. */
	private StartButtonListener startButtonListener;

	/** The stop button listener. */
	private StopButtonListener stopButtonListener;

	private IOnLocationChanged locationChangedAction;
	private IOnSignalStrengthsChanged signalStrengthsChangedAction;

	/** This class provides access to the system location services. 
	 * These services allow applications to obtain periodic updates of the device's geographical location, 
	 * or to fire an application-specified Intent when the device enters the proximity of a given geographical location. */
	private LocationManager locationManager;
	private LocationSensor locationSensor;
	private TelephonyManager telephonyManager;
	private TelephonySensor telephonySensor;

	private int signalStrengthRSSI = 0;
	private String networkType = "";
	private String mobileProvider = "";
	private String reverseGeocodedLocation = "unknown";
	private int cellId = -1;
	private int lac = -1;
	private boolean telephonyActive = false;
	private int locationsRecorded = 0;
	private Data data;
	
	private AlertDialog.Builder saveDialog = null;
	private AlertDialog.Builder stopDialog = null;
	private EditText inputFilename = null;

	private String networkTypeIntToString(int networkType) {
		switch (networkType) {
		case 7:
			return "1xRTT";
		case 4:
			return "CDMA";
		case 2: 
			return "EDGE";
		case 14:
			return "eHRPD";
		case 5: 
			return "EVDO_0";
		case 6:
			return "EVDO_A";
		case 12:
			return "EVDO_B";
		case 1:
			return "GPRS";
		case 8:
			return "HSDPA";
		case 10:
			return "HSPA";
		case 15:
			return "HSPA+";
		case 9:
			return "HSUPA";
		case 11:
			return "iDen";
		case 13:
			return "LTE";
		case 3:
			return "UMTS";
		case 0:
		default:
			return "unknown";
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); // get ref. to loc. serv.
		locationChangedAction = new LocationChangedHandler();
		locationSensor = new LocationSensor(locationManager, LocationManager.GPS_PROVIDER);
		locationSensor.setAction(locationChangedAction);

		telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); // get ref. to tel. serv.
		signalStrengthsChangedAction = new SignalStrengthsChangedHandler();
		telephonySensor = new TelephonySensor(telephonyManager);
		telephonySensor.setAction(signalStrengthsChangedAction);
		data = new Data();

		initSaveDialog();
		initStopDialog();
		
		start = (Button)findViewById(R.id.button_start);
		stop = (Button)findViewById(R.id.button_stop);

		startButtonListener = new StartButtonListener(); 
		stopButtonListener = new StopButtonListener();        
		start.setOnClickListener(startButtonListener); 
		stop.setOnClickListener(stopButtonListener);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		locationSensor.stopTrackingLocation();
		telephonySensor.stopTrackingTelephony();
	}

	private void initStopDialog() {
		stopDialog = new AlertDialog.Builder(this);
	    stopDialog.setTitle("Are you sure you want to stop?");
	    stopDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	resetState();
		        resetUI();
		        if (data!=null) {
		        	inputFilename = new EditText(MainActivity.this);
		        	saveDialog.setView(inputFilename);
		        	long timestamp = DateTime.now().getMillis();
		        	inputFilename.setText("TelephonyGPSMapper" + String.valueOf(timestamp));
					saveDialog.show();
		        }
	        }
	    });

	    stopDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	stop.setEnabled(true);
				start.setEnabled(false);
				locationSensor.startTrackingLocation();
				telephonySensor.startTrackingTelephony();
	        	dialog.cancel();
	        }
	    });
	}

	private void initSaveDialog() {
		saveDialog = new AlertDialog.Builder(this);
	    //inputFilename = new EditText(this);
	    //saveDialog.setView(inputFilename);
	    saveDialog.setTitle("Please enter filename:");
	    saveDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            writeToSDCard(inputFilename.getText().toString().trim());
	            data = new Data();
	        }
	    });

	    saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	private class StopButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			
			start.setEnabled(true);
			stop.setEnabled(false);
			locationSensor.stopTrackingLocation();
			telephonySensor.stopTrackingTelephony();
			
			stopDialog.show();			
		}
	}

	private class StartButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			stop.setEnabled(true);
			start.setEnabled(false);
			locationSensor.startTrackingLocation();
			telephonySensor.startTrackingTelephony();
		}
	}

	private class LocationChangedHandler implements IOnLocationChanged {

		@Override
		public void OnLocationChanged(Location location) {
			TextView latitudeValue = (TextView)findViewById(R.id.latitude_value);
			TextView longitudeValue = (TextView)findViewById(R.id.longitude_value);
			TextView altitudeValue = (TextView)findViewById(R.id.altitude_value);
			TextView locationProviderAndAccuracyValue = (TextView)findViewById(R.id.location_provider_and_accuracy_value);
			TextView locationsRecordedValue = (TextView)findViewById(R.id.locations_recorded_value);

			String locationProviderAndAccuracyValueString = location.getProvider() + " / " + location.getAccuracy() + "m";

			try {
				(new ReverseGeocodingTask(MainActivity.this)).execute(new Location[] {location});	
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			locationsRecordedValue.setText(String.valueOf(++locationsRecorded));
			latitudeValue.setText(Double.toString(location.getLatitude()));
			longitudeValue.setText(Double.toString(location.getLongitude()));
			altitudeValue.setText(Double.toString(location.getAltitude()) + "m (ASL)");
			locationProviderAndAccuracyValue.setText(locationProviderAndAccuracyValueString);
			
			if (telephonyActive) {
				DataPacket dataPacket = new DataPacket(
						signalStrengthRSSI,
						networkType,
						mobileProvider,
						cellId,
						lac,
						location.getLatitude(),
						location.getLongitude(),
						location.getAltitude(),
						location.getProvider(),
						location.getAccuracy(),
						reverseGeocodedLocation
						);
				
				data.addPacket(dataPacket);
			}
		}
	}

	private class SignalStrengthsChangedHandler implements IOnSignalStrengthsChanged {

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			networkType = networkTypeIntToString(telephonyManager.getNetworkType()) + " (" + (signalStrength.isGsm()?"GSM":"CDMA") + ")";
			mobileProvider = telephonyManager.getNetworkOperatorName();

			if (signalStrength.isGsm()) {
				int asu = signalStrength.getGsmSignalStrength();
				if (asu!=99)
					signalStrengthRSSI = 2 * asu - 113;
			} else {
				signalStrengthRSSI = signalStrength.getCdmaDbm();
			}

			CellLocation cellLocation = telephonyManager.getCellLocation();
			if (cellLocation != null) {
				if (signalStrength.isGsm()) {
					GsmCellLocation gsmCellLocation = (GsmCellLocation)cellLocation;
					cellId = gsmCellLocation.getCid();
					lac = gsmCellLocation.getLac();
				} else {
					// TODO TODO TODO TODO TODO TODO				
				}
			}

			TextView signalStrengthValue = (TextView)findViewById(R.id.signal_strength_value);
			TextView networkTypeValue = (TextView)findViewById(R.id.network_type_value);
			TextView mobileProviderValue = (TextView)findViewById(R.id.mobile_provider_value);
			TextView cellIdValue = (TextView)findViewById(R.id.cell_id_value);
			TextView lacValue = (TextView)findViewById(R.id.lac_value);

			signalStrengthValue.setText(String.valueOf(signalStrengthRSSI) + " dBm");
			networkTypeValue.setText(networkType);
			mobileProviderValue.setText(mobileProvider);
			cellIdValue.setText(String.valueOf(cellId));
			lacValue.setText(String.valueOf(lac));
			
			if (!telephonyActive)
				telephonyActive = true;
		}	
	}

	// AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
	// we do not want to invoke it from the UI thread.
	/**
	 * The Class ReverseGeocodingTask.
	 */
	private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {

		/** The m context. */
		Context mContext;

		/**
		 * Instantiates a new reverse geocoding task.
		 *
		 * @param context the context
		 */
		public ReverseGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			Location loc = params[0];
			List<Address> addresses = null;
			try {
				// Call the synchronous getFromLocation() method by passing in the lat/long values.
				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
				// Update UI field with the exception.

			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Format the first line of address (if available), city, and country name.
				final String addressText = String.format("%s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getLocality(),
								address.getCountryName());

				// Update the UI
				runOnUiThread(new Runnable() {
					public void run() {
						TextView reverseGeocodedLocationValue = (TextView)findViewById(R.id.reverse_geocoded_location_value);
						reverseGeocodedLocationValue.setText(addressText);
						reverseGeocodedLocation = addressText;
					}
				});  
			}
			return null;
		}
	}
	
	/**
	 * Write to sd card.
	 */
	private void writeToSDCard(String filename) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			Toast.makeText(MainActivity.this, "Writing to KML file...", Toast.LENGTH_SHORT).show();
			
			File path = Environment.getExternalStorageDirectory();
		    File dir = new File(path.getAbsolutePath() + "/TelephonyGPSMapper");
		    
		    
		    try {
		        // Make sure the directory exists.
		        dir.mkdirs();
		        File file = new File(dir, filename + ".kml");

		        /* Write GPS data to .gpx file */
		        OutputStream os = new FileOutputStream(file);
		        os.write(Kml.ConvertDataToKml(data).getBytes());        
		        os.close();
		        Toast.makeText(MainActivity.this, "Done.", Toast.LENGTH_SHORT).show();
		        
		    } catch (IOException e) {
		        // Unable to create file, likely because external storage is
		        // not currently mounted.
		    	Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
		        Log.w("ExternalStorage", "Error writing!", e);
		    }
		}
	}
	
	private void resetUI() {
		((TextView)findViewById(R.id.signal_strength_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.network_type_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.mobile_provider_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.cell_id_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.lac_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.latitude_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.longitude_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.altitude_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.location_provider_and_accuracy_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.reverse_geocoded_location_value)).setText(R.string.unknown);
		((TextView)findViewById(R.id.locations_recorded_value)).setText("0");
	}
	
	private void resetState() {
		signalStrengthRSSI = 0;
		networkType = "";
		mobileProvider = "";
		reverseGeocodedLocation = "unknown";
		cellId = -1;
		lac = -1;
		telephonyActive = false;
		locationsRecorded = 0;
	}
}
