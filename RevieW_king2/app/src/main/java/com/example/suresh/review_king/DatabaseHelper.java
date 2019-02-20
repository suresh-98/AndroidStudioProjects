package com.example.suresh.review_king;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context ) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
  db.execSQL("Create table user(password text,email text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("Drop table if exists user");
    }

    public boolean insert(String email,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email",email);
        contentValues.put("password",password);
        long ins = db.insert("user",null,contentValues);
        if(ins==-1) return  false;
        else return  true;
    }
    public  boolean chkemail(String email ){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.rawQuery("Select * from user where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return  true;
    }
}
