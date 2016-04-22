package com.lol.contacts.fragement;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lol.contacts.Dao.ContactsDao;
import com.lol.contacts.R;
import com.lol.contacts.activity.IntimacyActivity;
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
    private TextView mTv_fragmentviewpager_call;
    private TextView mTv_fragmentviewpager_sendmsg;
    private TextView mTv_fragmentviewpager_userdetail;
    private boolean mIsMarqueeStart;
    private MarqueeTextView mTv_fragmentviewpage_marquee;
    private List<ContactListItemInfo> mAllContacts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_viewpage, null);
        mVp_activitymain_showcontext = (ViewPager) mView.findViewById(R.id.vp_activitymain_showcontext);
        mRl_fragmentviewpage_usermenu = (RelativeLayout) mView.findViewById(R.id.rl_fragmentviewpage_usermenu);
        mIv_fragmentviewpage_bg = (ImageView)mView.findViewById(R.id.iv_fragmentviewpage_blurbg);
        mIv_fragmentviewpage_biglogo = (ImageView) mView.findViewById(R.id.iv_fragmentviewpage_biglogo);
        mBt_fragmentviewpager_tointimacy = (Button) mView.findViewById(R.id.bt_fragmentviewpager_tointimacy);
        mHv_fragmentviewpager_heart = (HeartView) mView.findViewById(R.id.hv_fragmentviewpager_heart);
        mTv_fragmentviewpage_marquee = (MarqueeTextView) mView.findViewById(R.id.mtv_fragmentviewpage_marquee);


        mTv_fragmentviewpager_call = (TextView) mView.findViewById(R.id.tv_fragmentviewpager_call);
        mTv_fragmentviewpager_sendmsg = (TextView) mView.findViewById(R.id.tv_fragmentviewpager_sendmsg);
        mTv_fragmentviewpager_userdetail = (TextView) mView.findViewById(R.id.tv_fragmentviewpager_userdetail);

        //将系统的status设置与apptitlebar一致的颜色
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#C4717E"));
        addViewToViewPage();
        mVp_activitymain_showcontext.setAdapter(new ViewPageAdapter());
        //将viewpager和viewpagerIndicator进行关联
        mIdr_fragmentviewpage_indicator = (TitlePageIndicator) mView.findViewById(R.id.idr_fragmentviewpage_indicator);
        mIdr_fragmentviewpage_indicator.setViewPager(mVp_activitymain_showcontext);

        getIntentFromPage();//接收来自fragment的广播，点击联系人的头像后就隐藏这页，显示带有打电话、发短信功能的页面
        setBigLogoPageVisible();//点击设置具体联系人能够打电话页面隐藏，头像联系人显示
        jumpToIntimacyActivity();//点击亲密度心形按钮，跳转到显示最亲密人的页面
        jumpToIntimacy();//半透明心形图片可以跳转到亲密度排行榜
        jumToIntimacyDetail();
        controlMarqueeText();//控制界面跑马灯效果，停止、开始、重新开始，有利于提升用户体验

        return mView;//super.onCreateView(inflater, container, savedInstanceState);
    }


    //动态显示联系人的个数，以及动态加载每一个联系人的信息，信息包含姓名、用户设定联系人的头像以及根据亲密度不同显示不同头像大小
    private void addViewToViewPage() {

        //创建查询数据库对象db
        ContactsDao contactsDao = new ContactsDao(getActivity());
        mAllContacts = contactsDao.getAllContacts(getActivity());

        mPageList = new ArrayList<Page>();

        Page page = new Page(getActivity());
        Page page1 = new Page(getActivity());
        Page page2 = new Page(getActivity());
        Page page3 = new Page(getActivity());
        Page page4 = new Page(getActivity());
        Page page5 = new Page(getActivity());
        Page page6 = new Page(getActivity());
        Page page7 = new Page(getActivity());
        Page page8 = new Page(getActivity());
        Page page9 = new Page(getActivity());
        mPageList.add(page);
        mPageList.add(page1);
        mPageList.add(page2);
        mPageList.add(page3);
        mPageList.add(page4);
        mPageList.add(page5);
        mPageList.add(page6);
        mPageList.add(page7);
        mPageList.add(page8);
        mPageList.add(page9);
        mTitles = new ArrayList<>();
        mTitles.add("A");
        mTitles.add("B");
        mTitles.add("C");
        mTitles.add("D");
        mTitles.add("E");
        mTitles.add("F");
        mTitles.add("G");
        mTitles.add("H");
        mTitles.add("M");
        mTitles.add("N");
        int i = 1;
        while(true){
            Page pagei = new Page(getActivity());
            if ((i%7)!=0){
                break;
            }
            mPageList.add(pagei);
            i++;
        }
    }

    private void getIntentFromPage() {

        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lol.contacts");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean clickbig = intent.getBooleanExtra("clickbig", false);
                Log.i("tag",""+clickbig);
                if (clickbig){//true 显示毛玻璃和用户大头像、操作菜单
                    mVp_activitymain_showcontext.setVisibility(View.INVISIBLE);
                    mIv_fragmentviewpage_bg.setVisibility(View.VISIBLE);
                    //showBackgroundBlur();
                    mRl_fragmentviewpage_usermenu.setVisibility(View.VISIBLE);

                }
            }
        };
        instance.registerReceiver(broadcastReceiver,intentFilter);//注册广播接收者
    }

    private void setBigLogoPageVisible() {
        mIv_fragmentviewpage_biglogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRl_fragmentviewpage_usermenu.setVisibility(View.GONE);
                mIv_fragmentviewpage_bg.setVisibility(View.INVISIBLE);
                mVp_activitymain_showcontext.setVisibility(View.VISIBLE);
            }
        });
    }

    private class ViewPageAdapter extends PagerAdapter {

       /* @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }*/

        @Override
        public int getCount() {
            return mAllContacts.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPageList.get(position).getmRootView());
            return mPageList.get(position).getmRootView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            Log.i("destroyItem","destroyItem");
            container.removeView((View) object);
        }
    }

    /*//实现背景图片毛玻璃效果,点击头像之后背景出现的效果
    public void showBackgroundBlur(){

        //使用颜色表示法 创建图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        //使用bitmapFactory将图片传入进来
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.startbg13);

        mIv_fragmentviewpage_bg.setImageBitmap(bitmap);

        //实现毛玻璃效果
        //增大scaleRatio缩放比，使用一样更小的bitmap去虚化可以得到更好的模糊效果 减少内存的消耗
        //增大blurRadius获得更好的效果，但是会提高cpu的消耗
        int scaleRatio = 4;
        int blurRadius = 3;
        Bitmap bitmapBlurOption = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/scaleRatio,bitmap.getHeight()/blurRadius,false);

        Bitmap bitmapBlur = FastBlur.doBlur(bitmapBlurOption, blurRadius, true);
        mIv_fragmentviewpage_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIv_fragmentviewpage_bg.setImageBitmap(bitmapBlur);
    }*/

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


    private void jumToIntimacyDetail() {
        mTv_fragmentviewpager_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), IntimacyActivity.class));
            }
        });
        mTv_fragmentviewpager_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), IntimacyActivity.class));
            }
        });

        mTv_fragmentviewpager_userdetail.setOnClickListener(new View.OnClickListener() {
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


}


