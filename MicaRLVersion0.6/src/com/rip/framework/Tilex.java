package com.rip.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;

import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;

import com.rip.framework.Tilex.TXKey;
import com.rip.framework.Tilex.TXColor;

public final class Tilex implements OnTouchListener
{
	private class TXTile
	{
		public int value;
		public TXColor color;
		public int size;
		public float offsetX;
		public float offsetY;
		public TXTile()
		{
			value = 0;
			color = WHITE;
			size = 1;
			offsetX = 0;
			offsetY = 0;
		}
	}

	private class TXLayer
	{
		public float offsetX;
		public float offsetY;
		public boolean changed;
		public int width;
		public int height;
		public int cellSize;
		public TXTile[][] buffer;

		public TXLayer(int width, int height, int cellSize)
		{
			offsetX = 0;
			offsetY = 0;
			changed = false;
			this.width = width;
			this.height = height;
			this.cellSize = cellSize;

			buffer = new TXTile[width][height];

			for(int ay = 0; ay < height; ay++)
				for(int ax = 0; ax < width; ax++)
					buffer[ax][ay] = new TXTile();
		}
	}

	public static class TXButton
    {
        public int x;
        public int y;
        public int w;
        public int h;
        public int centerX;
        public int centerY;
        public int cellSize;
        public int size;
        public int index;
        public int imageId;

        public TXButton(int imageId, int x, int y, int index, int size)
        {
        	this.cellSize = Tilex.getCellSize(index);
            this.x = x;
            this.y = y;
            this.centerX = x + (cellSize * size / 2);
            this.centerY = y + (cellSize * size / 2);
            this.w = cellSize * size;
            this.h = cellSize * size;
            Log.d("TXButton", "(" + this.x + "," + this.y + ")" + this.w + "," + this.h);
            this.size = size;
            this.index = index;
            this.imageId = imageId;
        }
 
        public boolean clicked(TXKey key)
        {
        	int pixX = x * cellSize;
        	int pixY = y * cellSize;
            if(key.type == TXKey.UP)
                return(key.x >= pixX && key.x < pixX + w && key.y >= pixY && key.y < pixY + h);
            else
                return false;
        }

        public boolean held(TXKey key)
        {
        	int pixX = x * cellSize;
        	int pixY = y * cellSize;
            if(key.type == TXKey.DOWN || key.type == TXKey.MOTION)
                return(key.x >= pixX && key.x < pixX + w && key.y >= pixY && key.y < pixY + h);
            else
                return false;
        }

        public void draw()
        {
            Tilex.draw(imageId, x, y, index, WHITE, size);
        }
    }

	public static class TXColor
	{
		public int r;
		public int g;
		public int b;

		public TXColor (int r, int g, int b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	public static class TXKey
	{
		public static final int NONE = 0;
		public static final int DOWN = 1;
		public static final int MOTION = 2;
		public static final int UP = 3;

		public int id;
		public int x;
		public int y;
		public int type;
		public boolean used;

		public TXKey()
		{
			id = -1;
			x = -1;
			y = -1;
			type = NONE;
			used = true;
		}
	}

	public static final TXColor WHITE = new TXColor(255, 255, 255);
	public static final TXColor LIGHT_GRAY = new TXColor(191, 191, 191);
	public static final TXColor GRAY = new TXColor(127, 127, 127);
	public static final TXColor DARK_GRAY = new TXColor(63, 63, 63);
	public static final TXColor BLACK = new TXColor(0, 0, 0);
	public static final TXColor RED = new TXColor(255, 0, 0);
	public static final TXColor GREEN = new TXColor(0, 255, 0);
	public static final TXColor BLUE = new TXColor(0, 0, 255);

	private static Tilex tx = new Tilex();

	private Activity activity;
	private WakeLock wakeLock;
	private TXGraphics txGraphics;
	private TXLoop txLoop;
	private BufferedWriter fileWriter;
	private BufferedReader fileReader;
	private Canvas displayBuffer;
	private TXLayer layer[] = new TXLayer[8];
	private Bitmap[] tileSet = new Bitmap[8];
	private Tilex.TXKey[] key = new TXKey[10];
	private int width;
	private int height;
	private int cellSize;
	private float scaleX;
	private float scaleY;

	public Tilex()
	{
		//to prevent any other class from creating an instance
	}

	private void txInit(Activity activity, TXRoutine routine, int width , int height)
	{
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.activity = activity;
		this.width = width;
		this.height = height;
		this.scaleX = (float)width / activity.getWindowManager().getDefaultDisplay().getWidth();
		this.scaleY = (float)height / activity.getWindowManager().getDefaultDisplay().getHeight();
		
		Bitmap buffer = Bitmap.createBitmap(width, height, Config.RGB_565);
		displayBuffer = new Canvas(buffer);

		txGraphics = new TXGraphics(activity, buffer);
		txGraphics.setOnTouchListener(this);

		txLoop = new TXLoop(routine);

		PowerManager powerManager = (PowerManager)activity.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MicaRL");

		for(int a = 0; a < 10; a++)
			key[a] = new TXKey();

		for(int a = 0; a < 8; a++)
			layer[a] = new TXLayer(0, 0, 0);

		activity.setContentView(txGraphics);
	}

	private void txResume()
	{
		txLoop.resume();
		wakeLock.acquire();
	}

	private void txPause()
	{
		txLoop.pause();
		wakeLock.release();
	}

	private int txNewSheet(String file, int index)
	{
		if(index >= 0 && index < 8)
		{
			tileSet[index] = loadImage(file);
			return 0;
		}
		return -1;
	}

	private int txNewLayer(int index, int width, int height, int cellSize)
	{
		if(index >= 0 && index < 8)
		{
			layer[index] = new TXLayer(width, height, cellSize);
			return 0;
		}
		return -1;
	}

	private int txSetOffset(int index, float oX, float oY)
	{
		if(index >= 0 && index < 8)
		{
			layer[index].offsetX = oX;
			layer[index].offsetY = oY;
			return 0;
		}
		return -1;
	}

	private int txSetOffset(int x, int y, int index, float oX, float oY)
	{
		if(index >= 0 && index < 8)
		{
			layer[index].buffer[x][y].offsetX = oX;
			layer[index].buffer[x][y].offsetY = oY;
			return 0;
		}
		return -1;
	}

	private static int getCellSize(int index)
	{
		if(index >= 0 && index < 8)
		{
			return tx.layer[index].cellSize;
		}
		else return -1;
	}

	private Bitmap loadImage(String file)
	{
		try
		{
			Bitmap returnImage;
			AssetManager assetManager = activity.getAssets();
			InputStream indata = assetManager.open(file);
			returnImage = BitmapFactory.decodeStream(indata);
			indata.close();
			return returnImage;
		}
		catch(IOException e)
		{

		}
		return null;
	}

	private void txClear()
	{
		displayBuffer.drawRGB(0, 0, 0);
		for(int index = 0; index < 8; index++)
		{
			layer[index].changed = false;
			layer[index].offsetX = 0;
			layer[index].offsetY = 0;
			for(int ay = 0; ay < layer[index].height; ay++)
			{
				for(int ax = 0; ax < layer[index].width; ax++)
				{
					layer[index].buffer[ax][ay].value = 0;
					layer[index].buffer[ax][ay].color = WHITE;
					layer[index].buffer[ax][ay].size = 1;
					layer[index].buffer[ax][ay].offsetX = 0;
					layer[index].buffer[ax][ay].offsetY = 0;
				}
			}
		}
	}

	private void txDraw(int variable, int x, int y, int index, TXColor color, int size)
	{
		if(index >= 0 && index < 8)
		{
			layer[index].buffer[x][y].value = variable;
			layer[index].buffer[x][y].color = color;
			layer[index].buffer[x][y].size = size;
			layer[index].changed = true;
		}
	}

	private void txPrint(String variable, int x, int y, int index, TXColor color)
	{
		if(index >= 0 && index < 8)
		{
			for(int a = 0; a < variable.length(); a++)
			{
				if(x + a >= 0 && x + a < layer[index].width && y >= 0 && y < layer[index].height)
				{
					layer[index].buffer[x + a][y].value = (int)variable.charAt(a);
					layer[index].buffer[x + a][y].color = color;
				}
			}
			layer[index].changed = true;
		}
	}

	private void txFlip()
	{
		Paint paint = new Paint();
		Rect from = new Rect();
		Rect to = new Rect();
		int baseX = 0;
		int baseY = 0;
		
		for(int index = 0; index < 8; index++)
		{
			if(layer[index].changed)
			{
				from.left = 0;
				from.top = 0;
				from.right = from.left + 16;
				from.bottom = from.bottom + 16;

				baseX = (int)(layer[index].offsetX * layer[index].cellSize);
				baseY = (int)(layer[index].offsetY * layer[index].cellSize);
				to.right = to.left + layer[index].cellSize;
				to.bottom = to.bottom + layer[index].cellSize;

				for(int ay = 0; ay < layer[index].height; ay++)
				{
					for(int ax = 0; ax < layer[index].width; ax++)
					{
						if(layer[index].buffer[ax][ay].value > 0)
						{
							int texIndex = layer[index].buffer[ax][ay].value / 256;
							if(texIndex >= 8)
								texIndex = 0;
							to.left = baseX + (int)(layer[index].buffer[ax][ay].offsetX * layer[index].cellSize);
							to.top = baseY + (int)(layer[index].buffer[ax][ay].offsetY * layer[index].cellSize);
							to.right = to.left + (layer[index].cellSize * layer[index].buffer[ax][ay].size);
							to.bottom = to.top + (layer[index].cellSize * layer[index].buffer[ax][ay].size);
							
							from.left = (layer[index].buffer[ax][ay].value % 16) * 16;
							from.top = ((layer[index].buffer[ax][ay].value % 256) / 16) * 16;
							from.right = from.left + 16;
							from.bottom = from.top + 16;

							if(layer[index].buffer[ax][ay].color != WHITE)
							{
								paint.setARGB(255, layer[index].buffer[ax][ay].color.r, layer[index].buffer[ax][ay].color.g, layer[index].buffer[ax][ay].color.b);
								ColorFilter filter = new LightingColorFilter(paint.getColor(), 1);
								paint.setColorFilter(filter);
								displayBuffer.drawBitmap(tileSet[texIndex], from, to, paint);
							}
							else 
								displayBuffer.drawBitmap(tileSet[texIndex], from, to, null);
						}
						baseX += layer[index].cellSize;
					}
					baseX = (int)(layer[index].offsetX * layer[index].cellSize);
					baseY += layer[index].cellSize;
				}
			}
		}
		txGraphics.draw();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;;
		int pointerCount = event.getPointerCount();
		for(int a = 0; a < 10; a++)
		{
			if(a >= pointerCount)
			{
				key[a].type = TXKey.NONE;
				key[a].id = -1;
				continue;
			}

			if(event.getAction() != MotionEvent.ACTION_MOVE && a != pointerIndex)
			{
				continue;
			}

			int pointerId = event.getPointerId(a);
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				key[a].type = TXKey.DOWN;
				key[a].id = pointerId;
				key[a].x = (int)(event.getX(a) * scaleX);
				key[a].y = (int)(event.getY(a) * scaleY);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
				key[a].type = TXKey.UP;
				key[a].id = -1;
				key[a].x = (int)(event.getX(a) * scaleX);
				key[a].y = (int)(event.getY(a) * scaleY);
				break;
			case MotionEvent.ACTION_MOVE:
				key[a].type = TXKey.MOTION;
				key[a].id = pointerId;
				key[a].x = (int)(event.getX(a) * scaleY);
				key[a].y = (int)(event.getY(a) * scaleY);
				break;
			}
		}
		return true;
	}

	private TXKey txGetInput(int index)
	{
		TXKey returnKey = new TXKey();
		returnKey.type = key[index].type;
		returnKey.x = key[index].x;
		returnKey.y = key[index].y;
		returnKey.id = key[index].id;
		if(key[index].type == TXKey.UP)
			key[index].type = TXKey.NONE;
		return returnKey;
	}

	private void txFinish()
	{
		activity.finish();
	}

	private boolean txOpenWriter(String file)
	{
		String state = Environment.getExternalStorageState();
		if(!state.equals(Environment.MEDIA_MOUNTED))
		{
			Log.d("MicaRL", "Storage not mounted");
			return false;
		}
		else
		{
			try
			{
				File extDir = Environment.getExternalStorageDirectory();
				File textFile = new File(extDir.getAbsolutePath() + File.separator + file);
				fileWriter = new BufferedWriter(new FileWriter(textFile));
				return true;
			}
			catch(IOException e)
			{
				Log.d("MicaRL", "IOException");
				return false;
			}
		}
	}

	private boolean txWrite(String variable)
	{
		try
		{
			if(fileWriter != null)
			{
				fileWriter.write(variable);
				return true;
			}
			else
				return false;
		}
		catch(IOException e)
		{
			return false;
		}
	}

	private void txCloseWriter()
	{
		try
		{
			fileWriter.close();
			fileWriter = null;
		}
		catch(IOException e)
		{
		}
	}

	private boolean txOpenReader(String file)
	{
		String state = Environment.getExternalStorageState();
		if(!state.equals(Environment.MEDIA_MOUNTED))
		{
			return false;
		}
		else
		{
			try
			{
				File extDir = Environment.getExternalStorageDirectory();
				File textFile = new File(extDir.getAbsolutePath() + File.separator + file);
				fileReader = new BufferedReader(new FileReader(textFile));
				return true;
			}
			catch(IOException e)
			{
				return false;
			}
		}
	}

	private String txRead()
	{
		StringBuilder returnString = new StringBuilder();
		int indata;
		char currentChar = 0;
		boolean found = false;
		try
		{
			if(fileReader != null)
			{
				while(true)
				{
					indata = fileReader.read();
					if(indata != -1)
						currentChar = (char)indata;
					if((currentChar == ' ' || currentChar == '\n' || indata == -1) && found)
					{
						return returnString.toString();
					}
					else if(indata == -1 && !found)
					{
						return "eof";
					}
					else if(currentChar == ' ' || currentChar == '\n' && !found)
					{
						continue;
					}
					else
					{
						found = true;
						returnString.append(currentChar);
					}
				}
			}
		}
		catch(IOException e)
		{
		}
		return "exception";
	}

	private void txCloseReader()
	{
		try
		{
			fileReader.close();
			fileReader = null;
		}
		catch(IOException e)
		{
		}
	}

	private boolean txDeleteFile(String file)
	{
		String state = Environment.getExternalStorageState();
		if(!state.equals(Environment.MEDIA_MOUNTED))
		{
			return false;
		}
		else
		{
			File extDir = Environment.getExternalStorageDirectory();
			File textFile = new File(extDir.getAbsolutePath() + File.separator + file);
			textFile.delete();
			return true;
		}
	}

	public static void init(Activity activity, TXRoutine routine, int width, int height)
	{
		tx.txInit(activity, routine, width, height);
	}

	public static void resume()
	{
		tx.txResume();
	}

	public static void pause()
	{
		tx.txPause();
	}

	public static int newSheet(String file, int index)
	{
		return tx.txNewSheet(file, index);
	} 

	public static int newLayer(int index, int width, int height, int cellSize)
	{
		return tx.txNewLayer(index, width, height, cellSize);
	}

	public static int setOffset(int index, float oX, float oY)
	{
		return tx.txSetOffset(index, oX, oY);
	}

	public static int setOffset(int x, int y, int index, float oX, float oY)
	{
		return tx.txSetOffset(x, y, index, oX, oY);
	}

	public static void clear()
	{
		tx.txClear();
	}

	public static void draw(int variable, int x, int y, int index, TXColor color, int size)
	{
		tx.txDraw(variable, x, y, index, color, size);
	}

	public static void print(String variable, int x, int y, int index, TXColor color)
	{
		tx.txPrint(variable, x, y, index, color);
	}

	public static void print(int variable, int x, int y, int index, TXColor color)
	{
		tx.txPrint(Integer.toString(variable), x, y, index, color);
	}

	public static void print(double variable, int x, int y, int index, TXColor color)
	{
		StringBuilder number = new StringBuilder();
		if(variable < 0)
		{
			number.append("-");
			variable *= -1;
		}
		int intVar = (int)variable;
		double decVar = (variable - intVar);
		decVar *= 100;
		number.append(Integer.toString(intVar));
		number.append(".");
		if(decVar < 10)
			number.append("0");
		number.append(Integer.toString((int)decVar));
		tx.txPrint(number.toString(), x, y, index, color);
	}

	public static void flip()
	{
		tx.txFlip();
	}

	public static TXKey getInput(int index)
	{
		return tx.txGetInput(index);
	}

	public static void finish()
	{
		tx.txFinish();
	}

	public static boolean openWriter(String file)
	{
		return tx.txOpenWriter(file);
	}

	public static boolean write(String variable)
	{
		return tx.txWrite(variable);
	}

	public static boolean write(int variable)
	{
		return tx.txWrite(Integer.toString(variable));
	}

	public static boolean write(float variable)
	{
		return tx.txWrite(Float.toString(variable));
	}

	public static boolean write(boolean variable)
	{
		if(variable)
			return tx.txWrite("1");
		else
			return tx.txWrite("0");
	}

	public static void closeWriter()
	{
		tx.txCloseWriter();
	}

	public static boolean openReader(String file)
	{
		return tx.txOpenReader(file);
	}

	public static String readString()
	{
		return tx.txRead();
	}

	public static int readInt()
	{
		return Integer.parseInt(tx.txRead());
	}

	public static float readFloat()
	{
		return Float.valueOf(tx.txRead());
	}

	public static boolean readBoolean()
	{
		String temp = tx.txRead();
		if(temp == "0")
			return false;
		else
			return true;
	}

	public static void closeReader()
	{
		tx.txCloseReader();
	}

	public static boolean deleteFile(String file)
	{
		return tx.txDeleteFile(file);
	}
}