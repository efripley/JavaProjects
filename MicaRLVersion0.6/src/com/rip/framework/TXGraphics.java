package com.rip.framework;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import android.content.Context;

public class TXGraphics extends SurfaceView
{
	SurfaceHolder holder;
	Bitmap displayBuffer;

	public TXGraphics(Context context, Bitmap displayBuffer)
	{
		super(context);
		this.displayBuffer = displayBuffer;
		holder = getHolder();
	}

	public void draw()
	{
		Rect dstRect = new Rect();
		if(holder.getSurface().isValid())
		{				
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(displayBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
}