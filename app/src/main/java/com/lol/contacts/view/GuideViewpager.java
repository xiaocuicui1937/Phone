package com.lol.contacts.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lol.contacts.activity.MainActivity;


/**
 * Created by Administrator on 2016/4/22.
 */
public class GuideViewpager extends ViewPager {

    Context context;

    public GuideViewpager(Context context) {
        super(context);
        this.context=context;
    }

    public GuideViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    //监测左滑事件，如果是不是最后一页，do  noting，如果是最后一页，那么跳转
    float start_x;
    float start_y;
    float end_x;
    float end_y;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                start_x=ev.getRawX();
                start_y=ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                end_x=ev.getRawX();
                end_y=ev.getRawY();

                float dx=Math.abs(end_x-start_x);
                float dy=Math.abs(end_y-start_y);
                if(dx>dy){
                    if(end_x<start_x&&(getAdapter().getCount()-1)==getCurrentItem()){
                        //left  jump to mainActivity
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
                break;

        }


        return super.onTouchEvent(ev);
    }
}
