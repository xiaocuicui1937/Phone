package com.lol.contacts.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lol.contacts.Dao.ContactsDao;
import com.lol.contacts.R;
import com.lol.contacts.utils.BreathingViewHelper;
import com.lol.contacts.view.CustomImageView;


/**
 * Created by hh on 2016/4/19.
 */
public class Page {
    private static final String TAG = "Page";
    private Activity mActivity;
    public View mRootView;
    //private CustomImageView mIv_pagecontent_userimgcenter;
    private ImageView mIv_pagecontent_userimgcenter;

    private CustomImageView mIv_pagecontent_userimg6;
    private ImageView mIv_pagecontent_userimg2;
    private ImageView mIv_pagecontent_userimg3;
    private ImageView mIv_pagecontent_userimg4;
    private ImageView mIv_pagecontent_userimg5;
    //private ImageView mIv_pagecontent_userimg6;
    private ImageView mIv_pagecontent_userimg7;
    private boolean mIsShowBigUserMsg;
    private final Intent mIntent;
    private TextView mTv_pagecontent_center;
    private ImageView mIv_pagecontent_ringcenter;
    private ContactsDao mContactsDao;


    public Page(Activity activity){
        this.mActivity = activity;
        mIntent = new Intent();
        mIntent.setAction("com.lol.contacts");
        initView();
        initData();
    }



    private void initView() {
        mRootView = View.inflate(mActivity, R.layout.page_content, null);
        //自定义iv view控件
        mIv_pagecontent_userimgcenter = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimgcenter);
        mIv_pagecontent_userimg2 = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg2);
        mIv_pagecontent_userimg3 = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg3);
        mIv_pagecontent_userimg4 = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg4);
        mIv_pagecontent_userimg5 = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg5);
        mIv_pagecontent_userimg6 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg6);
        mIv_pagecontent_userimg7 = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg7);

        mIv_pagecontent_ringcenter = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_ringcenter);
        //文本控件
        mTv_pagecontent_center = (TextView) mRootView.findViewById(R.id.tv_pagecontent_center);



    }

    private void initData(){
        setTextViewBreath();
    }



    public View getmRootView(){
        return mRootView;
    }
    //给中间view name添加呼吸背景效果
    private void setTextViewBreath() {
        BreathingViewHelper.setBreathingBackgroundColor(mTv_pagecontent_center, Color.parseColor("#99ff0000"));
    }



}
