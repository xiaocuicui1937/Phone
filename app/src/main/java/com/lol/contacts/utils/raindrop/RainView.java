package com.lol.contacts.utils.raindrop;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lol.contacts.R;


public class RainView extends View {

    private static final int NUM_SNOWFLAKES = 15;
    private static final int DELAY = 5;
    private RainFlake[] mSnowFlakes;

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override 
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    private void initSnow(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // �����
        paint.setColor(getResources().getColor(R.color.colorWater)); // ��ε���ɫ
        paint.setStyle(Paint.Style.FILL);
        mSnowFlakes = new RainFlake[NUM_SNOWFLAKES];
        //mSnowFlakes���е���ζ����ɷŵ�������
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mSnowFlakes[i] = RainFlake.create(width, height, paint);
        }
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //for����SnowFlake
        for (RainFlake s : mSnowFlakes) {
        	//Ȼ����л���
            s.draw(canvas);
        }
        // ��һ��ʱ���ػ�һ��, ����Ч��
        getHandler().postDelayed(runnable, DELAY);
    }

    // �ػ��߳�
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        	//�Զ�ˢ��
            invalidate();
        }
    };
}
