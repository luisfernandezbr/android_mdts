package br.com.mobiplus.multipledevicestest_choosebehavior;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		registerViewClicksListener();
	}

	private void registerViewClicksListener() {
		ViewGroup mainView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);

		applyOnClickRecursively(mainView);

	}

	private void applyOnClickRecursively(ViewGroup parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);

			System.out.println("CHILD_VIEWS: " + child.toString());

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

					AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {

							if (MotionEvent.ACTION_DOWN == event.getAction()) {
								System.out.println("CHILD_VIEWS: MotionEvent.ACTION_DOWN");
							} else if (MotionEvent.ACTION_UP == event.getAction()) {
								System.out.println("CHILD_VIEWS: MotionEvent.ACTION_UP");

								try {
									mSocket = new Socket("192.168.1.6", 8888);
									mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
									mDataOutputStream.writeUTF("VIEW_ID:" + v.getId());
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
					};
					
					async.execute();
					
					
					

					return false;
				}
			});
			// view.setOnLongClickListener(new OnLongClickListener() {
			//
			// @Override
			// public boolean onLongClick(View v) {
			//
			// try {
			// mSocket = new Socket("192.168.1.6", 8888);
			// mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
			// mDataOutputStream.writeUTF("VIEW_ID:" + v.getId());
			// } catch (UnknownHostException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// //Toast.makeText(mActivity.getApplicationContext(), "CLICADO!!!", Toast.LENGTH_LONG).show();
			// return false;
			// }
			// });
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
							int viewId = Integer.parseInt(serverContent.replace("VIEW_ID:", ""));
							
							if (viewId > 0) {
								View view = mActivity.findViewById(viewId);
								Toast.makeText(mActivity.getApplicationContext(), "clicou? " + view.performClick(), Toast.LENGTH_LONG);
							} else if (viewId == 0) {
								mActivity.finish();
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
	
	
}
