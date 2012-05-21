package pl.indev.dao;

public class PlaceDao {
	public int id;
	public String name;
	public String longitude;
	public String latitude;
	public String streetName;
	public int buildingNumber;
	public int flatNumber;
	public String city;
	
	public PlaceDao (int _id, String _name, String _longitude, String _latitude, String _streetName, int _buildingNumber, int _flatNumber, String _city) {
		id = _id;
		name = _name;
		longitude = _longitude;
		latitude = _latitude;
		streetName = _streetName;
		buildingNumber = _buildingNumber;
		flatNumber = _flatNumber;
		city = _city;
	}
	
	public PlaceDao () {
		
	}
}
