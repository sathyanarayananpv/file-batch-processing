package com.hpe.interview.model;

public class GeoDataModel {

	private String anzsic06;
	private String area;
	private String year;
	private int geo_count; // using the same name as column name
	private int ec_count; // using the same name as column name

	public String getAnzsic06() {
		return anzsic06;
	}

	public void setAnzsic06(String anzsic06) {
		this.anzsic06 = anzsic06;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getGeo_count() {
		return geo_count;
	}

	public void setGeo_count(int geo_count) {
		this.geo_count = geo_count;
	}

	public int getEc_count() {
		return ec_count;
	}

	public void setEc_count(int ec_count) {
		this.ec_count = ec_count;
	}

}
