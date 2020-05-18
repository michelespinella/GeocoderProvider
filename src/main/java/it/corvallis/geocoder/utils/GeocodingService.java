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

	}

	public static String getGeoCoding(String street, String postCode, String city, String geocoderType)
			throws ClientProtocolException, IOException {
		return getGeoCodingReference(geocoderType).getGeoCoder(street, postCode, city);
	}

	private static GeocoderProvider getGeoCodingReference(String geocoderType) {
		if (geocoderType.equals("MAPBOX")) {
			return map.get(GeocodingServiceType.MAPBOX);
		}
		return null;

	}

}