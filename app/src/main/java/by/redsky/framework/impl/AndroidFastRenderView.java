package by.redsky.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}

	public void resume() {
		this.running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	@Override
	public void run() {
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			game.getCurrentScreen().update(deltaTime);
			// Обновляем экран только тогда когда там действительно происходят
			// обновления...
			// Иначе сожрем батарею и нагреем процессор.
			if (game.getCurrentScreen().present(deltaTime)) {
				Canvas canvas = holder.lockCanvas();
				canvas.getClipBounds(dstRect);
				canvas.drawBitmap(framebuffer, null, dstRect, null);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void pause() {
		Log.d("lol", "Join thread.");
		running = false;
		// while (true) {
		try {
			Log.d("lol", "Join thread.1");
			renderThread.join();
			Log.d("lol", "Join thread.2");
		} catch (InterruptedException e) {

		}
		// }
	}
}
