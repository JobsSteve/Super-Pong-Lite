package com.headdetect.games.superpong.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.headdetect.games.superpong.R;

public class MainActivity extends Activity implements OnClickListener {
	
	public void onCreate(Bundle snow){
		super.onCreate(snow);
		this.setContentView(R.layout.activity_main);
		
		((Button)findViewById(R.id.btnPlay)).setOnClickListener(this);
		((Button)findViewById(R.id.btnHelp)).setOnClickListener(this);
		((Button)findViewById(R.id.btnOnline)).setOnClickListener(this);
		((Button)findViewById(R.id.btnOptions)).setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View view) {
		if(view.equals(findViewById(R.id.btnPlay))){
			
			Intent myIntent = new Intent(MainActivity.this, SinglePlayerPongActivity.class);
			startActivityForResult(myIntent, 0);
			return;
		}
		
		Toast.makeText(this, "To be added soon", Toast.LENGTH_LONG).show();
		
	}
	
}
