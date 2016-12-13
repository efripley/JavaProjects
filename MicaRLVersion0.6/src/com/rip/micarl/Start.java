package com.rip.micarl;

import android.os.Bundle;
import android.app.Activity;

import com.rip.framework.Tilex;
import com.rip.framework.TilexTest;

public class Start extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Tilex.init(this, new Main(), 800, 480);

		TilexTest test = new TilexTest();
		test.fileWriter();
		test.fileReader();
		test.fileContents();
	}

	@Override
	protected void onResume()
	{
		Tilex.resume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		Tilex.pause();
		super.onPause();
	}
}