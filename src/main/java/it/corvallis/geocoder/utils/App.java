package it.corvallis.geocoder.utils;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import it.corvallis.geocoder.utils.GeocoderProviderBing;

public class App {

	private static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
        String address = args[0];
        String postalcode = args[1];
        String geocoderType = args[2];
        if (geocoderType.equals("BING")) {
    		getCoordinates(address, postalcode);
    		//System.out.println(getLocalCurrentDate());
        } else if(geocoderType.equals("CSV")) {
    		GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
    		try {
				geocodedAddress.bulkGeocoder();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  		
        }

	}

	private static String getLocalCurrentDate() {

		if (logger.isDebugEnabled()) {
			logger.debug("getLocalCurrentDate() is executed!");
		}

		LocalDate date = new LocalDate();
		return date.toString();

	}
	
	private static Boolean getCoordinates(String address, String postalcode) {
		
		GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
		System.out.println(geocodedAddress.getCoordinates(address, postalcode));
		return Boolean.TRUE;
		
	}

}
