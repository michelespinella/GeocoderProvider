package it.corvallis.geocoder.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;
import com.opencsv.CSVReader;



public class GeocoderProviderBing {
	private static final Logger logger = LogManager.getLogger(GeocoderProviderBing.class);

	public String getGeometry(String address, String postalcode, String city) {
		try {
		    FileReader reader=new FileReader("./application.properties");  
		    Properties p=new Properties();  
		    p.load(reader);
		    String BingKey = p.getProperty("BingKey");
		    String BingEndpoint = p.getProperty("BingEndpoint");
			OkHttpClient client = new OkHttpClient();

			String fullAddress = address+" "+city;
			Request request = new Request.Builder().url(
					BingEndpoint+"?countryRegion=ITA&postalCode="+postalcode+"&addressLine="+fullAddress+"&maxResults=2&key="+BingKey)
					.get().build();

			Response response = client.newCall(request).execute();
			String dataFromBing = response.body().string();
			JSONObject point = getJsonKey(dataFromBing);
            String geometry = "POINT ("+point.getJSONArray("coordinates").get(1).toString()+" "+point.getJSONArray("coordinates").get(0).toString()+")"; 
            logger.debug("Output WKT Geometry : " + geometry);
            response.body().close();
            return geometry;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("Error : " + e.getMessage());
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
        } catch (Exception e) {
        	logger.debug("Error : " + e.getMessage());
            e.printStackTrace();
        }

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

