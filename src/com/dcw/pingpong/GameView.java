package com.dcw.pingpong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	public static final int ONE_PLAYER = 1;
	public static final int TWO_PLAYER = 2;
	public static final int POINTS_TO_WIN = 10;
	
	int numPlayers;
	private SurfaceHolder holder;
	private GameLoop gameLoop;
	Ball ball;
	Paddle paddle1;
	Paddle paddle2;
	private int p1Score = 0;
	private int p2Score = 0;
	private Paint text = new Paint();
	private Boolean resumed = false;
	private Boolean served = false;
	private Boolean gameOver = false;
	int height;
	int width;
	private int volley = 0;
	private Paint whitePaint;		


	public GameView(Context context, int numPlayers) {
		this(context);
		this.numPlayers = numPlayers;
	}

	public GameView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder arg0) {
				gameLoop.setRunning(false);
				Boolean retry = true;
				while (retry) {
					try {
						gameLoop.join();
						retry = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				gameLoop = null;
				resumed = true;
			}

			@Override
			public void surfaceCreated(SurfaceHolder arg0) {
				if (!resumed) {// first time this game runs, create ball and paddles
					ball = new Ball(GameView.this);
					paddle1 = new Paddle(GameView.this, 1);
					paddle2 = new Paddle(GameView.this, 2);
				}
				gameLoop = new GameLoop(GameView.this);
				gameLoop.setRunning(true);
				gameLoop.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});

		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"minecraftia.ttf");
		text.setColor(Color.WHITE);
		text.setTextAlign(Align.CENTER);
		text.setTextSize(50);
		text.setTypeface(font);
		
		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
	}

	private void update() {
		if (served) {
			if (numPlayers == ONE_PLAYER) {
				paddle2.cpuMove();
			}
			ball.move();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		update();
		
		canvas.drawColor(Color.BLACK);
		float[] lines = {
				0,0,0,height,
				0,0,width,0,
				width-1,0,width-1,height,
				width,height-1,0,height-1,
				width/2,0,width/2,height
		} ;
		canvas.drawLines(lines, whitePaint);
		
		ball.draw(canvas);
		paddle1.draw(canvas);
		paddle2.draw(canvas);
		if (!served && !gameOver) {
			canvas.drawText("Tap to launch ball", width / 2, height / 2, text);
		}
		if (p1Score >=POINTS_TO_WIN){
			canvas.drawText("Player 1 Wins!", width / 2, height / 2, text);
		} else if(p2Score >=POINTS_TO_WIN ){
			canvas.drawText("Player 2 Wins!", width / 2, height / 2, text);
		}
		
		//shows scores
		canvas.drawText(Integer.valueOf(p1Score).toString(), (width * 3 / 8),
				height / 10, text);
		canvas.drawText(Integer.valueOf(p2Score).toString(), (width * 5 / 8),
				height / 10, text);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//game over, quit to main menu
		if ((event.getActionMasked() == MotionEvent.ACTION_DOWN ||  
				event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) && gameOver){ 
		    ((Activity) getContext()).finish();
		}
		
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)
			served = true;

		//1 player game
		if (numPlayers == ONE_PLAYER) {
			paddle1.move((int) event.getY());
			return true;
		}

		//2 player game
		int num = event.getPointerCount();
		for (int i = 0; i < num; i++) {

			int x = (int) event.getX(i);
			int y = (int) event.getY(i);

			if (numPlayers == TWO_PLAYER) {
				if (x < width / 2)
					paddle1.move((int) y);
				else if (x > width / 2)
					paddle2.move((int) y);
			}
		}
		return true;
	}

	public void p1Scored() {
		p1Score++;
		newVolley();
	}

	public void p2Scored() {
		p2Score++;
		newVolley();
	}

	public void newVolley() {
		if (p1Score >= POINTS_TO_WIN || p2Score >= POINTS_TO_WIN){
			gameOver = true;
		}
		volley++;
		
		//increase ball speed across the map on the 3rd and 7th volleys
		if (volley == 3) {
			ball.increaseXVelo(1.3);
		} else if (volley == 7) {
			ball.increaseXVelo(1.5);
		}
		served = false;
	}
}
