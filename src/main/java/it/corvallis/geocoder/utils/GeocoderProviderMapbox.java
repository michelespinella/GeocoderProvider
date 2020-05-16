package it.corvallis.geocoder.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.opencsv.CSVReader;


public class GeocoderProviderMapbox extends GeocoderProvider {
	private static final Logger logger = LogManager.getLogger(GeocoderProviderMapbox.class);

	@Override
	public String getGeoCoder(String street, String postCode, String city) throws ParseException, IOException {
		return this.getGeometry(street, postCode, city);	
	}
	
	public String getGeometry(String address, String postalcode, String city) {
		try {
			InputStream in = getClass().getResourceAsStream("/application.properties");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// FileReader reader=new FileReader("application.properties");
			Properties p = new Properties();
			p.load(reader);
			String MapboxKey = p.getProperty("MapboxKey");
			String MapboxEndPoint = p.getProperty("MapboxEndPoint");
			String fullAddress = address+" "+postalcode+" "+city;

			HttpClient client = HttpClientBuilder.create().build();
			StringBuilder url = new StringBuilder();
			url.append(MapboxEndPoint);
			url.append(encodeValue(fullAddress)+".json?access_token=");
			url.append(MapboxKey);
			HttpGet getRequest = new HttpGet(url.toString());
			HttpResponse response = client.execute(getRequest);
			logger.debug("Status Code: ", response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				JSONObject point = getJsonKey(result.toString());
	            String geometry = "POINT ("+point.getJSONArray("coordinates").get(0).toString()+" "+point.getJSONArray("coordinates").get(1).toString()+")"; 
	            logger.debug("Output WKT Geometry : " + geometry);
	            return geometry;
			} else {
				logger.debug("Error: ", response.toString());
				throw new HttpResponseException(response.getStatusLine().getStatusCode(),response.getEntity().toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("Error : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// TODO ms: to be completed after online providers
	public void bulkGeocoder() throws Exception {
		String csvFile = "/home/michele/lavoro2020/DatiCompleti/test_geocoding.csv";

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile), ',', '/', 1);
			String[] line;
			while ((line = reader.readNext()) != null) {
				String geometry = this.getGeometry(line[6], "73100", "Lecce");
				System.out.println(line + "Indirizzo [id= " + line[6] + " geometry=" + geometry + "]");
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
			if (key.equals("features")) {
				JSONObject propToRead = resobj.getJSONArray("features").getJSONObject(0).getJSONObject("geometry");
				return propToRead;
			}
		}
		return null;
	}
	
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}
