package it.corvallis.geocoder.utils;

import java.io.IOException;
import java.util.Iterator;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.*;

public class GeocoderProviderBing {

	public String getCoordinates() {
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url(
					"http://dev.virtualearth.net/REST/v1/Locations?countryRegion=ITA&postalCode=73100&addressLine=77%2BVIA%2BDALMAZIO%2BBIRAGO&maxResults=2&key=Aq7ibVaUqO0zn3fw1zEih5tcNeN8B5cc5EQjDJaupcSNvMlbm9MgDy5Iy-FfkC9u")
					.get().build();

			Response response = client.newCall(request).execute();
			String dataFromBing = response.body().string();
			JSONObject point = getJsonKey(dataFromBing);
            String geometry = "POINT ("+point.getJSONArray("coordinates").get(1).toString()+" "+point.getJSONArray("coordinates").get(0).toString()+")"; 
			return geometry;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject getJsonKey(String jsonObject) {
		JSONObject resobj = new JSONObject(jsonObject);
		Iterator<?> keys = resobj.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (key.equals("resourceSets")) {
				JSONObject propToRead = resobj.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources")
						.getJSONObject(0).getJSONObject("point");
				return propToRead;
			}
		}
		return null;
	}
}
