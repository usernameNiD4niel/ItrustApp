package com.rey.itrustapplication.youtube_player.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.rey.itrustapplication.youtube_player.models.UserInteractionModel;

import java.util.ArrayList;
import java.util.List;

public class UserInteraction extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_INTERACTION = "UserInteraction";
    private static final String COLUMN_POINTS = "points";
    private static final String COLUMN_CATEGORY = "category";

    public UserInteraction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_INTERACTION + "(" +
                COLUMN_POINTS + " INTEGER, " +
                COLUMN_CATEGORY + " TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do here
    }

    public void insertInteraction(int points, String category) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO " + TABLE_INTERACTION + " (" +
                COLUMN_POINTS + ", " + COLUMN_CATEGORY + ") " +
                "VALUES (?, ?)";

        try (SQLiteStatement statement = db.compileStatement(sql)) {
            statement.bindLong(1, points);
            statement.bindString(2, category);
            statement.executeInsert();
        }

        db.close();
    }

    public void updateInteractionPoints(String category, int newPoints) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "UPDATE " + TABLE_INTERACTION +
                " SET " + COLUMN_POINTS + " = ?" +
                " WHERE " + COLUMN_CATEGORY + " = ?";

        try (SQLiteStatement statement = db.compileStatement(sql)) {
            statement.bindLong(1, newPoints);
            statement.bindString(2, category);
            statement.executeUpdateDelete();
        }

        db.close();
    }

    public List<UserInteractionModel> getAllUserInteractions() {
        List<UserInteractionModel> userInteractionList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_INTERACTION + " ORDER BY " + COLUMN_POINTS + " DESC";

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            while (cursor.moveToNext()) {

                int columnCategoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int columnPointsIndex = cursor.getColumnIndex(COLUMN_POINTS);

                if (columnCategoryIndex > -1 && columnPointsIndex > -1) {
                    UserInteractionModel userInteraction = new UserInteractionModel(cursor.getString(columnCategoryIndex), cursor.getInt(columnPointsIndex));
                    userInteractionList.add(userInteraction);

                }
            }
        }

        return userInteractionList;
    }

}
