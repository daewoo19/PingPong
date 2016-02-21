package com.dcw.pingpong;

import android.graphics.Canvas;

public class GameLoop extends Thread {

	final static long FPS = 30;
	private Boolean running = false;
	private GameView gameView;

	public GameLoop(GameView gv) {
		gameView = gv;
	}

	public void setRunning(Boolean run) {
		running = run;
	}

	@Override
	public void run() {
		long ticksPS = 1000 / FPS; // ticksPS is the minimum time each loop must
									// last
		long startTime;
		long sleepTime;
		while (running) {
			Canvas canvas = null;
			startTime = System.currentTimeMillis();
			try {
				canvas = gameView.getHolder().lockCanvas();
				gameView.onDraw(canvas);

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (canvas != null) {
					gameView.getHolder().unlockCanvasAndPost(canvas);
				}
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {
			}
		}
	}
}
