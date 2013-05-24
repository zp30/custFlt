package com.example.customflashlight;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnClickListener {

	ImageButton ib;
	TextView state;
	View frontPage;
	private Boolean lightOn;
	private Boolean screenOn;
	private Camera mCamera;
	private WakeLock wakeLock;
	private boolean previewOn;
	Camera cam; 
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	private ListView list;
	private ArrayAdapter<String> adapter;
	private ViewRibbonMenu rbmView;
	private Button test;
	private ListView rbmListView;
	private ListView rbmListView2;
	private ArrayAdapter<String> adapt;
	private ArrayAdapter<String> adapter2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		disablePhoneSleep();
		initialize();
		gestureDetector = new GestureDetector(new MyGestureDetector());
//		torchOn();

//		ib.setOnClickListener(new View.OnClickListener() {
//
//			// @Override
//			public void onClick(View v) {
//				if (lightOn) {
//					state.setText("off");
//					 torchOff();
//				} else {
//					state.setText("on");
//					 torchOn();
//				}
//				
//
//			}
//		});
		
		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
				
			}
		};

		// Do this for each view added to the grid
		frontPage.setOnClickListener(MainActivity.this); 
		frontPage.setOnTouchListener(gestureListener);
		
		
		/** Menu **/
		rbmView = (ViewRibbonMenu) findViewById(R.id.ribbonMenuView1);

		/** Main ListView **/
		list = (ListView) findViewById(R.id.listView1);

		/** 
		 * This is an exmaple button 
		 * it calls "hideMenu()" after each click similiar to the Facebook or Google+ apps 
		 * **/
		test = (Button) findViewById(R.id.button);
		test.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				rbmView.hideMenu();
			}
		});

		/** This is the most important ListView, updating the main list in the Activity **/
		final String[] items_list = { "List1", "List2", "List3", "List4" };
		adapt = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items_list);
		rbmListView = (ListView) findViewById(R.id.rbm_listview);
		rbmListView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				switch (position)
				{
				case 0:
					final String[] items = { "A", "B", "C", "D" };
					adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
					list.setAdapter(adapter);
					break;

				case 1:
					final String[] items1 = { "1", "2", "3", "4" };
					adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items1);
					list.setAdapter(adapter);
					break;

				case 2:
					final String[] items2 = { "z", "x", "c", "v" };
					adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items2);
					list.setAdapter(adapter);
					break;

				case 3:
					final String[] items3 = { "Test1", "Test2", "Test3", "Test4" };
					adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items3);
					list.setAdapter(adapter);
					break;

				default:
					final String[] itemsd = { "A", "B", "C", "D" };
					adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, itemsd);
					list.setAdapter(adapter);
					break;
				}
				rbmView.hideMenu();
			}
		});
		rbmListView.setAdapter(adapt);


		/** This is the second ListView on the menu **/
		final String[] items_list2 = { "Notification1", "Notification2", "Notification3", "Notification4" };
		adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items_list2);
		rbmListView2 = (ListView) findViewById(R.id.rbm_listview2);
		rbmListView2.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Toast.makeText(MainActivity.this, items_list2[position], Toast.LENGTH_SHORT).show();
				rbmView.hideMenu();
			}
		});
		rbmListView2.setAdapter(adapter2);
		
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		ib = (ImageButton) findViewById(R.id.ibState);
		state = (TextView) findViewById(R.id.tvState);
		frontPage = (View) findViewById(R.id.front_page);
//		if (this.getPackageManager().hasSystemFeature(
//				PackageManager.FEATURE_CAMERA_FLASH)) {
//			lightOn = true;
//			screenOn = false;
//			state.setText("Cam");
//		} else {
//			lightOn = false;
//			screenOn = true;
//			state.setText("Screen");
//			Intent goScreen = new Intent("android.intent.action.SCREENLIGHT");
//			startActivity(goScreen);
//
//		}
	}

	private void disablePhoneSleep() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	private void torchOn() {
		lightOn = true;
		cam = Camera.open();
		Parameters p = cam.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		cam.setParameters(p);
		cam.startPreview();
	}

	private void torchOff() {
		lightOn = false;
		cam.stopPreview();
		cam.release();
	}

	@Override
	public void onPause() {
		super.onPause();

		finish();
		 torchOff();
	}

	@Override
	public void onStop() {
		super.onStop();
		 torchOff();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		 torchOff();
	}
	
	@Override
	public void onClick(View v) {
//		Filter f = (Filter) v.getTag();
//		FilterFullscreenActivity.show(this, input, f);
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(MainActivity.this, "Left Swipe",
							Toast.LENGTH_SHORT).show();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Toast.makeText(MainActivity.this, "Right Swipe",
							Toast.LENGTH_SHORT).show();
					
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			rbmView.hideMenu();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	/**
	 * Options Menu<br>
	 * example toggle
	 */
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.test:
			rbmView.toggleMenu();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
		

	// public void onStart() {
	// super.onStart();
	// Log.i(TAG, "onStart");
	// getCamera();
	// startPreview();
	// }
	// private void startPreview() {
	// if (!previewOn && mCamera != null) {
	// mCamera.startPreview();
	// previewOn = true;
	// }
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	// private void startWakeLock() {
	// if (wakeLock == null) {
	// Log.d(TAG, "wakeLock is null, getting a new WakeLock");
	// PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	// Log.d(TAG, "PowerManager acquired");
	// wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
	// Log.d(TAG, "WakeLock set");
	// }
	// wakeLock.acquire();
	// Log.d(TAG, "WakeLock acquired");
	// }
	//
	// private void stopWakeLock() {
	// if (wakeLock != null) {
	// wakeLock.release();
	// Log.d(TAG, "WakeLock released");
	// }
	// }
	//
	// private void getCamera() {
	// if (mCamera == null) {
	// try {
	// mCamera = Camera.open();
	// } catch (RuntimeException e) {
	// Log.e(TAG, "Camera.open() failed: " + e.getMessage());
	// }
	// }
	// }
	//
	// private void turnLightOn() {
	// if (mCamera == null) {
	// Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG);
	// // Use the screen as a flashlight (next best thing)
	// //button.setBackgroundColor(COLOR_WHITE);
	// return;
	// }
	// lightOn = true;
	// Parameters parameters = mCamera.getParameters();
	// if (parameters == null) {
	// // Use the screen as a flashlight (next best thing)
	// //button.setBackgroundColor(COLOR_WHITE);
	// return;
	// }
	// List<String> flashModes = parameters.getSupportedFlashModes();
	// // Check if camera flash exists
	// if (flashModes == null) {
	// // Use the screen as a flashlight (next best thing)
	// //button.setBackgroundColor(COLOR_WHITE);
	// return;
	// }
	// String flashMode = parameters.getFlashMode();
	// Log.i(TAG, "Flash mode: " + flashMode);
	// Log.i(TAG, "Flash modes: " + flashModes);
	// if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
	// // Turn on the flash
	// if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
	// parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
	// mCamera.setParameters(parameters);
	// //button.setBackgroundColor(COLOR_LIGHT);
	// startWakeLock();
	// } else {
	// Toast.makeText(this, "Flash mode (torch) not supported",
	// Toast.LENGTH_LONG);
	// // Use the screen as a flashlight (next best thing)
	// //button.setBackgroundColor(COLOR_WHITE);
	// Log.e(TAG, "FLASH_MODE_TORCH not supported");
	// }
	// }
	// }
	//
	// private void turnLightOff() {
	// if (lightOn) {
	// // set the background to dark
	// //button.setBackgroundColor(COLOR_DARK);
	// lightOn = false;
	// if (mCamera == null) {
	// return;
	// }
	// Parameters parameters = mCamera.getParameters();
	// if (parameters == null) {
	// return;
	// }
	// List<String> flashModes = parameters.getSupportedFlashModes();
	// String flashMode = parameters.getFlashMode();
	// // Check if camera flash exists
	// if (flashModes == null) {
	// return;
	// }
	// Log.i(TAG, "Flash mode: " + flashMode);
	// Log.i(TAG, "Flash modes: " + flashModes);
	// if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
	// // Turn off the flash
	// if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
	// parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
	// mCamera.setParameters(parameters);
	// stopWakeLock();
	// } else {
	// Log.e(TAG, "FLASH_MODE_OFF not supported");
	// }
	// }
	// }
	// }

}
