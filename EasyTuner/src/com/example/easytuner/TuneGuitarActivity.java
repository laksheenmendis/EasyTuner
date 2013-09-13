package com.example.easytuner;

import com.example.waveprocessor.FrequencyActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class TuneGuitarActivity extends Activity {

	ImageView img1;
	TextView tx, tx1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tune_guitar);

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
		getMenuInflater().inflate(R.menu.tune_guitar, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();

			Display display = getWindowManager().getDefaultDisplay();

			@SuppressWarnings("deprecation")
			int width = display.getWidth();

			if (((width * 63.0 / 480) - 20) < x
					&& x < ((width * 63.0 / 480) + 20)) {
				strartDialog("Low E");
			}

			if (((width * 136 / 480) - 20) < x
					&& x < ((width * 136 / 480) + 20)) {
				strartDialog("A");
			}

			if (((width * 209 / 480) - 20) < x
					&& x < ((width * 209 / 480) + 20)) {
				strartDialog("D");
			}

			if (((width * 278 / 480) - 20) < x
					&& x < ((width * 278 / 480) + 20)) {
				strartDialog("G");
			}

			if (((width * 353 / 480) - 20) < x
					&& x < ((width * 353 / 480) + 20)) {
				strartDialog("B");
			}

			if (((width * 424 / 480) - 20) < x
					&& x < ((width * 424 / 480) + 20)) {

				strartDialog("High E");
			}

		}
		return super.onTouchEvent(event);
	}

	public void strartDialog(final String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TuneGuitarActivity.this);
		builder.setMessage("Tune " + s + "?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getBaseContext(),
								FrequencyActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("chosen_String", s);
						intent.putExtras(bundle);
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
	public void registerForContextMenu(View view) {
		// TODO Auto-generated method stub
		super.registerForContextMenu(view);
	}
}
