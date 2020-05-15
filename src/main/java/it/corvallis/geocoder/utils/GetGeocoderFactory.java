package it.corvallis.geocoder.utils;


public class GetGeocoderFactory {
	public GeocoderProvider getGeocoderProvider(String GeocoderType) {
		if (GeocoderType == null) {
			return null;
		}
		if (GeocoderType.equalsIgnoreCase("MAPBOX")) {
			return new GeocoderProviderMapbox();
		}

		return null;
	}
}
