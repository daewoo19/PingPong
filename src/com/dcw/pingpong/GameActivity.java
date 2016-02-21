package com.dcw.pingpong;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GameActivity extends Activity{

	GameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bundle = this.getIntent().getExtras();
		gameView = new GameView(this, bundle.getInt("players", 1));
		setContentView(gameView);
	}
}
