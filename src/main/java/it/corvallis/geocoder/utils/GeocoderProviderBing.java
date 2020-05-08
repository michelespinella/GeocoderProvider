package it.corvallis.geocoder.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.*;

public class GeocoderProviderBing {

	public String getCoordinates(String address, String postalcode) {
		try {
		    FileReader reader=new FileReader("application.properties");  
		    Properties p=new Properties();  
		    p.load(reader);
		    String BingKey = p.getProperty("BingKey");
		    String BingEndpoint = p.getProperty("BingEndpoint");
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url(
					BingEndpoint+"?countryRegion=ITA&postalCode="+postalcode+"&addressLine="+address+"&maxResults=2&key="+BingKey)
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
