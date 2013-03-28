package br.com.mobiplus.multipledevicestest_choosebehavior;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class MDTMain extends Thread {

	private Activity mActivity;

	private Socket mSocket;
	private DataOutputStream mDataOutputStream;
	private DataInputStream mDataInputStream;

	private String mServerIp;
	private int mServerPort;

	public MDTMain(Activity activity, String serverIp, int serverPort) {
		super();
		mActivity = activity;
		mServerIp = serverIp;
		mServerPort = serverPort;

//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);

		registerViewClicksListener();
	}

	private void registerViewClicksListener() {
		ViewGroup mainView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);

		applyOnClickRecursively(mainView);

	}

	private void applyOnClickRecursively(ViewGroup parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);

			//System.out.println("CHILD_VIEWS: " + child.toString());

			if (child instanceof ViewGroup) {
				setOnClick(child);
				applyOnClickRecursively((ViewGroup) child);
			} else if (child != null) {
				setOnClick(child);
			}
		}
	}

//	public void onBackPressed() {
//		try {
//			mSocket = new Socket("192.168.1.6", 8888);
//			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
//			mDataOutputStream.writeUTF("VIEW_ID:0");
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	};
	
	private void setOnClick(View view) {
		if (view != null) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(final View v, final MotionEvent event) {

					
					AsyncTask<Void, Void, Void> async = new MyAsyncSender(event, v);
					async.execute();
					
					return false;
				}
			});
		}
	}

	@Override
	public void run() {
		super.run();

		try {
			mSocket = new Socket(mServerIp, mServerPort);
			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
			mDataInputStream = new DataInputStream(mSocket.getInputStream());
			mDataOutputStream.writeUTF("CONNECT...");

			while (true) {
				final String serverContent = mDataInputStream.readUTF();
				if (serverContent != null) {
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mActivity.getApplicationContext(), serverContent, Toast.LENGTH_LONG).show();
						}
					});

					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (serverContent.startsWith("VIEW_ID:")) {
								int viewId = Integer.parseInt(serverContent.replace("VIEW_ID:", ""));
								
								if (viewId > 0) {
									View view = mActivity.findViewById(viewId);
									Toast.makeText(mActivity.getApplicationContext(), "clicou? " + view.performClick(), Toast.LENGTH_LONG);
								}
//								} else if (viewId == 0) {
//									mActivity.finish();
//								}	
							} else if (serverContent.startsWith("EDIT:")) {
								String [] values = serverContent.split("ZYZY");
								
								int viewId = Integer.parseInt(values[1].replace("VIEW_ID:", ""));
								
								if (viewId > 0) {
									EditText view = (EditText) mActivity.findViewById(viewId);
									System.out.println("CHILD_VIEWS: EditText > " + view);
									System.out.println("CHILD_VIEWS: values > " + values[0] + " | " + values[1]);
									
									view.setText(values[0].replace("EDIT:", ""));
									Toast.makeText(mActivity.getApplicationContext(), "Texto a ser inserido: " + values[0].replace("EDIT:", ""), Toast.LENGTH_LONG);	
								}
							}
						}
					});

				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void afterTextChanged(EditText editText, Editable editable) {
		AsyncTask<Void, Void, Void> async = new MyEditAsyncSender(editable, editText);
		async.execute();
	}
	
	private static final int ACTION_VIEW_CLICK = 1;
	private static final int ACTION_EDIT_TEXT = 2;
	
	private class MyAsyncSender extends AsyncTask<Void, Void, Void> {
		private MotionEvent event;
		private View view;
		
		public MyAsyncSender(MotionEvent event, View view) {
			super();
			this.event = event;
			this.view = view;
		}


		@Override
		protected Void doInBackground(Void... params) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				System.out.println("CHILD_VIEWS: MotionEvent.ACTION_DOWN");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				System.out.println("CHILD_VIEWS: MotionEvent.ACTION_UP");

				try {
					mSocket = new Socket(mServerIp, mServerPort);
					mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
					mDataOutputStream.writeUTF("VIEW_ID:" + view.getId());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (MotionEvent.ACTION_MOVE == event.getAction()) {
				System.out.println("CHILD_VIEWS: MotionEvent.ACTION_MOVE");
			} else {
				System.out.println("CHILD_VIEWS: MotionEvent.ACTION_OTHER !!!");
			}
			return null;
		}
		
	}
	
	private class MyEditAsyncSender extends AsyncTask<Void, Void, Void> {
		private Editable editable;
		private View view;
		
		public MyEditAsyncSender(Editable editable, View view) {
			super();
			this.editable = editable;
			this.view = view;
		}


		@Override
		protected Void doInBackground(Void... params) {
			
				System.out.println("CHILD_VIEWS: Editable: " + editable);

				try {
					mSocket = new Socket(mServerIp, mServerPort);
					mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
					mDataOutputStream.writeUTF("EDIT:" + editable.toString() + "ZYZY" + "VIEW_ID:" + view.getId());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			return null;
		}
		
	}
	
}
