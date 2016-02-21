package com.dcw.pingpong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	// GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// gameView = new GameView(this, GameView.TWO_PLAYER);
		// setContentView(gameView);

		setContentView(R.layout.main_screen);

		Typeface tf = Typeface.createFromAsset(getAssets(), "minecraftia.ttf");

		TextView tv = (TextView) findViewById(R.id.textView1);
		Button onePlayer = (Button) findViewById(R.id.bOne);
		Button twoPlayer = (Button) findViewById(R.id.bTwo);

		tv.setTypeface(tf);
		onePlayer.setTypeface(tf);
		twoPlayer.setTypeface(tf);
		onePlayer.setOnClickListener(this);
		twoPlayer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		Intent intent = new Intent("com.dcw.GAMEACTIVITY");
		switch(v.getId()){
		case R.id.bOne:
			bundle.putInt("players", GameView.ONE_PLAYER);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.bTwo:
			bundle.putInt("players", GameView.TWO_PLAYER);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
}
