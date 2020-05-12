package it.corvallis.geocoder.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.*;
import com.opencsv.CSVReader;
import java.io.FileReader;

public class GeocoderProviderMapQ {
	public String getGeometry(String address, String postalcode, String city) {
		try {
		    FileReader reader=new FileReader("application.properties");  
		    Properties p=new Properties();  
		    p.load(reader);
		    String MapQuestKey = p.getProperty("MapQuestKey");
		    String MapQuestEndpoint = p.getProperty("MapQuestEndPoint");
			OkHttpClient client = new OkHttpClient();

			String addressToFormat = address+" "+city;
			String fullAddress = addressToFormat.replace(" ", "+");
			Request request = new Request.Builder().url(
					MapQuestEndpoint+"?key="+MapQuestKey+"&location="+fullAddress)
					.get().build();

			Response response = client.newCall(request).execute();
			String dataFromBing = response.body().string();
			JSONObject point = getJsonKey(dataFromBing);
            String geometry = "POINT ("+point.get("lng").toString()+" "+point.get("lat").toString()+")"; 
			return geometry;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//TODO ms: to be completed after online providers
    public void bulkGeocoder() throws Exception {
        String csvFile = "/home/michele/lavoro2020/DatiCompleti/test_geocoding.csv";

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile), ',', '/', 1);
            String[] line;
            while ((line = reader.readNext()) != null) {
            	String geometry = this.getGeometry(line[6], "73100", "Lecce");
                System.out.println(line + "Indirizzo [id= " + line[6] +" geometry="+ geometry +"]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
	private JSONObject getJsonKey(String jsonObject) {
		JSONObject resobj = new JSONObject(jsonObject);
		Iterator<?> keys = resobj.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (key.equals("results")) {
				JSONObject propToRead = resobj.getJSONArray("results").getJSONObject(0).getJSONArray("locations")
						.getJSONObject(0).getJSONObject("latLng");
				return propToRead;
			}
		}
		return null;
	}
}
