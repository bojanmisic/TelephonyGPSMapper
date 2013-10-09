package org.botja.etf.android.telephonygpsmapper.data;


// statically implemented TODO TODO TODO
public class Kml {
	public static final String XmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String KmlHeader = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
	public static final String KmlClosingTag = "</kml>";
	public static final String DocumentOpeningTag = "<Document>";
	public static final String DocumentClosingTag = "</Document>";
	public static final String NameOpeningTag = "<name>";
	public static final String NameClosingTag = "</name>";
	public static final String DescriptionOpeningTag = "<description>";
	public static final String DescriptionClosingTag = "</description>";
	public static final String StyleIdGreenLineTag = "<Style id=\"greenLine\"><LineStyle><color>64147800</color><width>6</width></LineStyle></Style>";
	public static final String StyleIdYellowLineTag = "<Style id=\"yellowLine\"><LineStyle><color>6414F0FF</color><width>6</width></LineStyle></Style>";
	public static final String StyleIdRedLineTag = "<Style id=\"redLine\"><LineStyle><color>641400FF</color><width>6</width></LineStyle></Style>";
	public static final String StyleIdGreenIconTag = "<Style id=\"greenIcon\"><IconStyle><color>64147800</color><scale>1.5</scale><Icon><href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href></Icon></IconStyle></Style>";
	public static final String StyleIdYellowIconTag = "<Style id=\"yellowIcon\"><IconStyle><color>6414F0FF</color><scale>1.5</scale><Icon><href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href></Icon></IconStyle></Style>";		
	public static final String StyleIdRedIconTag = "<Style id=\"redIcon\"><IconStyle><color>641400FF</color><scale>1.5</scale><Icon><href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href></Icon></IconStyle></Style>";		
	public static final String StyleUrlGreenLineTag = "<styleUrl>#greenLine</styleUrl>";
	public static final String StyleUrlYellowLineTag = "<styleUrl>#yellowLine</styleUrl>";
	public static final String StyleUrlRedLineTag = "<styleUrl>#redLine</styleUrl>";
	public static final String StyleUrlGreenIconTag = "<styleUrl>#greenIcon</styleUrl>";
	public static final String StyleUrlYellowIconTag = "<styleUrl>#yellowIcon</styleUrl>";
	public static final String StyleUrlRedIconTag = "<styleUrl>#redIcon</styleUrl>";
	public static final String PlacemarkOpeningTag = "<Placemark>";
	public static final String PlacemarkClosingTag = "</Placemark>";
	public static final String LineStringOpeningTag = "<LineString>";
	public static final String LineStringClosingTag = "</LineString>";
	public static final String ExtrudeTag = "<extrude>1</extrude>";
	public static final String TessellateTag = "<tessellate>1</tessellate>";
	public static final String AltitudeModeTag = "<altitudeMode>absolute</altitudeMode>";
	public static final String PointOpeningTag = "<Point>";
	public static final String PointClosingTag = "</Point>";
	public static final String CoordinatesOpeningTag = "<coordinates>";
	public static final String CoordinatesClosingTag = "</coordinates>";
	public static final String CDATAOpeningTag = "<![CDATA[";
	public static final String CDATAClosingTag = "]]>";
	
	public static final String HTMLTableOpeningTag = "<table border=\"1\">";
	public static final String HTMLTableClosingTag = "</table>";
	public static final String HTMLTableRowOpeningTag = "<tr>";
	public static final String HTMLTableRowClosingTag = "</tr>";
	public static final String HTMLTableCellYellowBackgroundOpeningTag = "<td bgcolor=\"yellow\">";
	public static final String HTMLTableCellGreenBackgroundOpeningTag = "<td bgcolor=\"green\">";
	public static final String HTMLTableCellRedBackgroundOpeningTag = "<td bgcolor=\"red\">";
	public static final String HTMLTableCellOpeningTag = "<td>";
	public static final String HTMLTableCellClosingTag = "</td>";
	public static final String HTMLParagraphAlignCenterOpeningTag = "<p align=\"center\">";
	public static final String HTMLParagraphClosingTag = "</p>";
	public static final String HTMLBoldOpeningTag = "<b>";
	public static final String HTMLBoldClosingTag = "</b>";
	public static final String HTMLFontColorRedOpeningTag = "<font color=\"red\">";
	public static final String HTMLFontColorYellowOpeningTag = "<font color=\"yellow\">";
	public static final String HTMLFontColorGreenOpeningTag = "<font color=\"green\">";
	public static final String HTMLFontColorWhiteOpeningTag = "<font color=\"white\">";
	public static final String HTMLFontOpeningTag = "<font>";
	public static final String HTMLFontClosingTag = "</font>";
	
	public static final String HTMLSignalStrengthCell = "<td><b>Signal strength</b></td>";
	public static final String HTMLNetworkTypeCell = "<td>Network type</td>";
	public static final String HTMLMobileProviderCell = "<td>Mobile Provider</td>";
	public static final String HTMLCellIDCell = "<td>Cell ID</td>";
	public static final String HTMLLocationAreaCodeCell = "<td>Location Area Code</td>";
	public static final String HTMLLatitudeCell = "<td>Latitude</td>";
	public static final String HTMLLongitudeCell = "<td>Longitude</td>";
	public static final String HTMLAltitudeCell = "<td>Altitude</td>";
	public static final String HTMLLocationProviderCell = "<td>Location Provider</td>";
	public static final String HTMLAccuracyCell = "<td>Accuracy</td>";
	public static final String HTMLReverseGeocodedLocationCell = "<td>Reverse Geocoded Location</td>";
	
	
	public static String ConvertDataToKml(Data data) {
		StringBuilder kml = new StringBuilder();
		
		kml.append(XmlHeader)
		.append(KmlHeader)
		.append(DocumentOpeningTag)
		.append(NameOpeningTag)
		.append("Telephony GPS Mapper Visualization Data")
		.append(NameClosingTag)
		.append(DescriptionOpeningTag)
		.append("Data Collected via Telephony GPS Mapper Android application, to be used when analysing Mobile Network's signal coverage")
		.append(DescriptionClosingTag)
		.append(StyleIdGreenLineTag)
		.append(StyleIdYellowLineTag)
		.append(StyleIdRedLineTag)
		.append(StyleIdGreenIconTag)
		.append(StyleIdYellowIconTag)
		.append(StyleIdRedIconTag);
		
		if (data != null) {
			for (DataPacket packet: data.getDataList())
			{
			StringBuilder pointDescription = new StringBuilder();
			String color;
			double signalAltitude = 113 - Math.abs(packet.getSignalStrength()) + packet.getAltitude();
			
			if (packet.getSignalStrength() <= -93)
				color = "red";
			else if (packet.getSignalStrength() <= -72)
				color = "yellow";
			else 
				color = "green";
						
			pointDescription.append(CDATAOpeningTag)
			.append(HTMLTableOpeningTag)
			.append(HTMLTableRowOpeningTag)
			.append(HTMLSignalStrengthCell)
			.append(color.compareTo("red")==0 ? HTMLTableCellRedBackgroundOpeningTag : (color.compareTo("yellow")==0 ? HTMLTableCellYellowBackgroundOpeningTag : HTMLTableCellGreenBackgroundOpeningTag))
			.append(HTMLBoldOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(color.compareTo("red")==0 ? HTMLFontColorWhiteOpeningTag : (color.compareTo("yellow")==0 ? HTMLFontOpeningTag : HTMLFontColorWhiteOpeningTag))
			.append(String.valueOf(packet.getSignalStrength()).concat(" dBm"))
			.append(HTMLFontClosingTag)
			.append(HTMLParagraphClosingTag)
			.append(HTMLBoldClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLNetworkTypeCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getNetworkType())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLMobileProviderCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getMobileProvider())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLCellIDCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getCellId())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLLocationAreaCodeCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getLac())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLLatitudeCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getLatitude())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLLongitudeCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getLongitude())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLAltitudeCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(String.valueOf(packet.getAltitude()).concat("m (ASL)"))
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLLocationProviderCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getLocationProvider())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLAccuracyCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(String.valueOf(packet.getLocationAccuracy()).concat(" m"))
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableRowOpeningTag)
			.append(HTMLReverseGeocodedLocationCell)
			.append(HTMLTableCellOpeningTag)
			.append(HTMLParagraphAlignCenterOpeningTag)
			.append(packet.getReverseGeocodedLocation())
			.append(HTMLParagraphClosingTag)
			.append(HTMLTableCellClosingTag)
			.append(HTMLTableRowClosingTag)
			
			.append(HTMLTableClosingTag)
			.append(CDATAClosingTag);
			
			kml.append(PlacemarkOpeningTag)
			.append(color.compareTo("red")==0 ? StyleUrlRedLineTag : (color.compareTo("yellow")==0 ? StyleUrlYellowLineTag : StyleUrlGreenLineTag))
			.append(LineStringOpeningTag)
			.append(ExtrudeTag)
			.append(TessellateTag)
			.append(AltitudeModeTag)
			.append(CoordinatesOpeningTag)
			.append(String.valueOf(packet.getLongitude()).concat(",").concat(String.valueOf(packet.getLatitude())).concat(",").concat(String.valueOf(signalAltitude)))
			.append(CoordinatesClosingTag)
			.append(LineStringClosingTag)
			.append(PlacemarkClosingTag);
			
			kml.append(PlacemarkOpeningTag)
			.append(NameOpeningTag)
			.append(String.valueOf(packet.getSignalStrength()).concat(" dBm"))
			.append(NameClosingTag)
			.append(DescriptionOpeningTag)
			.append(pointDescription.toString())
			.append(DescriptionClosingTag)
			.append(color.compareTo("red")==0 ? StyleUrlRedIconTag : (color.compareTo("yellow")==0 ? StyleUrlYellowIconTag : StyleUrlGreenIconTag))
			.append(PointOpeningTag)
			.append(AltitudeModeTag)
			.append(CoordinatesOpeningTag)
			.append(String.valueOf(packet.getLongitude()).concat(",").concat(String.valueOf(packet.getLatitude())).concat(",").concat(String.valueOf(signalAltitude)))
			.append(CoordinatesClosingTag)
			.append(PointClosingTag)
			.append(PlacemarkClosingTag);
			
			}
		}
		kml.append(DocumentClosingTag)
		.append(KmlClosingTag);
		
		return kml.toString();
	}
}
