package it.corvallis.geocoder.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App {

	private static final Logger logger = LogManager.getLogger(App.class);
	
	public static void main(String[] args) {
		String address = args[0];
		String postalcode = args[1];
		String city = args[2];
		String geocoderType = args[3];
        logger.debug("Parameters : " + address +" "+postalcode+" "+city+" "+geocoderType);

		if (geocoderType.equals("BING")) {
			GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
			try {
				logger.debug("Provider to use : " + geocoderType);
				geocodedAddress.getGeometry(address, postalcode, city);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.debug("Error : " + e.getMessage());
				e.printStackTrace();
			}
			// System.out.println(getLocalCurrentDate());
		} else if (geocoderType.equals("CSV")) {
			GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
			try {
				logger.debug("Provider to use : " + geocoderType);
				geocodedAddress.bulkGeocoder();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.debug("Error : " + e.getMessage());
				e.printStackTrace();
			}
		} else if (geocoderType.equals("LIQ")){
			GeocoderProviderLocIq geocodedAddress = new GeocoderProviderLocIq();
			try {
				logger.debug("Provider to use : " + geocoderType);
				geocodedAddress.getGeometry(address, postalcode, city);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.debug("Error : " + e.getMessage());
				e.printStackTrace();
			}
		} else if (geocoderType.equals("MAPQ")){
			GeocoderProviderMapQ geocodedAddress = new GeocoderProviderMapQ();
			try {
				logger.debug("Provider to use : " + geocoderType);
				geocodedAddress.getGeometry(address, postalcode, city);
			} catch (Exception e) {
				logger.debug("Error : " + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (geocoderType.equals("MAPBOX")){
			try {
				logger.debug("Provider to use : " + geocoderType);
				String p = GeocodingService.getGeoCoding(address, postalcode, city, geocoderType);
				
			} catch (Exception e) {
				logger.debug("Error : " + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
