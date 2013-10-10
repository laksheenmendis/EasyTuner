package com.example.waveprocessor;

import com.example.easytuner.R;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FrequencyViolinActivity extends Activity {
	Button start;
	String string_note;
	float std_freq;
	String decision=null;
	String to_display;
	TextView txt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_violin_frequency);

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
			int index;
			short audioData[];
			double array[];
			int bufferSize;
			boolean isEven=false;
			int sampleRate=8000; 	//from Hz

			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT) * 3; // get the buffer size to use with
																	// this audio record

			if(bufferSize % 2 ==0){
				isEven = true;
			}
			
			recorder = new AudioRecord(AudioSource.VOICE_RECOGNITION, sampleRate,
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
						
						DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);
						array=new double[bufferSize];
						for(int count=0;count<bufferSize;count++){
							array[count]=audioData[count];
							Log.d("copying to the double array",String.valueOf(array[count]));
						}
						fft.realForward(array);
						
						if(isEven){
							index=evenFrequencyCalc(array,bufferSize);							
						}else{
							index=oddFrequencyCalc(array,bufferSize);
						}
						
						
						frequency=(index * sampleRate)/bufferSize; 

						Log.d("FREQUENCY", String.valueOf(frequency));
						
						compareWithStd(frequency);
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
	 * calculates the spectrum and finds out the index of the maximum value
	 * when the bufferSize is even
	 */
	public int evenFrequencyCalc(double[] a,int n){
		
		int k=n/2;
		double spec[]=new double[k];
		
		spec[0]=a[0];
		
		for(int c=2;c<n;c=c+2){		
				spec[c/2]=Math.sqrt(Math.pow(a[c],2) + Math.pow(a[c+1],2));			
		}
		
		double max=0;
	    int maxIndex = 0;
		
		 for(int i = 1; i < spec.length; i++) {
             if (spec[i] > max) {
                 max = spec[i];
                 maxIndex = i;
             }
		 }      
		 
		 return maxIndex;
	}	
	
	/*
	 * calculates the spectrum and finds out the index of the maximum value
	 * when the bufferSize is odd
	 */
	public int oddFrequencyCalc(double[] a,int n){
		int k=(n-1)/2;
		double spec[]=new double[k];
		
		spec[0]=a[0];
		
		for(int c=2;c<n-1;c=c+2){		
				spec[c/2]=Math.sqrt(Math.pow(a[c],2) + Math.pow(a[c+1],2));			
		}
		
		double max=0;
	    int maxIndex = 0;
		
		 for(int i = 1; i < spec.length; i++) {
             if (spec[i] > max) {
                 max = spec[i];
                 maxIndex = i;
             }
		 }      
		 
		 return maxIndex;
	}
	
	/*
	 * when the string name is passed, this returns the standard frequency
	 */
	public float getStandardFrequency(String note){
		
		float f = 0;
		
		if(note.equalsIgnoreCase("g")){
			f=(float)195;
		}else if(note.equalsIgnoreCase("d")){
			f=(float) 293;
		}else if(note.equalsIgnoreCase("a")){
			f=(float) 440;
		}else if(note.equalsIgnoreCase("e")){
			f=(float) 659;			
		}
		return f;
		
	}
	
	/*
	 * compares the calculated frequency with the standard frequency of the string
	 */
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
	
	/*
	 * one of the 3 toast message types are chosen
	 */
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
