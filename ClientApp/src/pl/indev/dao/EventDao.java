package pl.indev.dao;

import java.sql.Date;

public class EventDao {
	public int id;
	public String name;
	public String date;
	public String descriptionText;
	public PlaceDao placeid;
	public int attendies;
	public int approved;
	
	public EventDao (int _id, String _name, String _date, String _desc, PlaceDao _placeid, int _attendies, int _approved) {
		id = _id;
		name = _name;
		date = _date;
		descriptionText = _desc;
		placeid = _placeid;
		attendies = _attendies;
		approved = _approved;
	}
	
	public EventDao() {
		
	}
	
	public String getDate() {
		Date d = new Date(Long.parseLong(date));
		return d.toString();
	}
	
	public String toString() {
		return name;
	}
}
