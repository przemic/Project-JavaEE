package pl.indev.rest;

import pl.indev.dao.EventDao;
import pl.indev.utils.Commons;

import com.google.gson.Gson;

public class EventService {
	
	private RestService rest = new RestService(Commons.REST_URL);
	
	public EventDao getEvent(int eventId) {
		String json = rest.invoke("events/"+eventId, "GET", null, 0);
		EventDao dao = new Gson().fromJson(json, EventDao.class);
		return dao;
	}
	
}
