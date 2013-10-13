package com.example.waveprocessor;

import com.example.easytuner.R;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

;

public class FrequencyGuitarActivity extends Activity {

	Button start;
	String string_note;
	float std_freq;
	String decision = null;
	String to_display;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guitar_frequency);

		Bundle bundle = this.getIntent().getExtras();
		string_note = bundle.getString("chosen_String");

		start = (Button) findViewById(R.id.button1);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				start.setEnabled(false);
				recorderThread th = new recorderThread();
				th.run();

			}
		});
	}

	class recorderThread extends Thread {
		public boolean recording; // variable to start or stop recording
		public double frequency; // the public variable that contains the frequency
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
			boolean isEven = false;
			int sampleRate = 8000; // from Hz
			int matched_count = 0, low_count = 0, high_count = 0;

			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT) * 3; // get the
																	// buffer
																	// size to
																	// use with
																	// this
																	// audio
																	// record

			if (bufferSize % 2 == 0) {
				isEven = true;
			}

			recorder = new AudioRecord(AudioSource.MIC, sampleRate,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize); // instantiate
																	// the
																	// AudioRecorder

			recording = true; // variable to use start or stop recording
			audioData = new short[bufferSize]; // short array that pcm data is
												// put into.

			long startTime = System.currentTimeMillis();
			long endTime = startTime + 8000L; // 500L=0.5s, therefore,it will
												// record for 6s (6000L)

			while (System.currentTimeMillis() < endTime) { // loop while
															// recording is
															// needed
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
						array = new double[bufferSize];
						for (int count = 0; count < bufferSize; count++) {
							array[count] = (double) audioData[count];
							// Log.d("copying to the double array",String.valueOf(array[count]));
						}

						fft.realForward(array);

						if (isEven) {
							index = evenFrequencyCalc(array);
						} else {
							index = oddFrequencyCalc(array);
						}

						frequency = (double)(index * sampleRate) / (double)bufferSize;

						//Log.d("Note", string_note);
						//Log.d("FREQUENCY", String.valueOf(frequency));

						compareWithStd(frequency);

						if (decision.equalsIgnoreCase("matched")) {
							matched_count++;
						} else if (decision.equalsIgnoreCase("low")) {
							low_count++;
						} else if (decision.equalsIgnoreCase("high")) {
							high_count++;
						}

						//Log.d("decision in the loop", decision);
					}

			}

			if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
				recorder.stop(); // stop the recorder before ending the thread
			recorder.release(); // release the recorders resources
			recorder = null; // set the recorder to be garbage collected.

			if (matched_count >= 3) {
				to_display = "String is tuned";
			} else if (high_count > low_count) {
				to_display = "Loose the string";
			} else {
				to_display = "Tighten the string";
			}

			//Log.d("decision is ", to_display);
			Toast.makeText(getApplicationContext(), to_display,
					Toast.LENGTH_SHORT).show();
			start.setEnabled(true);

		}

	}

	/*
	 * calculates the spectrum and finds out the index of the maximum value when
	 * the bufferSize is even
	 */
	public int evenFrequencyCalc(double[] a) {

		int k = a.length / 2;
		double spec[] = new double[k];

		for (int c = 0; c < a.length; c += 2) {
			spec[c / 2] = Math.sqrt(Math.pow(a[c], 2) + Math.pow(a[c + 1], 2));
		}

		double max = 0;
		int maxIndex = -1;

		for (int i = 1; i < spec.length; i++) {
			if (spec[i] > max) {
				max = spec[i];
				maxIndex = i;
			}
		}

		return maxIndex;
	}

	/*
	 * calculates the spectrum and finds out the index of the maximum value when
	 * the bufferSize is odd
	 */
	public int oddFrequencyCalc(double[] a) {
		int k = (a.length - 1) / 2;
		double spec[] = new double[k];

		for (int c = 0; c < (a.length - 1); c += 2) {
			spec[c / 2] = Math.sqrt(Math.pow(a[c], 2) + Math.pow(a[c + 1], 2));
		}

		double max = 0;
		int maxIndex = 0;

		for (int i = 1; i < spec.length; i++) {
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
	public float getStandardFrequency(String note) {

		float f = 0;

		if (note.equalsIgnoreCase("low e")) {
			f = (float) 82.41;
		} else if (note.equalsIgnoreCase("a")) {
			f = (float) 110.00;
		} else if (note.equalsIgnoreCase("d")) {
			f = (float) 146.83;
		} else if (note.equalsIgnoreCase("g")) {
			f = (float) 196.00;
		} else if (note.equalsIgnoreCase("b")) {
			f = (float) 246.94;
		} else if (note.equalsIgnoreCase("high e")) {
			f = (float) 329.63;
		}
		return f;

	}

	/*
	 * compares the calculated frequency with the standard frequency of the
	 * string
	 */
	public void compareWithStd(double freq) {
		float std_frq = getStandardFrequency(string_note);
		float range_low = (float) (std_frq - (std_frq * 0.015));
		float range_high = (float) (std_frq + (std_frq * 0.015));

		if (freq >= range_low && freq <= range_high) {
			decision = "matched";
		} else if (freq < range_low) {
			decision = "low";
		} else
			decision = "high";

	}

}
