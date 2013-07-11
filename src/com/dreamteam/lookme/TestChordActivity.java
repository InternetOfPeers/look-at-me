package com.dreamteam.lookme;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.chord.message.ChordMessage;
import com.dreamteam.lookme.communication.ILookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class TestChordActivity extends Activity implements OnClickListener {

	private EditText editName;
	private EditText editSurname;
	private TextView textBuddy;

	private ChordManager chord;
	private IChordChannel publicChannel;
	private IChordChannel privateChannel;

	private DBOpenHelper dbOpenHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_chord);
		dbOpenHelper = new DBOpenHelperImpl(this);
		editName = (EditText) findViewById(R.id.editName);
		editSurname = (EditText) findViewById(R.id.editSurname);
		textBuddy = (TextView) findViewById(R.id.textBuddy);

		Button buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(this);
		Button buttonStop = (Button) findViewById(R.id.buttonStop);
		buttonStop.setOnClickListener(this);
		Button buttonJoinPublic = (Button) findViewById(R.id.buttonJoinPublic);
		buttonJoinPublic.setOnClickListener(this);
		Button buttonJoinPrivate = (Button) findViewById(R.id.buttonJoinPrivate);
		buttonJoinPrivate.setOnClickListener(this);
		Button buttonSendAllPublic = (Button) findViewById(R.id.buttonSendAllPublic);
		buttonSendAllPublic.setOnClickListener(this);
		Button buttonSendAllPrivate = (Button) findViewById(R.id.buttonSendAllPrivate);
		buttonSendAllPrivate.setOnClickListener(this);

		chord = ChordManager.getInstance(this);
		Log.d("CHORDTEST", "chord != null ? " + (chord != null));
		// chord.setTempDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/Chord");
		chord.setHandleEventLooper(getMainLooper());
	}

	@Override
	public void onClick(View v) {
		try {
			Profile profile = dbOpenHelper.getProfile(0);
			String name = editName.getText().toString();
			Log.d("", "name is: " + name);
			profile.setName(name);
			String surname = editSurname.getText().toString();
			Log.d("", "surname is:" + surname);
			profile.setSurname(surname);

			Button button = (Button) v;
			switch (button.getId()) {
			case R.id.buttonStart:
				startChordTest();
				break;
			case R.id.buttonStop:
				stopChordTest();
				break;
			case R.id.buttonJoinPublic:
				joinPublicChannel();
				break;
			case R.id.buttonJoinPrivate:
				joinPrivateChannel();
				break;
			case R.id.buttonSendAllPublic:
				sendAllPublicChannel(profile);
				break;
			case R.id.buttonSendAllPrivate:
				sendAllPrivateChannel(profile);
				break;
			default:
				Log.d("CHORDTEST", "UNKNOWN");
				break;
			}
		} catch (Exception e) {
			Log.e("tesctCHORD", "error during onclick, error:" + e.getMessage());
		}

	}

	private void startChordTest() {
		Log.d("CHORDTEST", "STARTING");
		int startingResult = chord.start(ChordManager.INTERFACE_TYPE_WIFI,
				new IChordManagerListener() {
					@Override
					public void onStarted(String arg0, int arg1) {
						Log.d("CHORDTEST", "onStarted(" + arg0 + "," + arg1
								+ ")");
					}

					@Override
					public void onNetworkDisconnected() {
						Log.d("CHORDTEST", "onNetworkDisconnected()");
					}

					@Override
					public void onError(int arg0) {
						Log.d("CHORDTEST", "onError(" + arg0 + ")");
					}
				});
		switch (startingResult) {
		case ChordManager.ERROR_FAILED:
			Log.d("CHORDTEST", "ERROR_FAILED");
			break;
		case ChordManager.ERROR_INVALID_INTERFACE:
			Log.d("CHORDTEST", "ERROR_INVALID_INTERFACE");
			break;
		case ChordManager.ERROR_INVALID_PARAM:
			Log.d("CHORDTEST", "ERROR_INVALID_PARAM");
			break;
		case ChordManager.ERROR_INVALID_STATE:
			Log.d("CHORDTEST", "ERROR_INVALID_STATE");
			break;
		case ChordManager.ERROR_NONE:
			Log.d("CHORDTEST", "ERROR_NONE");
			break;
		}
	}

	private void stopChordTest() {
		Log.d("CHORDTEST", "STOPPING");
		chord.stop();
	}

	private void joinPublicChannel() {
		Log.d("CHORDTEST", "JOINING TO PUBLIC CHANNEL");
		publicChannel = chord.joinChannel(ChordManager.PUBLIC_CHANNEL,
				new IChordChannelListener() {
					@Override
					public void onNodeLeft(String arg0, String arg1) {
						Log.d("CHORDTEST", "onNodeLeft public");
					}

					@Override
					public void onNodeJoined(String arg0, String arg1) {
						Log.d("CHORDTEST", "onNodeJoined public");
					}

					@Override
					public void onFileWillReceive(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6) {
						Log.d("CHORDTEST", "onFileWillReceive public");
					}

					@Override
					public void onFileSent(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5) {
						Log.d("CHORDTEST", "onFileSent public");
					}

					@Override
					public void onFileReceived(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, String arg7) {
						Log.d("CHORDTEST", "onFileReceived public");
					}

					@Override
					public void onFileFailed(String arg0, String arg1,
							String arg2, String arg3, String arg4, int arg5) {
						Log.d("CHORDTEST", "onFileFailed public");
					}

					@Override
					public void onFileChunkSent(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, long arg7, long arg8) {
						Log.d("CHORDTEST", "onFileChunkSent public");
					}

					@Override
					public void onFileChunkReceived(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, long arg7) {
						Log.d("CHORDTEST", "onFileChunkReceived public");
					}

					@Override
					public void onDataReceived(String fromNode,
							String fromChannel, String payloadType,
							byte[][] payload) {
						Log.d("CHORDTEST", "onDataReceived public");
						ILookAtMeMessage message = ChordMessage
								.obtainChordMessage(payload[0], fromNode);
						Profile profile = (Profile) message
								.getObject(ILookAtMeMessage.PROFILE_KEY);
						textBuddy.setText("RECEIVED MESSAGE FROM "
								+ profile.getName() + " "
								+ profile.getSurname());
					}
				});
		Log.d("CHORDTEST", "public channel is " + publicChannel.getName());
	}

	private void joinPrivateChannel() {
		Log.d("CHORDTEST", "JOINING TO PRIVATE CHANNEL");
		privateChannel = chord.joinChannel("LookAtMePrivateChannel",
				new IChordChannelListener() {
					@Override
					public void onNodeLeft(String arg0, String arg1) {
						Log.d("CHORDTEST", "onNodeLeft private");
					}

					@Override
					public void onNodeJoined(String arg0, String arg1) {
						Log.d("CHORDTEST", "onNodeJoined private");
					}

					@Override
					public void onFileWillReceive(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6) {
						Log.d("CHORDTEST", "onFileWillReceive private");
					}

					@Override
					public void onFileSent(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5) {
						Log.d("CHORDTEST", "onFileSent private");
					}

					@Override
					public void onFileReceived(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, String arg7) {
						Log.d("CHORDTEST", "onFileReceived private");
					}

					@Override
					public void onFileFailed(String arg0, String arg1,
							String arg2, String arg3, String arg4, int arg5) {
						Log.d("CHORDTEST", "onFileFailed private");
					}

					@Override
					public void onFileChunkSent(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, long arg7, long arg8) {
						Log.d("CHORDTEST", "onFileChunkSent private");
					}

					@Override
					public void onFileChunkReceived(String arg0, String arg1,
							String arg2, String arg3, String arg4, String arg5,
							long arg6, long arg7) {
						Log.d("CHORDTEST", "onFileChunkReceived private");
					}

					@Override
					public void onDataReceived(String fromNode,
							String fromChannel, String payloadType,
							byte[][] payload) {
						Log.d("CHORDTEST", "onDataReceived private");
						ILookAtMeMessage message = ChordMessage
								.obtainChordMessage(payload[0], fromNode);

						Profile profile = (Profile) message
								.getObject(ILookAtMeMessage.PROFILE_KEY);
						textBuddy.setText("RECEIVED MESSAGE FROM "
								+ profile.getName() + " "
								+ profile.getSurname());
					}

				});
		Log.d("CHORDTEST", "private channel is " + privateChannel.getName());
	}

	private void sendAllPublicChannel(Profile profile) {
		Log.d("CHORDTEST", "SENDING DATA TO PUBLIC CHANNEL");
		ILookAtMeMessage message = profileToMessage(profile);
		byte[][] payload = new byte[1][];
		payload[0] = message.getBytes();
		publicChannel.sendDataToAll(ILookAtMeMessage.PROFILE_MESSAGE, payload);
	}

	private void sendAllPrivateChannel(Profile profile) {
		Log.d("CHORDTEST", "SENDING DATA TO PRIVATE CHANNEL");
		byte[][] payload = new byte[1][];
		ILookAtMeMessage message = profileToMessage(profile);
		payload[0] = message.getBytes();
		privateChannel.sendDataToAll(ILookAtMeMessage.PROFILE_MESSAGE, payload);
	}

	private ILookAtMeMessage profileToMessage(Profile profile) {
		ILookAtMeMessage message = ChordMessage
				.obtainMessage(LookAtMeMessageType.PREVIEW);
		message.putObject(ILookAtMeMessage.PROFILE_KEY, profile);
		return message;
	}
}
