package it.corvallis.geocoder.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import it.corvallis.geocoder.model.type.GeocodingServiceType;

public class GeocodingService  {

	private static final Map<GeocodingServiceType, GeocoderProvider> map = new HashMap<GeocodingServiceType, GeocoderProvider>();

	static {

		map.put(GeocodingServiceType.MAPBOX, new GeocoderProviderMapbox());
		map.put(GeocodingServiceType.MAPQUEST, new GeocoderProviderMapQ());
		map.put(GeocodingServiceType.LOCIQ, new GeocoderProviderLocIq());
		map.put(GeocodingServiceType.BING, new GeocoderProviderBing());	
	}

	public static String getGeoCoding(String street, String postCode, String city, String geocoderType)
			throws ClientProtocolException, IOException {
		return getGeoCodingReference(geocoderType).getGeoCoder(street, postCode, city);
	}

	private static GeocoderProvider getGeoCodingReference(String geocoderType) {
		if (geocoderType.equals("MAPBOX")) {
			return map.get(GeocodingServiceType.MAPBOX);
		} else if (geocoderType.equals("MAPQUEST")){
			return map.get(GeocodingServiceType.MAPQUEST);
		} else if (geocoderType.equals("LOCIQ")){
			return map.get(GeocodingServiceType.LOCIQ);
		} else if (geocoderType.equals("BING")){
			return map.get(GeocodingServiceType.BING);
		}
		return null;

	}

}