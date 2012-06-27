package com.headdetect.games.superpong.Activities.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.headdetect.games.superpong.Render.StarRenderer;

public class StarCanvas extends SurfaceView implements SurfaceHolder.Callback {
	public class StarThread extends Thread {

		private boolean mRun;
		private SurfaceHolder mSurfaceHolder;
		private StarRenderer mCanvas;

		public StarThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mCanvas = new StarRenderer();
		}

		@Override
		public void run() {
			mRun = true;
			while (mRun) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						doDraw(c);
					}
				}
				finally {
					if (c != null) {
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}

		void doDraw(Canvas canvas) {
			if (canvas == null)
				return;

			canvas.drawColor(Color.BLACK);
			mCanvas.update(canvas);
		}
	}

	private StarThread thread;

	public StarCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		thread = new StarThread(holder, context, new Handler());

		setFocusable(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		thread.start();
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		
	}


}