package org.botja.etf.android.telephonygpsmapper.sensory;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class TelephonySensor {
	private TelephonyManager telephonyManager = null;
	private boolean tracking = false;
	private IOnSignalStrengthsChanged action = null;
	
	public TelephonyManager getTelephonyManager() {
		return telephonyManager;
	}

	private void setTelephonyManager(TelephonyManager telephonyManager) {
		this.telephonyManager = telephonyManager;
	}
	
	/** Add custom actions when new telephony info is acquired. */
	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
    	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			if (action!=null) 
				action.onSignalStrengthsChanged(signalStrength);
		}
	};
	
	public TelephonySensor(TelephonyManager telephonyManager) {
		setTelephonyManager(telephonyManager);
	}
	
	public void startTrackingTelephony() {
		if (!isTracking()) {
			telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			setTracking(true);
		}
	}
	
	public void stopTrackingTelephony() {
		if (isTracking()) {
			telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
			setTracking(false);
		}
	}

	public boolean isTracking() {
		return tracking;
	}

	private void setTracking(boolean tracking) {
		this.tracking = tracking;
	}

	public IOnSignalStrengthsChanged getAction() {
		return action;
	}

	public void setAction(IOnSignalStrengthsChanged action) {
		this.action = action;
	}
}
