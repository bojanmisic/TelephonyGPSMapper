package org.botja.etf.android.telephonygpsmapper.data;

import java.util.LinkedList;
import java.util.List;

public class Data {
	private List<DataPacket> data;

	public Data() {
		setDataList(new LinkedList<DataPacket>());
	}
	
	public void addPacket(DataPacket packet) {
		getDataList().add(packet);
	}

	public List<DataPacket> getDataList() {
		return data;
	}

	private void setDataList(List<DataPacket> data) {
		this.data = data;
	}
}
