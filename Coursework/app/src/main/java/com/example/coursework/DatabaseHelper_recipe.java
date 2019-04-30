package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper_recipe extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipe.db";
    public static final String TABLE_NAME = "recipe_table";
    public static final String COL_1 = "recipe_name";
    public static final String COL_2 = "recipe_ingredient";

    public DatabaseHelper_recipe(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (recipe_name text, recipe_ingredient text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String recipeName, String recipeIngredient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, recipeName);
        contentValues.put(COL_2, recipeIngredient);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "recipe_name = ?", new String[] {item});
    }

}
