package com.lol.contacts.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lol.contacts.DbOpenHelper.HoneyDegreeDbOpenHelper;
import com.lol.contacts.bean.HoneyContactInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 功能：实现获取按亲密度排序的联系人
 *
 * @author wzq
 *         created at 2016/4/22 16:16
 */
public class HoneyDegreeDao {
    SQLiteDatabase db;

    public HoneyDegreeDao(Context context) {
        HoneyDegreeDbOpenHelper honeyDegreeDb = new HoneyDegreeDbOpenHelper(context, "honeyDegreeDb.db", null, 1);
        db = honeyDegreeDb.getWritableDatabase();
    }
/**
*功能：获取按亲密度排行的联系人的信息
*@author wzq
*created at 2016/4/22 17:03
*/
    public List<HoneyContactInfo> getContactOrderByScore() {
        ArrayList<HoneyContactInfo> list_honeyInfo = new ArrayList<>();

        Cursor cursor = db.query("honeyDegree", null, null, null, null, null, "score");
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));

            HoneyContactInfo honeyContactInfo = new HoneyContactInfo(contact_id, name, phone_number, score, count);
            list_honeyInfo.add(honeyContactInfo);
        }
        cursor.close();
        return list_honeyInfo;
    }
}
