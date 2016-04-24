package com.lol.contacts.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.lol.contacts.utils.ScreenAdapterUtils;

public class HeartView extends View {

	private Paint mPaint;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private BitmapShader mBitmapShader;
	private int height;
	private int width;
	private float mScreenDesity;

	public HeartView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// mPaint = new Paint();

		// mBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.chat_icon);
		//
		// mBitmapShader = new BitmapShader(mBitmap, TileMode.REPEAT,
		// TileMode.REPEAT);
		// mPaint.setShader(mBitmapShader);

		mPaint = new Paint();
		mPaint.setARGB(0x99,0xFF,0x61,0x00);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(5f);
		mScreenDesity = ScreenAdapterUtils.getScreenDesity(getContext());
		int bitmapWidth = (int) (mScreenDesity * 170);
		mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapWidth, Bitmap.Config.ARGB_4444); // ����һ��bitmap
		mCanvas = new Canvas(mBitmap);

		new Thread() {
			@Override
			public void run() {
				super.run();
				drawLove(mCanvas);

			}
		}.start();

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		matrix.postScale(0.65f,0.65f);
		canvas.drawBitmap(mBitmap,matrix, mPaint);

	}// end onDraw

	private void drawLove(Canvas canvas) {
		// (17*(x^2))-(16*abs(x)*y)+(17*(y^2))<255 x(-5,5) y(-5,5) (���κ�������ʽ)


		int loveWidth = (int)(mScreenDesity * 60);// ���Ϳ�ȣ�������ż��
		int oneLine = loveWidth / 2;// һ���᳤��

		float scale = oneLine / 5f;// ʵ��������Ϸ���ʽ���꣬����

		for (int i = 0; i < oneLine; i++) {
			for (int j = 0; j < oneLine; j++) {

				// ���ݱ��ʽxy�ķ�Χ������Ҫ������ϵ�ķ�ΧҲ��С
				float xf = i / scale;
				float yf = j / scale;

				if ((17 * Math.pow(xf, 2) - 16 * Math.abs(xf) * yf + 17 * Math
						.pow(yf, 2)) < 255) {

					// ����1
					// canvas.drawPoint(xf*scale+250,250+yf*scale, paint);
					// ����2
					canvas.drawPoint(250 - xf * scale, 250 - yf * scale, mPaint);
					// this.postInvalidateDelayed(10);

					// ����1
					// canvas.drawPoint(-xf*scale+250,250+yf*scale, paint);
					// ����2
					canvas.drawPoint(250 + xf * scale, 250 - yf * scale, mPaint);
					// this.postInvalidateDelayed(10);

					// Log.i("TAG",
					// "xf-->"+(xf*scale+250)+"yf-->"+(250-yf*scale));
				}

				if ((17 * Math.pow(xf, 2) - 16 * Math.abs(xf) * (-yf) + 17 * Math
						.pow(yf, 2)) < 255) {

					// ����2
					canvas.drawPoint(250 - xf * scale, 250 + yf * scale, mPaint);
					// ���� 1
					// canvas.drawPoint(250+xf*scale,250-yf*scale, paint);

					// this.postInvalidateDelayed(10);

					// ����2
					canvas.drawPoint(250 + xf * scale, 250 + yf * scale, mPaint);
					// ���� 1
					// canvas.drawPoint(250-xf*scale,250-yf*scale, paint);

					// this.postInvalidateDelayed(10);

				}

				// ��ʱˢ�£���������Ч��
				delayedTime();
				this.postInvalidate();
			}
		}// end for

	}

	private void delayedTime() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
