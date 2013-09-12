package software.easytuner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	/*@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}*/
	
	public void tuneGuitar(View view) {
		Intent intent=new Intent(this,TuneGuitar.class);
		startActivity(intent);
	}
	
	public void tuneViolin(View view) {
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
