package pl.indev.activities;

import pl.indev.R;
import pl.indev.dao.EventDao;
import pl.indev.rest.EventService;
import pl.indev.rest.EventsService;
import pl.indev.rest.MyEventsService;
import pl.indev.utils.Commons;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EventDetails extends Activity implements Runnable {
	
	private int eventId;
	private int myevents = 0;
	
	private TextView eventName;
	private TextView eventDesc;
	private TextView eventDate;
	private TextView eventPlace;
	
	private EventDao event;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			eventName.setText(event.name);
			eventDesc.setText(event.descriptionText);
			eventDate.setText(event.getDate());
			eventPlace.setText(event.placeid.city + ", " 
							   + event.placeid.streetName + " " 
							   + event.placeid.buildingNumber + "/" 
							   + event.placeid.flatNumber);
  }
};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        
        
        eventId = getIntent().getExtras().getInt("id");
        myevents = getIntent().getExtras().getInt("myevents");
        
        
        eventName = (TextView) findViewById(R.id.eventN);
        eventDesc = (TextView) findViewById(R.id.eventDesc);
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventPlace = (TextView) findViewById(R.id.eventPlace);
        
        final Button goButton = (Button) findViewById(R.id.goButton);
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button removeButton = (Button) findViewById(R.id.removeButton);
        
        if (myevents == 1) {
        	addButton.setVisibility(View.GONE);
        }	else {
        	removeButton.setVisibility(View.GONE);
        }
        
        goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + event.placeid.city + ", " + event.placeid.streetName + " " + event.placeid.buildingNumber ));
				intent.setClassName(
		                 "com.google.android.apps.maps",
		                 "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		});
        
        addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EventsService eventsService = new EventsService();
				eventsService.addToFavourite(String.valueOf(Commons.USER_ID), String.valueOf(event.id));
			}
		});
        
        removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyEventsService myeventsService = new MyEventsService();
				myeventsService.removeFromFavourite(String.valueOf(Commons.USER_ID), String.valueOf(event.id));
			}
		});
      
        Thread t = new Thread(EventDetails.this);
        t.start();
        
	}

	@Override
	public void run() {
		EventService eventService = new EventService();
		event = eventService.getEvent(eventId);		
		handler.sendEmptyMessage(0);
	}
	
}
