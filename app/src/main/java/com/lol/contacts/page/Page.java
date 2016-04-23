package com.lol.contacts.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lol.contacts.Dao.ContactsDao;
import com.lol.contacts.R;
import com.lol.contacts.bean.ContactListItemInfo;
import com.lol.contacts.utils.BreathingViewHelper;
import com.lol.contacts.view.CustomImageView;

import java.util.List;


/**
 * Created by hh on 2016/4/19.
 */
public class Page {
    private static final String TAG = "Page";
    private Activity mActivity;
    public View mRootView;
    private CustomImageView mIv_pagecontent_userimgcenter;
    private CustomImageView mIv_pagecontent_userimg2;
    private CustomImageView mIv_pagecontent_userimg3;
    private CustomImageView mIv_pagecontent_userimg4;
    private CustomImageView mIv_pagecontent_userimg5;
    private CustomImageView mIv_pagecontent_userimg6;
    private CustomImageView mIv_pagecontent_userimg7;
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
        mIv_pagecontent_userimgcenter = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimgcenter);
        mIv_pagecontent_userimg2 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg2);
        mIv_pagecontent_userimg3 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg3);
        mIv_pagecontent_userimg4 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg4);
        mIv_pagecontent_userimg5 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg5);
        mIv_pagecontent_userimg6 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg6);
        mIv_pagecontent_userimg7 = (CustomImageView) mRootView.findViewById(R.id.iv_pagecontent_userimg7);



        mIv_pagecontent_ringcenter = (ImageView) mRootView.findViewById(R.id.iv_pagecontent_ringcenter);
        //文本控件
        mTv_pagecontent_center = (TextView) mRootView.findViewById(R.id.tv_pagecontent_center);



        //创建dao数据库查询对象
        mContactsDao = new ContactsDao(mActivity);
        //查询数据库数据
        queryDataBase();

    }


    private void queryDataBase() {
        //查询数据库中联系人头像
        new Thread(){
            @Override
            public void run() {
                 List<ContactListItemInfo> allContacts = mContactsDao.getAllContacts(mActivity);
                super.run();
                for (int i= 0;i<1;i++){
                    ContactListItemInfo contactListItemInfo = allContacts.get(i);
                    Log.i("tag",contactListItemInfo.toString());
                }
            }
        }.start();


    }

    private void initData(){
        setTextViewBreath();
        clickLogoToDo();//点击屏幕上的头像弹出能够打电话、发短信、跳转到联系人详情页面的具体信息
        setLogoAndName();//动态在数据库中读取头像和名字
    }

    private void setLogoAndName() {
        //ContactsDao
    }

    private void clickLogoToDo() {
        mIv_pagecontent_userimgcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMsgControlBlur(mIntent);

                Log.i(TAG,"广播发送成功");
            }
        });
        mIv_pagecontent_userimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
        mIv_pagecontent_userimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
        mIv_pagecontent_userimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
        mIv_pagecontent_userimg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
        mIv_pagecontent_userimg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
        mIv_pagecontent_userimg7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgControlBlur(mIntent);
            }
        });
    }

    //发送广播给fragment中的方法控制是否显示能够打电话发短信的页面
    private void sendMsgControlBlur(Intent intent) {
        if (!mIsShowBigUserMsg){
            intent.putExtra("clickbig",true);
            mIsShowBigUserMsg = true;
        }
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(mIntent);
    }

    public View getmRootView(){
        return mRootView;
    }
    //给中间view name添加呼吸背景效果
    private void setTextViewBreath() {
        BreathingViewHelper.setBreathingBackgroundColor(mTv_pagecontent_center, Color.parseColor("#99ff0000"));
    }



}
