package com.example.nanohttpd;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.example.nanohttpd.server.MainService;

public class MainActivity extends Activity {

	private SimpleServer server;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent service = new Intent(MainActivity.this, MainService.class);
		startService(service);
	}

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
