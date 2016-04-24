package com.lol.contacts.DbOpenHelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;


/**
*功能：用于创建亲密度数据库，并实现初始化。
*@author wzq
*created at 】 10:53
*/

public class HoneyDegreeDbOpenHelper extends SQLiteOpenHelper {

    Context context;

    public HoneyDegreeDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table honeyDegree (_id integer primary key autoincrement, contact_id varchar(4),name varchar(20)," +
                "phone_number varchar(20),count integer , score integer);";
        sqLiteDatabase.execSQL(sql);
    }
    private String getPhoneById(Context context, String rawContact_id) {
        String phone = null;
        Cursor query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.MIMETYPE + "=? AND " + ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{"vnd.android.cursor.item/phone_v2", rawContact_id}, null);
        if (query.moveToNext()) {
            phone = query.getString(0);
        }
        return phone;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}
