package com.lol.contacts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hh on 2016/4/22.
 *通过重写textVeiw实现可控置跑马灯效果
 */
public class MarqueeTextView extends TextView implements Runnable {

    private int currentScrollX;//当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isMeasure){
            //获取文字宽度一次就可以了
            getTextWidth();
        }
    }
    //获取文字的宽度
    private void getTextWidth() {
        TextPaint paint = this.getPaint();
        String s = this.getText().toString();
        textWidth = (int) paint.measureText(s);
    }

    @Override
    public void run() {
        currentScrollX -=2;//文字滚动速度
        scrollTo(currentScrollX,0);//横向滚动
        if (isStop){
            return;
        }
        if (getScrollX()<=-(this.getWidth())){//getScrollX就是marqueeTextView左上角恒做表相对于父控件TextVeiw左上角横坐标的偏移量（x轴）
            scrollTo(textWidth,0);
            currentScrollX = textWidth;
        }
        postDelayed(this,5);
    }
    //开始滚动
    public void startScroll(){
        isStop = false;
        this.removeCallbacks(this);//使线程run方法停止
        post(this);//通过handler发送消息给线程重新工作
    }
    //停止滚动
    public void stopScroll(){
        isStop = true;
    }
    //从头开始滚动
    public void startAgain(){
        currentScrollX = 0;
        startScroll();
    }
}
