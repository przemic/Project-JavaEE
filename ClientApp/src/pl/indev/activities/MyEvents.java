package pl.indev.activities;

import java.util.ArrayList;
import java.util.List;

import pl.indev.dao.EventDao;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pl.indev.R;
import pl.indev.utils.Commons;
import pl.indev.rest.MyEventsService;
import pl.indev.utils.DialogMaker;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class MyEvents extends ListActivity implements Runnable {

	private List<EventDao> myEvents;
	
	private ArrayAdapter<EventDao> arrayAdapter;
	private ProgressDialog progressDialog;

	private boolean canceled = false;

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if( !canceled ) {
	        	progressDialog.dismiss();
	        	
	        	if(myEvents.isEmpty()) {
	    			DialogMaker.showInfo(MyEvents.this, getResources().getText(R.string.alert_event_message), getResources().getText(R.string.alert_title).toString());
	    		} 
	    		
	        	arrayAdapter.notifyDataSetChanged();        	
        	}
        }
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myEvents = new ArrayList<EventDao>();
		
		arrayAdapter = new ArrayAdapter<EventDao>(this, R.layout.list_item, myEvents);
		setListAdapter(arrayAdapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setDivider(getResources().getDrawable(R.drawable.divider));
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				launchEventDetails(myEvents.get(position).id);
			}
		});
		
		final Thread t = new Thread(MyEvents.this);
		t.start();	
		
		progressDialog = ProgressDialog.show(MyEvents.this, "", getString(R.string.progress_dialog), true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				t.interrupt();
				canceled = true;
				finish();
			}
		});				
	}

	protected List<EventDao> getMyEvents() {
		MyEventsService myEventsService = new MyEventsService();
		
		return myEventsService.getMyEvents();
	}
	
	protected void launchEventDetails(int eventId) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setClassName(this, EventDetails.class.getName());
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", eventId);
		bundle.putInt("myevents", 1);	
		
		i.putExtras(bundle);
		
		startActivityForResult(i, Commons.REQUEST_VOTE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ) {
			finish();
		}
	}

	@Override
	public void run() {
		List<EventDao> myEventsFromServer = getMyEvents();
		
		if( myEventsFromServer != null ) {
			myEvents.clear();
			myEvents.addAll(myEventsFromServer);
		}
		
		handler.sendEmptyMessage(0);
	}

}
