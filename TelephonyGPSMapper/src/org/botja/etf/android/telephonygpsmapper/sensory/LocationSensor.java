package org.botja.etf.android.telephonygpsmapper.sensory;
import android.location.*;
import android.os.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationSensor.
 */
public class LocationSensor {
	// Constants used in the class:
	/** The Constant MIN_UPDATE_TIME. */
	private static final long MIN_UPDATE_TIME = 10000; // 10 seconds
	
	/** The Constant MIN_UPDATE_DISTANCE. */
	private static final float MIN_UPDATE_DISTANCE = 10.0f; // 10 meters
	
	/** The Constant TWO_MINUTES. */
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	public static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;
	public static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;
	public static final String GPS_AND_NETWORK_PROVIDER = "gps+network";
		
	/** The location manager acquired from Android OS at runtime. */
	private LocationManager locationManager = null; // set in construction time
	
	/** Action to perform when location is updated.
	 *  Needs to be set manually via set method. */
	private IOnLocationChanged action = null;
	
	/** Receive location updates from NETWORK_PROVIDER and/or GPS_PROVIDER. */
	private String locationProvider = LocationSensor.GPS_AND_NETWORK_PROVIDER; // default
	
	/** Minimum time interval between location updates, in milliseconds. */
	private long minUpdateTime = MIN_UPDATE_TIME; // default
	
	/** Minimum distance between location updates, in meters. */
	private float minUpdateDistance = MIN_UPDATE_DISTANCE; // default

	/** The current best location used to filter low accurate fixes. */
	private Location currentBestLocation = null;
	
	private boolean tracking = false;
	
	
	/** Add custom actions when new location is acquired. */
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			if (isBetterLocation(location, getCurrentBestLocation())) {
				setCurrentBestLocation(location);
				if (action != null)
					action.OnLocationChanged(location);	
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}	
	};
	
	/**
	 * Instantiates a new LocationSensor used to handle location updates.
	 *
	 * @param locationManager the location manager acquired from Android OS at runtime.
	 * @param locationProvider the name of the provider with which to register
	 * @param minTime minimum time interval between location updates, in milliseconds
	 * @param minDistance minimum distance between location updates, in meters
	 */
	public LocationSensor(LocationManager locationManager, String locationProvider, long minTime, float minDistance) {
		setLocationManager(locationManager);
		setLocationProvider(locationProvider);
		setMinUpdateTime(minTime);
		setMinUpdateDistance(minDistance);
	}
	
	/**
	 * Instantiates a new LocationSensor used to handle location updates.
	 * Default minimum time between updates (10 seconds).
	 * Default minimum distance between updates (10meters).
	 *
	 * @param locationManager the location manager acquired from Android OS at runtime.
	 * @param locationProvider the name of the provider with which to register
	 */
	public LocationSensor(LocationManager locationManager, String locationProvider) {
		setLocationManager(locationManager);
		setLocationProvider(locationProvider);
	}
	
	/**
	 * Instantiates a new LocationSensor used to handle location updates.
	 * Default location provider (GPS provider).
	 * Default minimum time between updates (10 seconds).
	 * Default minimum distance between updates (10meters).
	 * 
	 * @param locationManager the location manager acquired from Android OS at runtime.
	 */
	public LocationSensor(LocationManager locationManager) {
		setLocationManager(locationManager);
	}
	
	/**
	 * Start tracking location using provided Location Provider.
	 */
	public void startTrackingLocation() {
		if (!isTracking()) {
			if (isSameProvider(getLocationProvider(), LocationManager.GPS_PROVIDER)) {
				getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTime, minUpdateDistance, locationListener);
				setTracking(true);
			}
			
			if (isSameProvider(getLocationProvider(), LocationManager.NETWORK_PROVIDER)) {
				getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minUpdateTime, minUpdateDistance, locationListener);
				setTracking(true);
			}
			
			if (isSameProvider(getLocationProvider(), LocationSensor.GPS_AND_NETWORK_PROVIDER)) {
				getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTime, minUpdateDistance, locationListener);
				getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minUpdateTime, minUpdateDistance, locationListener);
				setTracking(true);
			}
		}	
	}
		
	
	/**
	 * Stop tracking location and unsubscribe from location changed events.
	 */
	public void stopTrackingLocation() {
		if (isTracking()) {
			getLocationManager().removeUpdates(locationListener);
			setTracking(false);
		}
	}

	/**
	 * Gets the Location Manager (if any) acquired before from Android OS at runtime.
	 *
	 * @return the Location Manager
	 */
	public LocationManager getLocationManager() {
		return locationManager;
	}

	/**
	 * Sets the Location Manager acquired from Android OS at runtime.
	 *
	 * @param locationManager the new Location Manager
	 */
	private void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	/**
	 * Gets the Location Provider ("gps" or "network").
	 *
	 * @return the Location Provider
	 */
	public String getLocationProvider() {
		return locationProvider;
	}

	/**
	 * Sets the Location Provider ("gps" for GPS, "network" for Network provider)
	 *
	 * @param locationProvider the new Location Provider
	 */
	public void setLocationProvider(String locationProvider) {
		this.locationProvider = locationProvider;
	}

	/**
	 * Gets the min update time between location updates (in milliseconds).
	 *
	 * @return the min update time
	 */
	public long getMinUpdateTime() {
		return minUpdateTime;
	}

	/**
	 * Sets the min update time between location updates (in milliseconds).
	 *
	 * @param minUpdateTime the new min update time
	 */
	public void setMinUpdateTime(long minUpdateTime) {
		this.minUpdateTime = minUpdateTime;
	}

	/**
	 * Gets the min update distance traveled between location updates (in meters).
	 *
	 * @return the min update distance
	 */
	public float getMinUpdateDistance() {
		return minUpdateDistance;
	}

	/**
	 * Sets the min update distance traveled between location updates (in meters).
	 *
	 * @param minUpdateDistance the new min update distance
	 */
	public void setMinUpdateDistance(float minUpdateDistance) {
		this.minUpdateDistance = minUpdateDistance;
	}

	/**
	 * Gets the action via object that implements IOnLocationChanged interface.
	 *
	 * @return the action
	 */
	public IOnLocationChanged getAction() {
		return action;
	}

	/**
	 * Sets the action via object that implements IOnLocationChanged interface.
	 *
	 * @param action the new action
	 */
	public void setAction(IOnLocationChanged action) {
		this.action = action;
	}
	
	/**
	 * Gets the current best location.
	 *
	 * @return the current best location
	 */
	public Location getCurrentBestLocation() {
		return currentBestLocation;
	}

	/**
	 * Sets the current best location.
	 *
	 * @param currentBestLocation the new current best location
	 */
	public void setCurrentBestLocation(Location currentBestLocation) {
		this.currentBestLocation = currentBestLocation;
	}
	
	/**
	 * Determines whether one Location reading is better than the current Location fix.
	 *
	 * @param location  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	 * @return true, if is better location
	 */
	private boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 20;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/**
	 * Checks whether two providers are the same.
	 *
	 * @param provider1 the provider1
	 * @param provider2 the provider2
	 * @return true, if is same provider
	 */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

	public boolean isTracking() {
		return tracking;
	}

	private void setTracking(boolean tracking) {
		this.tracking = tracking;
	}
}


