package com.example.easytuner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Button tguitar, tviolin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tguitar = (Button) findViewById(R.layout.activity_main);
		tviolin = (Button) findViewById(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 *Executes when the 'Tune Guitar' button is pressed
	 */
	public void tuneGuitar(View view) {
		Intent intent = new Intent(this, TuneGuitarActivity.class);
		startActivity(intent);
	}

	/*
	 * Executes when the 'Tune Violin' button is pressed
	 */
	public void tuneViolin(View view) {
		Intent intent = new Intent(this, TuneViolinActivity.class);
		startActivity(intent);
	}
}
