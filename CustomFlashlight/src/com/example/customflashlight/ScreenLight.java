package com.example.customflashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ScreenLight extends Activity implements View.OnClickListener {

	private View screen;

	private static final int COLOR_DARK = 0xCC000000;
	private static final int COLOR_LIGHT = 0xCCBFBFBF;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_light);
		
		screen = findViewById(R.id.vScreen);
		screen.setBackgroundColor(COLOR_WHITE);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
