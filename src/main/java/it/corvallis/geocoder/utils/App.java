package it.corvallis.geocoder.utils;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import it.corvallis.geocoder.utils.GeocoderProviderBing;

public class App {

	private static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		String address = args[0];
		String postalcode = args[1];
		String city = args[2];
		String geocoderType = args[3];
		if (geocoderType.equals("BING")) {
			GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
			try {
				geocodedAddress.getGeometry(address, postalcode, city);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(getLocalCurrentDate());
		} else if (geocoderType.equals("CSV")) {
			GeocoderProviderBing geocodedAddress = new GeocoderProviderBing();
			try {
				geocodedAddress.bulkGeocoder();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (geocoderType.equals("LIQ")){
			GeocoderProviderLocIq geocodedAddress = new GeocoderProviderLocIq();
			try {
				geocodedAddress.getGeometry(address, postalcode, city);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (geocoderType.equals("MAPQ")){
			GeocoderProviderMapQ geocodedAddress = new GeocoderProviderMapQ();
			try {
				geocodedAddress.getGeometry(address, postalcode, city);
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

}
