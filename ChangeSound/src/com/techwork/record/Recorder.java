package com.techwork.record;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class Recorder implements RecorderInterface{
	private String file_exts[] = { 
			RecordConstant.AUDIO_RECORDER_FILE_EXT_MP4,
			RecordConstant.AUDIO_RECORDER_FILE_EXT_3GP 
		};
	public String record(String fileName, int freq, int format, boolean recording){
		fileName = new String(RecordUtility.getFileName(file_exts[format]));
		File file = new File(fileName);
		int sampleFreq = freq;

		try {
			file.createNewFile();

			OutputStream outputStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					outputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(
					bufferedOutputStream);

			@SuppressWarnings("deprecation")
			int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);

			short[] audioData = new short[minBufferSize];

			@SuppressWarnings("deprecation")
			AudioRecord audioRecord = new AudioRecord(
					MediaRecorder.AudioSource.MIC, //determine recorder
					sampleFreq,//determine frequency
					AudioFormat.CHANNEL_CONFIGURATION_MONO,//determine channel config
					AudioFormat.ENCODING_PCM_16BIT, //determine type of encode
					minBufferSize//determine buffer size
					);

			audioRecord.startRecording();
			// write recorded tune
			while (recording) {
				int numberOfShort = audioRecord.read(audioData, 0,
						minBufferSize);
				for (int i = 0; i < numberOfShort; i++) {
					dataOutputStream.writeShort(audioData[i]);
				}
			}

			audioRecord.stop();
			dataOutputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	public void playRecord(String mFileName, int freq){
		File file = new File(mFileName);
		//change unit of record tune
		int shortSizeInBytes = Short.SIZE / Byte.SIZE;
		int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);
		short[] audioData = new short[bufferSizeInBytes];

		try {
			InputStream inputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					inputStream);
			DataInputStream dataInputStream = new DataInputStream(
					bufferedInputStream);
			
			int i = 0;
			while (dataInputStream.available() > 0) {
				audioData[i] = dataInputStream.readShort();
				i++;
			}

			dataInputStream.close();

			int sampleFreq = freq;//(Integer) spFrequency.getSelectedItem();

			@SuppressWarnings("deprecation")
			AudioTrack audioTrack = new AudioTrack(
					AudioManager.STREAM_MUSIC,
					sampleFreq, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, 
					bufferSizeInBytes,
					AudioTrack.MODE_STREAM);
			audioTrack.play();
			audioTrack.write(audioData, 0, bufferSizeInBytes);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
