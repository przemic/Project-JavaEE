package pl.indev;

import pl.indev.activities.Events;
import pl.indev.activities.MyEvents;
import pl.indev.utils.Commons;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.admob.android.ads.AdManager;

public class Client extends Activity implements Runnable {
    
	private Button mMyEvents;
	private Button mEvents;
	
	@Override
	public void run() {
		
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	AdManager.setTestDevices( new String[] {
    			AdManager.TEST_EMULATOR    			
    			} );
  	
        
    	mMyEvents = (Button) findViewById(R.id.myEventsButton);
      	mMyEvents.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				launchMyEvents();
			}
		});
        
    	mEvents = (Button) findViewById(R.id.eventsButton);
    	mEvents.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchEvents();
			}
		});

        Thread t = new Thread(Client.this);
        t.start();
    }

    protected void launchEvents() {    	
    	Intent i = new Intent(Intent.ACTION_VIEW);
		i.setClassName(this, Events.class.getName());
		startActivityForResult(i, Commons.REQUEST_PROGRESS_BAR);
    }
    
    protected void launchMyEvents() {    	
    	Intent i = new Intent(Intent.ACTION_VIEW);
		i.setClassName(this, MyEvents.class.getName());
		startActivityForResult(i, Commons.REQUEST_PROGRESS_BAR);
    }


}