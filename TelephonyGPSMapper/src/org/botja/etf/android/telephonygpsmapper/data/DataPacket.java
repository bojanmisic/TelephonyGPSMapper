package org.botja.etf.android.telephonygpsmapper.data;

public class DataPacket {
	private int signalStrength;
	private String networkType;
	private String mobileProvider;
	private int cellId;
	private int lac;
	private double latitude;
	private double longitude;
	private double altitude;
	private String locationProvider;
	private float locationAccuracy;
	private String reverseGeocodedLocation;
	
	public DataPacket(
			int signalStrength,
			String networkType,
			String mobileProvider,
			int cellId,
			int lac,
			double latitude,
			double longitude,
			double altitude,
			String locationProvider,
			float locationAccuracy,
			String reverseGeocodedLocation) {
		
		setSignalStrength(signalStrength);
		setNetworkType(networkType);
		setMobileProvider(mobileProvider);
		setCellId(cellId);
		setLac(lac);
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
		setLocationProvider(locationProvider);
		setLocationAccuracy(locationAccuracy);
		setReverseGeocodedLocation(reverseGeocodedLocation);
	}
	
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getMobileProvider() {
		return mobileProvider;
	}
	public void setMobileProvider(String mobileProvider) {
		this.mobileProvider = mobileProvider;
	}
	public int getCellId() {
		return cellId;
	}
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getLocationProvider() {
		return locationProvider;
	}
	public void setLocationProvider(String locationProvider) {
		this.locationProvider = locationProvider;
	}
	public float getLocationAccuracy() {
		return locationAccuracy;
	}
	public void setLocationAccuracy(float locationAccuracy) {
		this.locationAccuracy = locationAccuracy;
	}
	public String getReverseGeocodedLocation() {
		return reverseGeocodedLocation;
	}
	public void setReverseGeocodedLocation(String reverseGeocodedLocation) {
		this.reverseGeocodedLocation = reverseGeocodedLocation;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}
