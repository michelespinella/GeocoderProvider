package it.corvallis.geocoder.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.*;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeocoderProviderMapQ {
    private static final Logger logger = LogManager.getLogger(GeocoderProviderMapQ.class);	
    
    public String getGeometry(String address, String postalcode, String city) {
		try {
			InputStream in = getClass().getResourceAsStream("/application.properties"); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    //FileReader reader=new FileReader("application.properties");  
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
			String dataFromMapQ = response.body().string();
			JSONObject point = getJsonKey(dataFromMapQ);
            String geometry = "POINT ("+point.get("lng").toString()+" "+point.get("lat").toString()+")"; 
            logger.debug("Output WKT Geometry : " + geometry);
			response.body().close();
            return geometry;
		} catch (Exception e) {
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
			if (key.equals("results")) {
				JSONObject propToRead = resobj.getJSONArray("results").getJSONObject(0).getJSONArray("locations")
						.getJSONObject(0).getJSONObject("latLng");
				return propToRead;
			}
		}
		return null;
	}
}
