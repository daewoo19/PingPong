package com.dcw.pingpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Paddle {

	private int height;
	private int width;
	private GameView gameView;
	private int x;
	private int y;
	private int player;
	Paint paint;

	public Paddle(GameView gv, int player) {
		gameView = gv;
		paint = new Paint();
		height = gameView.getHeight() / 6;
		width = gameView.getHeight() / 50;
		
		this.player = player;
		// position the paddles at the center of the screen
		if (player == 1)
			x = 10 * width;
		else if (player == 2)
			x = gameView.getWidth() - 11 * width;

		y = gameView.getHeight() / 2;
	}

	public void move(int yPos) {
		if (yPos - height / 2 < 0)
			yPos = height / 2;
		if (yPos + height / 2 > gameView.getHeight())
			yPos = gameView.getHeight() - height / 2;
		y = yPos;
	}

	public void cpuMove() {
		int ballPos = gameView.ball.getBallPos();
		
		// paddle can only move as fast as the balls starting velocity
		int ballStartVelo = gameView.ball.getStartVelo(); 

		if (ballPos > y + height / 4) {//if ball is above paddle hit zone
			//move the paddle at the starting speed of ball or
			//the distance from the ball, whichever is smallest
			move(Math.min((int)(y + ballStartVelo*1.2), y + (ballPos - (y + height/4)))); 
		}
		if (ballPos < y - height / 4) {//if ball is below paddle
			move(Math.max((int)(y - ballStartVelo*1.2), y + (ballPos - (y - height/4))));
		}
	}
	
	public int getPaddleTop(){
		if (player == 1)
			return x + width;
		else if (player == 2)
			return x;
		return 0;
	}
	public Rect getRect() {
		return new Rect(x, y - height / 2, x + width, y + height / 2);
	}

	public int getHeight(){
		return height;
	}
	
	public int getPos() {
		return y;
	}

	public void draw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		canvas.drawRect(getRect(), paint);
	}
}
