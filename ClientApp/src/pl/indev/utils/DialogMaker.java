package pl.indev.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class DialogMaker {
	
	static public void showAlert(final Activity context, CharSequence text, String title) {
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		
		alert_builder.setTitle(title);
		alert_builder.setNeutralButton("OK", new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				context.setResult(Activity.RESULT_OK);
			  	context.finish();					
			}
		});
		
		alert_builder.setCancelable(true);
		alert_builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				context.setResult(Activity.RESULT_OK);
				context.finish();
			}
		});
		
		alert_builder.setMessage(text);
		alert_builder.create().show();
	}
	
	static public void showInfo(final Activity context, CharSequence text, String title) {
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		
		alert_builder.setTitle(title);
		alert_builder.setNeutralButton("OK", new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				context.setResult(Activity.RESULT_OK);
			  					
			}
		});
			
		alert_builder.setMessage(text);
		alert_builder.create().show();
	}
	
}
