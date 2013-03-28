package br.com.mobiplus.multipledevicestest_choosebehavior;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	MDTMain mMdtMain;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		
		//if (mMdtMain == null) {
			mMdtMain = new MDTMain(this, "192.168.1.2", 8888);
		//}
		
		mMdtMain.start();
	}
	
//	public void onBackPressed() {
//		super.onBackPressed();
//		
//		if (mMdtMain == null) {
//			mMdtMain = new MDTMain(this, "192.168.1.6", 8888);
//		}
//		
//		mMdtMain.onBackPressed();
//	};
}
