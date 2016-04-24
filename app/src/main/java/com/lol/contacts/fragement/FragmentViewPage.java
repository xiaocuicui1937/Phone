package com.lol.contacts.fragement;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lol.contacts.Dao.ContactsDao;
import com.lol.contacts.R;
import com.lol.contacts.activity.AddContactActivity;
import com.lol.contacts.activity.IntimacyActivity;
import com.lol.contacts.bean.CallRecordInfo;
import com.lol.contacts.bean.ContactDetailInfo;
import com.lol.contacts.bean.ContactListItemInfo;
import com.lol.contacts.page.Page;
import com.lol.contacts.view.HeartView;
import com.lol.contacts.view.MarqueeTextView;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;


/**
 * Created by hh on 2016/4/19.
 */
public class FragmentViewPage extends Fragment {

    private static final String TAG = "FragmentViewPage";
    private ViewPager mVp_activitymain_showcontext;
    private List<Page> mPageList;
    private TitlePageIndicator mIdr_fragmentviewpage_indicator;
    private ArrayList<String> mTitles;
    private View mView;
    private RelativeLayout mRl_fragmentviewpage_usermenu;
    private ImageView mIv_fragmentviewpage_bg;
    private ImageView mIv_fragmentviewpage_biglogo;
    private Button mBt_fragmentviewpager_tointimacy;
    private HeartView mHv_fragmentviewpager_heart;

    private boolean mIsMarqueeStart;
    private MarqueeTextView mTv_fragmentviewpage_marquee;
    private List<ContactListItemInfo> mAllContacts;
    private Button mBt_fragmentviewpage_adduserdetail;
    private Page page;
    private  int i = 0;//默认数据从零开始查询
    private int j = 0;//记录返页的次数
    //初始化手指触摸屏幕的开始位置和结束为止
    int startx = 0;
    int starty = 0;
    int endx;
    int endy ;

    private ImageView mIv_pagecontent_userimgcenter;
    private ImageView mIv_pagecontent_userimg2;
    private ImageView mIv_pagecontent_userimg3;
    private ImageView mIv_pagecontent_userimg4;
    private ImageView iv_pagecontent_userimg5;
    private ImageView mIv_pagecontent_userimg6;
    private ImageView mIv_pagecontent_userimg7;
    private List<CallRecordInfo> callLogMessage;
    private boolean mIsshowBigLogoDetail;//是否显示具体大头像联系人
    private ContactsDao mContactsDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_viewpage, null);
        mVp_activitymain_showcontext = (ViewPager) mView.findViewById(R.id.vp_activitymain_showcontext);
        mRl_fragmentviewpage_usermenu = (RelativeLayout) mView.findViewById(R.id.rl_fragmentviewpage_usermenu);
        mIv_fragmentviewpage_bg = (ImageView)mView.findViewById(R.id.iv_fragmentviewpage_blurbg);
        mBt_fragmentviewpager_tointimacy = (Button) mView.findViewById(R.id.bt_fragmentviewpager_tointimacy);
        mHv_fragmentviewpager_heart = (HeartView) mView.findViewById(R.id.hv_fragmentviewpager_heart);
        mTv_fragmentviewpage_marquee = (MarqueeTextView) mView.findViewById(R.id.mtv_fragmentviewpage_marquee);
        mBt_fragmentviewpage_adduserdetail = (Button) mView.findViewById(R.id.bt_fragmentviewpage_adduserdetail);




        //将系统的status设置与apptitlebar一致的颜色
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#C4717E"));
        addViewToViewPage();

        //viewpager设置适配器，用来显示从数据库中查询出来的数据
        mVp_activitymain_showcontext.setAdapter(new ViewPageAdapter());
        //将viewpager和viewpagerIndicator进行关联
        mIdr_fragmentviewpage_indicator = (TitlePageIndicator) mView.findViewById(R.id.idr_fragmentviewpage_indicator);
        mIdr_fragmentviewpage_indicator.setViewPager(mVp_activitymain_showcontext);


        jumpToIntimacyActivity();//修改  这里应该跳转到通话记录页面
        jumpToIntimacy();//半透明心形图形可以跳转到亲密度排行榜
        //jumToIntimacyDetail();
        controlMarqueeText();//控制界面跑马灯效果，停止、开始、重新开始，有利于提升用户体验
        //跳转到添加联系人界面
        jumpToAddDetail();

        return mView;
    }


    //动态显示联系人的个数，以及动态加载每一个联系人的信息，信息包含姓名、用户设定联系人的头像以及根据亲密度不同显示不同头像大小
    private void addViewToViewPage() {

        //查询数据库中联系人姓名和头像
        queryContactList();
        mPageList.add(page);//将页面添加到数据源
        mTitles = new ArrayList<>();
        mTitles.add("欢迎使用vg");

    }

    private void queryContactList() {

        //创建查询数据库对象db
        mContactsDao = new ContactsDao(getActivity());
        mAllContacts = mContactsDao.getAllContacts(getActivity());



        mPageList = new ArrayList<Page>();
        page = new Page(getActivity());

        controlChangeContactsList();//通过监听触摸事件，可以在MOVE事件中刷新数据库联系人列表下一批的数据，就是7个联系人
        showContactsList();//显示在主界面七个联系人的头像、姓名

    }

    private void showContactsList() {
        if (j==(mAllContacts.size())/7){
            //handlerLastCotacts();//针对最后一页进行的特殊处理
        }else{
            initContactsList();
            calling();

            TextView tv_pagecontent_pagenum = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_pagenum);
            int temp = j+1;
            tv_pagecontent_pagenum.setText(temp+"");//要把j转换成int类型，防止setText时把j当成系统资源文件去系统中找
            Log.i("temp",j+"");
        }

    }


    /*
    * 因为主页显示7个头像联系人，因为查询数据从0开始的，第一页就是0-6分别对应数据库中1-7个联系人
    * 信息；而且每个联系人的头像大小都不相同需要分其中情况处理，可以找出每一轮进行联系人设置头像和
    * 姓名的规律，就是对7其余数分别是0-6。搞定这些只需要监听触摸滑动事件，我使用 int j
    * 代表页码，每翻一页就在原来的基础增加j*7，相对应的位置就是获取联系人索引
    * mAllContacts.get(i + j * 7).getmContact_icon()
    * */
    private void initContactsList() {//数据初始化，排除最后一页不做特殊处理，刷新功能

        for (int i = 0; i < 7; i++) {
            if ((i % 7) == 0) {
                mIv_pagecontent_userimgcenter = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimgcenter);
                mIv_pagecontent_userimgcenter.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_center = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_center);
                tv_pagecontent_center.setText(mAllContacts.get(i + j * 7).getmName());


                //showBigLogoDetail();
            }
            if ((i % 7) == 1) {
                mIv_pagecontent_userimg2 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg2);
                mIv_pagecontent_userimg2.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get( 1+j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name2 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name2);
                tv_pagecontent_name2.setText(mAllContacts.get(1+ j * 7).getmName());


                //showBigLogoDetail();
            }
            if ((i % 7) == 2) {

                mIv_pagecontent_userimg3 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg3);
                mIv_pagecontent_userimg3.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(2 + j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name3 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name3);
                tv_pagecontent_name3.setText(mAllContacts.get(2 + j * 7).getmName());


                // showBigLogoDetail();
            }
            if ((i % 7) == 3) {

                mIv_pagecontent_userimg4 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg4);
                mIv_pagecontent_userimg4.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(3 + j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name4 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name4);
                tv_pagecontent_name4.setText(mAllContacts.get(3 + j * 7).getmName());


                //showBigLogoDetail();
            }
            if ((i % 7) == 4) {
                iv_pagecontent_userimg5 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg5);

                iv_pagecontent_userimg5.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(4 + j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name5 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name5);
                tv_pagecontent_name5.setText(mAllContacts.get(4 + j * 7).getmName());


                //showBigLogoDetail();
            }
            if ((i % 7) == 5) {
                mIv_pagecontent_userimg6 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg6);

                mIv_pagecontent_userimg6.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(5 + j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name6 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name6);
                tv_pagecontent_name6.setText(mAllContacts.get(5 + j * 7).getmName());




                //showBigLogoDetail();
            }
            if ((i % 7) == 6) {
                mIv_pagecontent_userimg7 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg7);

                mIv_pagecontent_userimg7.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(6 + j * 7).getmContact_id(),getActivity()));
                TextView tv_pagecontent_name7 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name7);
                tv_pagecontent_name7.setText(mAllContacts.get(6 + j * 7).getmName());



                //showBigLogoDetail();
            }
        }

    }

    private void calling() {//跳转到显示大头像具体信息的界面


        mIv_pagecontent_userimgcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingCenterImg();//点击中间亲密度最高的联系人拨打电话
            }
        });
        ( page.getmRootView().findViewById(R.id.iv_pagecontent_userimg2)).
        setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callingImg2();
                }
            });

        (page.getmRootView().findViewById(R.id.iv_pagecontent_userimg3))
           .setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   callingImg3();
               }
           });

        (page.getmRootView().findViewById(R.id.iv_pagecontent_userimg4)).
                setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callingImg4();
                }
            });

        (page.getmRootView().findViewById(R.id.iv_pagecontent_userimg5)).
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callingImg5();
                }
            });

        (page.getmRootView().findViewById(R.id.iv_pagecontent_userimg6)).
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callingImg6();
                }
            });


        (page.getmRootView().findViewById(R.id.iv_pagecontent_userimg7)).
                setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callingImg7();
                }
            });
        }

    private void callingImg7() {
        mIv_pagecontent_userimg7.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(6 + j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name7 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name7);
        tv_pagecontent_name7.setText(mAllContacts.get(6 + j * 7).getmName());


        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(6+j * 7).getmContact_id(),  mAllContacts.get(6+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话

        clickCalling(phone_number);
    }

    private void callingImg6() {
        mIv_pagecontent_userimg6 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg6);

        mIv_pagecontent_userimg6.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(5 + j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name6 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name6);
        tv_pagecontent_name6.setText(mAllContacts.get(5 + j * 7).getmName());


        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(5+j * 7).getmContact_id(),  mAllContacts.get(5+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话

        clickCalling(phone_number);
    }

    private void callingImg5() {
        iv_pagecontent_userimg5 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg5);

        iv_pagecontent_userimg5.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(4 + j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name5 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name5);
        tv_pagecontent_name5.setText(mAllContacts.get(4 + j * 7).getmName());

        //打电话直接获取联系人的电话号码拨打
        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(4+j * 7).getmContact_id(),  mAllContacts.get(4+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话

        clickCalling(phone_number);
    }

    private void callingImg4() {
        mIv_pagecontent_userimg4 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg4);
        mIv_pagecontent_userimg4.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(3 + j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name4 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name4);
        tv_pagecontent_name4.setText(mAllContacts.get(3 + j * 7).getmName());

        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(3+j * 7).getmContact_id(),  mAllContacts.get(3+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话


        clickCalling(phone_number);
    }

    private void callingImg3() {
        mIv_pagecontent_userimg3 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg3);
        mIv_pagecontent_userimg3.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(2 + j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name3 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name3);
        tv_pagecontent_name3.setText(mAllContacts.get(2 + j * 7).getmName());

        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(2+j * 7).getmContact_id(),  mAllContacts.get(2+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话

        clickCalling(phone_number);
    }

    private void callingImg2() {
        mIv_pagecontent_userimg2 = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimg2);
        mIv_pagecontent_userimg2.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get( 1+j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_name2 = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_name2);
        tv_pagecontent_name2.setText(mAllContacts.get(1+ j * 7).getmName());

        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(1+j * 7).getmContact_id(),  mAllContacts.get(1+j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();

        //设置监听事件可以打电话

        clickCalling(phone_number);
    }

    private void callingCenterImg() {
        mIv_pagecontent_userimgcenter = (ImageView) page.getmRootView().findViewById(R.id.iv_pagecontent_userimgcenter);
        mIv_pagecontent_userimgcenter.setImageBitmap(mContactsDao.getBitmap(mAllContacts.get(j * 7).getmContact_id(),getActivity()));
        TextView tv_pagecontent_center = (TextView) page.getmRootView().findViewById(R.id.tv_pagecontent_center);
        tv_pagecontent_center.setText(mAllContacts.get(i + j * 7).getmName());

        ContactDetailInfo contactMessage = mContactsDao.getContactMessage(getActivity(), mAllContacts.get(j * 7).getmContact_id(),  mAllContacts.get(j * 7).getmRawContact_id());
        String phone_number = contactMessage.getmPhone_number();
        //设置监听事件可以打电话


        clickCalling(phone_number);
    }


   /* public void showBigLogoDetail(){
        mIv_pagecontent_userimgcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsshowBigLogoDetail){
                    setBigLogoPageInVisible();//大头像联系人可见
                }else{
                    setBigLogoPagevisible();//大头像联系人不可见
                }

            }
        });
        mIv_pagecontent_userimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mIv_pagecontent_userimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mIv_pagecontent_userimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_pagecontent_userimg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mIv_pagecontent_userimg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mIv_pagecontent_userimg7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }*/

   /* private void setBigLogoPageInVisible() {//隐藏具体联系人的信息，同时还隐藏菜单用来直接打电话发短信，显示主页面显示7个头像模式
        mIv_fragmentviewpage_biglogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRl_fragmentviewpage_usermenu.setVisibility(View.GONE);
                mIv_fragmentviewpage_bg.setVisibility(View.INVISIBLE);
                mVp_activitymain_showcontext.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setBigLogoPagevisible(){//使具体联系人界面显示出来
        mVp_activitymain_showcontext.setVisibility(View.INVISIBLE);
        mIv_fragmentviewpage_bg.setVisibility(View.VISIBLE);
        mRl_fragmentviewpage_usermenu.setVisibility(View.VISIBLE);
    }*/
    private void clickCalling(String num) {
        //调用系统打电话
        // TODO Auto-generated method stub
        //获取编辑框内输入的目标电话号码
        // String number = text.getText().toString();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.addCategory("android.intent.category.DEFAULT");
        //指定要拨打的电话号码
        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);


    }

    private void controlChangeContactsList() {
        //通过监听viewpage滑动事件，分批查询数据库中的数据
        mVp_activitymain_showcontext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startx = (int) event.getX();
                        starty = (int) event.getY();
                        Log.i(TAG,"ACTION_DOWN"+startx+"----"+starty);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        Log.i(TAG,"ACTION_MOVE"+endx+"----"+endy);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG,"ACTION_UP");
                        endx = (int) event.getX();
                        endy = (int) event.getY();

                        //判断水平方向滑动，左滑还是右滑
                        judgeToSlide(startx,starty,endx,endy);
                        break;
                }
                return true;
            }


        });
    }

    private void judgeToSlide(float startx,float starty,float endx,float endy) {//判断是否水平
        float dx = Math.abs(endx - startx);
        float dy = Math.abs(endy - starty);

        boolean flag = dx>dy?true:false;//true：水平滑动 false:竖直滑动

        Log.i(TAG,"执行了"+dx+"_++_"+dy);
        if (flag){//水平方向滑动
            if (endx>startx){//水平方向由左向右滑动的情况就是换上一批数据
                slideRight();//右滑
                Log.i(TAG,"右滑");
            }else {//水平方向由右向左滑动就是更换显示数据为，接着上一批下一个数据开始显示
                slideLeft();//左滑
                Log.i(TAG,"左滑");
            }
        }

    }

    private void slideLeft() {//水平向左滑动
       /*小于等于最大页数：判断当向左滑动滑到最后一页的前一页，页码可以在加一，滑到最后一页；如果再次滑动判断大于最大页码的情况
       *大于最大页数：当左滑滑到最后一页的时候，再次滑动就可以跳转到第一页，可以一直左滑，实现左滑无限滑动
       * */
        if (j<(mAllContacts.size())/7){//
            j++;
            showContactsList();
        }else{
            j= 0;
            showContactsList();
        }


    }

    private void slideRight() {//水平向右滑动
        if ((j<(mAllContacts.size())/7)&&j>0){
            j--;//向右滑页码减一，相当于减少七个联系人记录
            showContactsList();
        }else if (j==0){//判断j==0边界条件 当右滑能够滑到最后一页，实现无限循环的效果
            j=(mAllContacts.size())/7-1;
            showContactsList();
        }
    }


    private class ViewPageAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {

            return mTitles.get(position);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPageList.get(position).mRootView);
            return mPageList.get(position).getmRootView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            Log.i("destroyItem","destroyItem");
            container.removeView((View) object);
        }
    }


    private void jumpToIntimacyActivity() {//跳转到开始显示最亲密联系人的页面

        mBt_fragmentviewpager_tointimacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), IntimacyActivity.class));
            }
        });


    }

    private void jumpToIntimacy() {//点击半透明心形跳转到通话联系人排行榜

        mHv_fragmentviewpager_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), IntimacyActivity.class));
            }
        });

    }

    public void controlMarqueeText(){//控制显示跑马灯效果开始还是停止,提高与用户的交互性，提升用户体验
        mTv_fragmentviewpage_marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsMarqueeStart){
                    mTv_fragmentviewpage_marquee.startScroll();
                    mIsMarqueeStart = true;
                }else{
                    mTv_fragmentviewpage_marquee.stopScroll();
                    mIsMarqueeStart = false;
                }
            }
        });

        //长摁按钮就会使textView重新回到开始的位置，再次呈现跑马灯效果
        mTv_fragmentviewpage_marquee.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTv_fragmentviewpage_marquee.startAgain();
                return true;
            }
        });

    }

    private void jumpToAddDetail() {//跳转到添加联系人界面

        mBt_fragmentviewpage_adduserdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
    }

}


