package it.corvallis.geocoder.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;



public class GeocoderProviderNominatim extends GeocoderProvider {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	@Override
	public String getGeoCoder(String street, String postCode, String city) throws ParseException, IOException {

		HttpClient client = HttpClientBuilder.create().build();
		StringBuilder url = new StringBuilder();

		// url.append(configServiceGeo.getProperty(IGeoConstants.GeoStrings.nominatimAddress));
	    String nominatimAddress = "https://nominatim.openstreetmap.org";
		// String nominatimAddress = "http://172.31.39.6:8989";

		url.append(nominatimAddress);
		url.append("/search?");
		url.append("street=");

		street = street.replace(" ", "+");
		url.append(street).append("&postalcode=").append(postCode).append("&format=json").append("&city=").append(city)
				.append("&limit=1");

		// url.append("&postalcode=73100&format=json&city=lecce&limit=1");

		HttpGet getRequest = new HttpGet(url.toString());
		HttpResponse response = client.execute(getRequest);
		String result = EntityUtils.toString(response.getEntity());
		JSONArray rsPath = new JSONArray(result);

		JSONObject rs = rsPath.getJSONObject(0);

		String lon = rs.getString("lon");
		String lat = rs.getString("lat");

		String geoWkt = "POINT(" + lon + "," + lat + ")";

		return geoWkt;
	}
	
	

}