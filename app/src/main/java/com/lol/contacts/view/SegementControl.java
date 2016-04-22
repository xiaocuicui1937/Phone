package com.lol.contacts.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lol.contacts.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hh on 2016/4/18.
 * 这个控件是模仿iso7二选一的效果才使用的
 *
 * 其实就是在linearlayout中放置两个textView 进行改造而已
 */
public class SegementControl extends LinearLayout{
    private TextView tv_right;
    private TextView tv_left;
    private onSegmentViewClickListener listener;

    public SegementControl(Context context) {
        super(context);
        init();
    }

    public SegementControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
            tv_left = new TextView(getContext());
            tv_right = new TextView(getContext());

        tv_right.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,2));
        tv_left.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT ,2));
        tv_left.setText("经典");
        tv_right.setText("头像");

        //getXml的资源文件放在 res/xml/下
        XmlPullParser xmp = getResources().getXml(R.xml.text_color_select);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),xmp);
            tv_left.setTextColor(csl);
            tv_right.setTextColor(csl);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_left.setGravity(Gravity.CENTER);
        tv_right.setGravity(Gravity.CENTER);
        tv_left.setPadding(5,9,5,9);
        tv_right.setPadding(5,9,5,9);
        setSegmentTextSize(18);

        tv_left.setBackgroundResource(R.drawable.bt_left_config);
        tv_right.setBackgroundResource(R.drawable.bt_right_config);

        this.removeAllViews();
        this.addView(tv_left);
        this.addView(tv_right);
        this.invalidate();
        tv_right.setSelected(true);
        //给两个textView设置监听事件
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_left.isSelected()){
                    return;
                }
                tv_left.setSelected(true);
                tv_right.setSelected(false);
                if (listener!=null){
                    listener.onSegmentViewClick(tv_left,0);
                }
            }
        });

        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_right.isSelected()){
                    return;
                }
                tv_left.setSelected(false);
                tv_right.setSelected(true);
                if (listener!=null){
                    listener.onSegmentViewClick(tv_right,1);
                }
            }
        });


    }
    //设置字体大小
    public void setSegmentTextSize(int dp){
        tv_left.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dp);
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dp);


    }
    private static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setOnSegmentViewClickListener(onSegmentViewClickListener listener) {
        this.listener = listener;
    }

    public void setSegmentText(CharSequence text,int position) {
        if (position == 0) {
            tv_left.setText(text);
        }
        if (position == 1) {
            tv_right.setText(text);
        }
    }

    public static interface onSegmentViewClickListener{
        /**
         *
         * <p>2014年7月18日</p>
         * @param v
         * @param position 0-左边 1-右边
         * @author RANDY.ZHANG
         */
        public void onSegmentViewClick(View v,int position);
    }
}

