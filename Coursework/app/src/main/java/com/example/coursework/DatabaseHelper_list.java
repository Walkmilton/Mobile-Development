package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper_list extends SQLiteOpenHelper {

    //Declaring Database Variables
    public static final String DATABASE_NAME = "list.db";
    public static final String TABLE_NAME = "shopping_list_table";
    public static final String COL_1 = "item_name";

    public DatabaseHelper_list(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (item_name text)");
    }

    //If the table already exists create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Inserting data to table
    public boolean insertData(String itemName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, itemName);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    //Retrieving Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    //Deleting Data
    public Integer deleteData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "item_name = ?", new String[] {item});
    }
}

