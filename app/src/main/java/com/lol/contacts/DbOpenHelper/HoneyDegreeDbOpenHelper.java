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

        initTable(sqLiteDatabase);
    }

    private void initTable(SQLiteDatabase db) {

        ContentResolver contentResolver = context.getContentResolver();
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
                    new String[]{ContactsContract.RawContacts._ID,ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                    ContactsContract.RawContacts.CONTACT_ID + "=?",
                    new String[]{contact_id},
                    null
            );
            String rawCotact_id=null;
            if(cursor_raw.moveToNext()){
                 rawCotact_id = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts._ID));
                String name = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                values.put("name",name);
            }
            if(rawCotact_id!=null){
                Cursor cursor_data = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.Data.DATA1},
                        ContactsContract.Data.RAW_CONTACT_ID + "=? AND "+ ContactsContract.Data.MIMETYPE+"=?",
                        new String[]{rawCotact_id,"vnd.android.cursor.item/phone_v2"},
                        null
                );
                if(cursor_data.moveToNext()){
                    String phone_number = cursor_data.getString(0);
                    values.put("phone_number",phone_number);
                }
            }
            db.insert("honeyDegree", null, values);
        }
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
    public int getScore(int number) {
        int re = -1;
        if (number == 0) {
            re = 20;
        } else {
            re = 20 + (int) (80 - 80 / number);
        }
        return re;
    }
}
