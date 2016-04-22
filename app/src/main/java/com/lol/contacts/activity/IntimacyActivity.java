package com.lol.contacts.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lol.contacts.R;
import com.lol.contacts.bean.IntimacyMan;
import com.lol.contacts.view.CustomImageView;

import java.util.ArrayList;
import java.util.List;

public class IntimacyActivity extends AppCompatActivity {

    private GridView gv_activityintimacy_mostintimacy;
    private List<IntimacyMan> intimacyManList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimacy);

        gv_activityintimacy_mostintimacy = (GridView) findViewById(R.id.gv_activityintimacy_mostintimacy);

        intimacyManList = new ArrayList<>();
        for(int i=0;i<9;i++){
            IntimacyMan intimacyMan = new IntimacyMan("张无忌",R.drawable.dog);
            intimacyManList.add(intimacyMan);
        }
        gv_activityintimacy_mostintimacy.setAdapter(new IntimacyAdpter());

    }

    class IntimacyAdpter extends BaseAdapter {
        @Override
        public int getCount() {
            return intimacyManList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item;
            ViewHolder holder;
            if (convertView!=null){
                item = convertView;
                holder = (ViewHolder) item.getTag();
            }else{
                item= View.inflate(IntimacyActivity.this, R.layout.item_intimacy, null);
                CustomImageView civ_itemintimacy_logo = (CustomImageView) item.findViewById(R.id.civ_itemintimacy_logo);
                TextView tv_itemintimacy_name = (TextView) item.findViewById(R.id.tv_itemintimacy_name);

                holder = new ViewHolder();
                holder.civ = civ_itemintimacy_logo;
                holder.tv = tv_itemintimacy_name;

                item.setTag(holder);
            }

            IntimacyMan intimacyMan = intimacyManList.get(position);
            holder.civ.setBackgroundResource(intimacyMan.getLogoId());
            holder.tv.setText(intimacyMan.getName());

            return item;
        }
    }

    class ViewHolder{
        TextView tv;
        CustomImageView civ;
    }
}
