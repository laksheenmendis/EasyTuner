package com.example.waveprocessor;

import com.example.easytuner.R;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FrequencyActivity extends Activity {

	Button start;
	String string_note;
	float std_freq;
	String decision=null;
	String to_display;
	TextView txt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frequency);

		Bundle bundle = this.getIntent().getExtras();
		string_note = bundle.getString("chosen_String");

		start = (Button) findViewById(R.id.button1);
		txt= (TextView) findViewById(R.id.textView1);
		txt.setText(null);
		start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txt.setText(null);
				start.setEnabled(false);
				recorderThread th = new recorderThread();
				th.run();
				
			}
		});
	}
	
	class recorderThread extends Thread {
		public boolean recording; // variable to start or stop recording
		public int frequency; // the public variable that contains the frequency
								// value "heard", it is updated continually
								// while the thread is running.

		public recorderThread() {
		}

		@Override
		public void run() {
			AudioRecord recorder;
			int numCrossing, p;
			short audioData[];
			int bufferSize;

			bufferSize = AudioRecord
					.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT) * 3; // get the buffer size to use with
																	// this audio record

			recorder = new AudioRecord(AudioSource.MIC, 8000,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize); // instantiate the AudioRecorder

			recording = true; // variable to use start or stop recording
			audioData = new short[bufferSize]; // short array that pcm data is put into.

			long startTime = System.currentTimeMillis();
			long endTime = startTime + 8000L; // 500L=0.5s, therefore,it will record for 6s (6000L)

			while (System.currentTimeMillis() < endTime) { // loop while recording is needed
				if (recorder.getState() == android.media.AudioRecord.STATE_INITIALIZED) // check
															// to see if the recorder has initialized yet.

					if (recorder.getRecordingState() == android.media.AudioRecord.RECORDSTATE_STOPPED)
						recorder.startRecording(); // check to see if the
													// Recorder has stopped or
													// is not recording, and
													// make it record.

					else {
						recorder.read(audioData, 0, bufferSize); // read the PCM
																	// audio
																	// data into
																	// the
																	// audioData
																	// array

						// Zero Crossings Method to decode PCM data

						numCrossing = 0; // initialize your number of zero
											// crossings to 0
						for (p = 0; p < bufferSize / 4; p += 4) {
							if (audioData[p] > 0 && audioData[p + 1] <= 0)
								numCrossing++;
							if (audioData[p] < 0 && audioData[p + 1] >= 0)
								numCrossing++;
							if (audioData[p + 1] > 0 && audioData[p + 2] <= 0)
								numCrossing++;
							if (audioData[p + 1] < 0 && audioData[p + 2] >= 0)
								numCrossing++;
							if (audioData[p + 2] > 0 && audioData[p + 3] <= 0)
								numCrossing++;
							if (audioData[p + 2] < 0 && audioData[p + 3] >= 0)
								numCrossing++;
							if (audioData[p + 3] > 0 && audioData[p + 4] <= 0)
								numCrossing++;
							if (audioData[p + 3] < 0 && audioData[p + 4] >= 0)
								numCrossing++;
						}

						for (p = (bufferSize / 4) * 4; p < bufferSize - 1; p++) {
							if (audioData[p] > 0 && audioData[p + 1] <= 0)
								numCrossing++;
							if (audioData[p] < 0 && audioData[p + 1] >= 0)
								numCrossing++;
						}
					

						frequency = (8000 / bufferSize) * (numCrossing / 2); // Set the audio
									// Frequency to half the number of zero crossings, times
									// the number of samples our buffersize is per second.
						
						Log.d("FREQUENCY", String.valueOf(frequency));
						if(decision==null){
							compareWithStd(frequency);
						}else{
							if(decision.equalsIgnoreCase("low")){
								compareWithStd(frequency);
							}else if(decision.equalsIgnoreCase("matched")){
								compareWithStd(frequency);
							}else if(decision.equalsIgnoreCase("high")){
								endTime=System.currentTimeMillis();
							}
						}
						
					}

			} 

			if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
				recorder.stop(); // stop the recorder before ending the thread
			recorder.release(); // release the recorders resources
			recorder = null; // set the recorder to be garbage collected.
			
			txt.setText(decision);
			to_display=getToastText();
			Toast.makeText(getApplicationContext(),to_display, Toast.LENGTH_SHORT).show();
			start.setEnabled(true);

		}
		
	}
	
	/*
	 * 
	 */
	public float getStandardFrequency(String note){
		
		float f = 0;
		
		if(note.equalsIgnoreCase("low e")){
			f=(float)82.41;
		}else if(note.equalsIgnoreCase("a")){
			f=(float) 110.00;
		}else if(note.equalsIgnoreCase("d")){
			f=(float) 146.83;
		}else if(note.equalsIgnoreCase("g")){
			f=(float) 196.00;			
		}else if(note.equalsIgnoreCase("b")){
			f=(float) 246.94;			
		}else if(note.equalsIgnoreCase("high e")){
			f=(float) 329.63;
		}
		return f;
		
	}
	
	public void compareWithStd(int freq){
		float std_frq=getStandardFrequency(string_note);
		float range_low= (float) (std_frq - (std_frq*0.01));
		float range_high= (float) (std_frq + (std_frq*0.01)) ;
		
		if(freq>=range_low && freq<=range_high){
			decision="matched";
		}else if(freq<range_low){
			decision="low";			
		}else
			decision="high";
		
	}
	
	public String getToastText(){
		String msg = null;
		
		if(decision.equalsIgnoreCase("low")){
			msg="Tighten the string";
		}else if(decision.equalsIgnoreCase("high")){
			msg="Loose the string";
		}else if(decision.equalsIgnoreCase("matched")){
			msg="You're done";
		}
		return msg;
	}
	
}
