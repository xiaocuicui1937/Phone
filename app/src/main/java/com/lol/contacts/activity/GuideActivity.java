package com.lol.contacts.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lol.contacts.Dao.ContactsDao;
import com.lol.contacts.DbOpenHelper.HoneyDegreeDbOpenHelper;
import com.lol.contacts.R;
import com.lol.contacts.bean.ContactListItemInfo;
import com.lol.contacts.view.GuideViewpager;

import java.util.ArrayList;
import java.util.List;

/**
*功能：主要实现引导页面,
 * 思路：check是否是第一次进入，分别作出处理
 *      if是第一次：那么就新建honeyDb，之后初始化，完成之后跳到主页面
 *
 *   ps；初始化是个耗时操作，放在子线程中处理，加了progressBar通知用户
 *
*@author wzq
*created at 2016/4/23 23:00
*/
public class GuideActivity extends Activity {

    private GuideViewpager vp_sflash_guidepage;
    private List<ImageView> list_guide;
    private Button bt_guide_input;
    private ProgressBar pb_guide_inittable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //check   是否是第一次，如果不是的话，就直接跳到main，
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirstTime = config.getBoolean("isFirstTime", true);
        if (!isFirstTime) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            intoGuidePage(config);//进入引导页
        }
    }

    private void intoGuidePage(SharedPreferences config) {
        SharedPreferences.Editor edit = config.edit();
        edit.putBoolean("isFirstTime",false);
        edit.commit();

        list_guide = new ArrayList<>();
        initList();

        vp_sflash_guidepage = (GuideViewpager) this.findViewById(R.id.vp_sflash_guidepage);
        bt_guide_input = (Button) this.findViewById(R.id.bt_guide_input);
        pb_guide_inittable = (ProgressBar) this.findViewById(R.id.pb_guide_inittable);
        pb_guide_inittable.setMax(100);

        bt_guide_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //接收广播，
                initHoneyTable();
            }
        });

        vp_sflash_guidepage.setAdapter(new GuideViwePagerAdapter());

        vp_sflash_guidepage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == vp_sflash_guidepage.getAdapter().getCount() - 1) {
                    bt_guide_input.setVisibility(View.VISIBLE);
                    pb_guide_inittable.setVisibility(View.VISIBLE);
                } else {
                    bt_guide_input.setVisibility(View.INVISIBLE);
                    pb_guide_inittable.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initList() {
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
    }

    private void initHoneyTable() {
        HoneyDegreeDbOpenHelper honeyDegreeDb = new HoneyDegreeDbOpenHelper(GuideActivity.this, "honeyDegreeDb.db", null, 1);
        SQLiteDatabase db = honeyDegreeDb.getWritableDatabase();

        //初始化放在子线程中进行（）
        // initTable(db);
        new InitTableAsyncTask().execute(db);
    }

    class InitTableAsyncTask extends AsyncTask<SQLiteDatabase, Integer, Void> {
        @Override
        protected Void doInBackground(SQLiteDatabase... sqLiteDatabases) {
            for (int i = 0; i < 30; i++) {
                try {
                    Thread.sleep(50);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            initTable(sqLiteDatabases[0]);
            for (int i = 30; i < 100; i++) {
                try {
                    Thread.sleep(50);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb_guide_inittable.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            GuideActivity.this.finish();
        }
    }

    private void initTable(SQLiteDatabase db) {
        ContentResolver contentResolver = GuideActivity.this.getContentResolver();
        //contact_id  +    name  +  phone  + count
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.TIMES_CONTACTED
        };
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            int count = cursor.getInt(1);
            int score = getScore(count);
            ContentValues values = new ContentValues();
            values.put("contact_id", contact_id);

            values.put("count", count);
            values.put("score", score);

            Cursor cursor_raw = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                    new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                    ContactsContract.RawContacts.CONTACT_ID + "=?",
                    new String[]{contact_id},
                    null
            );
            String rawCotact_id = null;
            if (cursor_raw.moveToNext()) {
                rawCotact_id = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts._ID));
                String name = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                values.put("name", name);
            }
            if (rawCotact_id != null) {
                Cursor cursor_data = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.Data.DATA1},
                        ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{rawCotact_id, "vnd.android.cursor.item/phone_v2"},
                        null
                );
                if (cursor_data.moveToNext()) {
                    String phone_number = cursor_data.getString(0);
                    values.put("phone_number", phone_number);
                }
            }
            db.insert("honeyDegree", null, values);
        }
    }

    public int getScore(int number) {
        int re = -1;
        if (number == 0) {
            re = 20;
        } else {
            re = 20 + (int) (80 - 80 / number);
        }
        return re;
    }

    class GuideViwePagerAdapter extends PagerAdapter {

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
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
            // super.destroyItem(container, position, object);
        }
    }

}
