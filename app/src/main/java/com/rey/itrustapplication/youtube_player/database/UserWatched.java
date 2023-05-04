package com.rey.itrustapplication.youtube_player.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rey.itrustapplication.youtube_player.models.UserWatchedModel;
import java.util.ArrayList;

public class UserWatched extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "UserWatched";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_YT_LINK = "yt_link";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    public UserWatched(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_YT_LINK + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT)";
        sqLiteDatabase.execSQL(createTableQuery);

    }

    public void insertUserWatched(String ytLink, String category, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_YT_LINK, ytLink);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<UserWatchedModel> getAllUserWatched() {
        ArrayList<UserWatchedModel> userWatchedList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                int columnYtLinkIndex = cursor.getColumnIndex(COLUMN_YT_LINK);
                int columnCategoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int columnTitleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                int columnDescriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);

                if (columnYtLinkIndex > -1 && columnCategoryIndex > -1 && columnTitleIndex > -1 && columnDescriptionIndex > -1) {
                    String ytLink = cursor.getString(columnYtLinkIndex);
                    String category = cursor.getString(columnCategoryIndex);
                    String title = cursor.getString(columnTitleIndex);
                    String description = cursor.getString(columnDescriptionIndex);
                    UserWatchedModel userWatched = new UserWatchedModel(ytLink, title, category, description);
                    userWatchedList.add(userWatched);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userWatchedList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
