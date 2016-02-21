package com.dcw.pingpong;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

public class Ball {

	private int diameter;
	private int x;
	private int y;
	private int xVelo;
	private int yVelo;
	private GameView gameView;
	private int startVelo;
	private SoundPool sp;
	private int wallbounce;
	private int paddleCollision;
	private Paint paint;

	public Ball(GameView gv) {
		gameView = gv;
		paint = new Paint();
		diameter = gameView.getHeight() / 50;
		startVelo = (int) (diameter * 1.20);
		xVelo = startVelo;
		reset();

		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		wallbounce = sp.load(gameView.getContext(), R.raw.wallbounce, 1);
		paddleCollision = sp.load(gameView.getContext(), R.raw.collision, 1);
	}

	private void reset() {
		Random rand = new Random(System.currentTimeMillis());

		y = rand.nextInt(gameView.getHeight() - diameter * 3 / 2) + diameter
				/ 2;
		x = gameView.getWidth() / 2;

		if (rand.nextBoolean())
			yVelo = startVelo;
		else
			yVelo = -startVelo;
	}

	public int getStartVelo() {
		return startVelo;
	}

	public void move() {
		if (y + diameter / 2 + yVelo > gameView.getHeight()
				|| y + yVelo - diameter / 2 < 0) {
			yVelo = -yVelo;
			playSound(wallbounce);

		} else if (x + diameter / 2 + xVelo > gameView.getWidth()) {
			gameView.p1Scored();
			playSound(wallbounce);
			reset();
			return;
		} else if (x + xVelo - diameter / 2 < 0) {
			gameView.p2Scored();
			playSound(wallbounce);
			reset();
			return;
		}
		
		x += xVelo;
		y += yVelo;
		
		if (collides(gameView.paddle1)) {
			// we need to make sure to check for collision in only one direction
			// otherwise the ball could glitch out
			if (xVelo < 0) {// ball moving left
				changeBallBounceAngel(gameView.paddle1);
				// so the ball doesn't animate halfway into the paddle
				x = gameView.paddle1.getPaddleTop() + diameter / 2;
				playSound(paddleCollision);
			}
		} else if (collides(gameView.paddle2)) {
			if (xVelo > 0) {// ball moving right
				changeBallBounceAngel(gameView.paddle2);
				x = gameView.paddle2.getPaddleTop() - diameter / 2;
				playSound(paddleCollision);
			}
		}

		
	}

	public void playSound(int sound) {
		if (wallbounce != 0) {
			sp.play(sound, 1, 1, 0, 0, 1);
		}
	}

	public int getBallPos() {
		return y;
	}

	public Rect getRect() {
		return new Rect(x - diameter / 2, y - diameter / 2, x + diameter / 2, y
				+ diameter / 2);
	}

	public Boolean collides(Paddle paddle) {
		return getRect().intersect(paddle.getRect());
	}

	public void changeBallBounceAngel(Paddle paddle) {
		yVelo = (int) (2.5 * startVelo * ((double) y - paddle.getPos()) / ((double) paddle
				.getHeight() / 2));
		xVelo *= -1;
	}

	public void increaseXVelo(double speed) {
		xVelo = (int) (xVelo * speed);
	}

	public void draw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		canvas.drawRect(getRect(), paint);
	}
}