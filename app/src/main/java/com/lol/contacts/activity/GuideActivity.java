package com.lol.contacts.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lol.contacts.DbOpenHelper.HoneyDegreeDbOpenHelper;
import com.lol.contacts.R;
import com.lol.contacts.view.GuideViewpager;

import java.util.ArrayList;
import java.util.List;



public class GuideActivity extends Activity {

    private GuideViewpager vp_sflash_guidepage;
    private List<ImageView> list_guide;
    private ImageView iv_guide_image;
    private ArrayList<View> list_guide_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //做一个viewPager。。。
        //自定义viewPager，最后一页时自己处理左滑事件
        View view = View.inflate(GuideActivity.this, R.layout.guide_page, null);
        iv_guide_image = (ImageView) view.findViewById(R.id.iv_guide_image);
        iv_guide_image.setImageResource(R.drawable.yindao1_ps);
        iv_guide_image.setScaleType(ImageView.ScaleType.FIT_XY);
       // view.findViewById(R)

        list_guide_page = new ArrayList<>();

        list_guide = new ArrayList<>();

        ImageView guide_page1 = new ImageView(GuideActivity.this);
        guide_page1.setImageResource(R.drawable.yindao1_ps);
        guide_page1.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView guide_page2 = new ImageView(GuideActivity.this);
        guide_page2.setImageResource(R.drawable.yindao2_ps);
        guide_page2.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageView guide_page3 = new ImageView(GuideActivity.this);
        guide_page3.setImageResource(R.drawable.yindao3_ps);
        guide_page3.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageView guide_page4 = new ImageView(GuideActivity.this);
        guide_page4.setImageResource(R.drawable.yindao4_ps);
        guide_page4.setScaleType(ImageView.ScaleType.FIT_XY);

        list_guide.add(guide_page1);
        list_guide.add(guide_page2);
        list_guide.add(guide_page3);
        list_guide.add(guide_page4);

        vp_sflash_guidepage = (GuideViewpager) this.findViewById(R.id.vp_sflash_guidepage);
        vp_sflash_guidepage.setAdapter(new GuideViwePagerAdapter());
        //接收广播，
        initHoneyTable();
    }

    class GuideViwePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list_guide.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = list_guide.get(position);
            container.addView(imageView);
            return imageView;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;

        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
            // super.destroyItem(container, position, object);
        }
    }
    private void initHoneyTable() {
        HoneyDegreeDbOpenHelper honeyDegreeDb = new HoneyDegreeDbOpenHelper(GuideActivity.this, "honeyDegreeDb.db", null, 1);
        SQLiteDatabase db = honeyDegreeDb.getWritableDatabase();
    }
}
