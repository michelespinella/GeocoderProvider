package it.corvallis.geocoder.utils;

import java.io.IOException;

import org.apache.http.ParseException;

public abstract class GeocoderProvider {
	public abstract String getGeoCoder(String street, String postCode, String city) throws ParseException, IOException;
}
