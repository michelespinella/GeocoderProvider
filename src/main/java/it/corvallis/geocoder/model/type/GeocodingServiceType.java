package it.corvallis.geocoder.model.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;

import it.corvallis.geocoder.utils.EnumUtils;

@XmlEnum
public enum GeocodingServiceType implements Serializable {
	NOMINATIM, MAPBOX;

	public static GeocodingServiceType fromString(String text) {
		return EnumUtils.readEnum(text, GeocodingServiceType.values());
	}


}
