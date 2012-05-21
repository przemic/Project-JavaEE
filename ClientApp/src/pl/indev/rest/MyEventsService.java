package pl.indev.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import pl.indev.dao.EventDao;
import pl.indev.utils.Commons;

public class MyEventsService {
	
	private RestService rest = new RestService(Commons.REST_URL);
	
	public List<EventDao> getMyEvents() {
		List<EventDao> list = null;
		try{
			String json = rest.invoke("event/"+String.valueOf(Commons.USER_ID), "GET", null, 0);
			
			String events = new JsonParser().parse(json).toString();
			
			if( events.charAt(0) != '[' ) {
				EventDao event = new Gson().fromJson(events, EventDao.class);
				list = new ArrayList<EventDao>();
				list.add(event);
			} else {
				Type collectionType = new TypeToken<List<EventDao>>(){}.getType();
				list = new Gson().fromJson(events, collectionType);
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void removeFromFavourite(String userId, String eventId) {
		rest.invoke("remove/"+userId+"/"+eventId, "GET", null, 0);
	}

}
