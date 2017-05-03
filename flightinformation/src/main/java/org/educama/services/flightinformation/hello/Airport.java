package org.educama.services.flightinformation.hello;

import org.springframework.data.annotation.Id;

public class Airport {
	@Id
	public String id;

	public String name;
	public String iataCode;

	public Airport() {
	}

	public Airport(String name, String iataCode) {
		this.name = name;
		this.iataCode = iataCode;
	}

	@Override
	public String toString() {
		return String.format("Airport[id=%s, name='%s', IATA='%s']", id, name, iataCode);
	}

}
