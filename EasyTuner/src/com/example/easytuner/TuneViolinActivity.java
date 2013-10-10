package com.example.easytuner;

import com.example.waveprocessor.FrequencyViolinActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class TuneViolinActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tune_violin);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tune_violin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();

			Display display = getWindowManager().getDefaultDisplay();

			/*
			 * When the guitar string is touched it scales and get the x,y coordinates and
			 * matches to the string
			 */
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
			@SuppressWarnings("deprecation")
			int height = display.getHeight();
			
			if(height<581){
				if (((width * 106.0 / 480) - 20) < x
						&& x < ((width * 106.0 / 480) + 20)) {
					strartDialog("G");
				}

				if (((width * 188 / 480) - 20) < x
						&& x < ((width * 188 / 480) + 20)) {
					strartDialog("D");
				}

				if (((width * 280 / 480) - 20) < x
						&& x < ((width * 280 / 480) + 20)) {
					strartDialog("A");
				}

				if (((width * 263 / 480) - 20) < x
						&& x < ((width * 263 / 480) + 20)) {
					strartDialog("E");
				}
				
			}else{
				if (((width * 106.0 / 480) - 20) < x
						&& x < ((width * 106.0 / 480) + 20)) {
					strartDialog("G");
				}

				if (((width * 193 / 480) - 20) < x
						&& x < ((width * 193 / 480) + 20)) {
					strartDialog("D");
				}

				if (((width * 278 / 480) - 20) < x
						&& x < ((width * 278 / 480) + 20)) {
					strartDialog("A");
				}

				if (((width * 358 / 480) - 20) < x
						&& x < ((width * 358 / 480) + 20)) {
					strartDialog("E");
				}
				
			}
			

		}
		return super.onTouchEvent(event);
	}
	
	/*
	 * Creates the dialog box
	 */
	public void strartDialog(final String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TuneViolinActivity.this);
		builder.setMessage("Tune " + s + "?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getBaseContext(),
								FrequencyViolinActivity.class);
						
								Bundle b1=new Bundle();
								b1.putString("chosen_String", s);
								intent.putExtras(b1);
								startActivity(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		builder.create();
		builder.show();
	}
	

}
