package com.lol.contacts.utils.raindrop;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.lol.contacts.utils.RandomGenerator;


public class RainFlake {

    // ��ε��ƶ��ٶ�
    private static final float INCREMENT_LOWER = 6f;
    private static final float INCREMENT_UPPER = 8f;

    // ��εĴ�С
    private static final float FLAKE_SIZE_LOWER = 2f;
    private static final float FLAKE_SIZE_UPPER = 5f;

    private final float mIncrement; // ��ε��ٶ�
    private final float mFlakeSize; // ��εĴ�С
    private final Paint mPaint; // ����
    
    private Line mLine; // ���
    
    private RandomGenerator mRandom;

    private RainFlake(RandomGenerator random,Line line, float increment, float flakeSize, Paint paint) {
    	mRandom = random;
    	mLine = line;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mPaint = paint;
    }

    //�������
    public static RainFlake create(int width, int height, Paint paint) {
    	RandomGenerator random = new RandomGenerator();
		int [] nline;
		nline = random.getLine(width, height);
        
        Line line = new Line(nline[0], nline[1], nline[2], nline[3]);
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new RainFlake(random,line, increment, flakeSize, paint);
    }

    // �������
    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        drawLine(canvas, width, height);
    }

    /**
     *
     * @param canvas
     * @param width
     * @param height
     */
	private void drawLine(Canvas canvas, int width, int height) {
		//�����߿�
	  mPaint.setStrokeWidth(mFlakeSize);
		//y����ֱ���򣬾�������
      double y1 = mLine.y1 + (mIncrement * Math.sin(1.5));
      double y2 = mLine.y2 + (mIncrement * Math.sin(1.5));


       mLine.set(mLine.x1,(int) y1,mLine.x2 ,(int) y2);
		
		if (!isInsideLine(height)) {
			resetLine(width,height);
		}
		
		canvas.drawLine(mLine.x1, mLine.y1, mLine.x2, mLine.y2, mPaint);
	}
	
    // �ж��Ƿ�������
    private boolean isInsideLine(int height) {
        return mLine.y1 < height && mLine.y2 < height;
    }

    // �������
    private void resetLine(int width, int height) {
		int [] nline;
		nline = mRandom.getLine(width, height);
		mLine.x1 = nline[0];
		mLine.y1 = nline[1];
		mLine.x2 = nline[2];
		mLine.y2 = nline[3];
    }

}
