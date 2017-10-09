package no.martinpedersen.myreminers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marti on 03.10.2017.
 */

public class RemindersDbAdapter {

    //column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";

    //corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IPORTANT = INDEX_ID + 2;

    //for logging
    public static final String TAG = "RemindersDbAdaptor";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    //SQL to create database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";

    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
    }

    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //CRUD operations

    //Create
    public void createReminder(String name, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, name);
        values.put(COL_IMPORTANT, important ? 1 : 0);
        mDb.insert(TABLE_NAME,null,values);
    }

    //overload to take a reminder
    public long createReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT,reminder.getContent());
        values.put(COL_IMPORTANT,reminder.getImportant());

        //insert row
        return mDb.insert(TABLE_NAME,null,values);
    }

    //read

    public Reminder fetchReminderById(int id) {
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new Reminder(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IPORTANT)
        );
    }

    public Cursor fetchAllReminders() {
        Cursor mCursor = mDb.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},null,null,null,null,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //update
    public void updateReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT,reminder.getImportant());
        mDb.update(TABLE_NAME, values, COL_ID + "=?",new String[]{String.valueOf(reminder.getId())});
    }




    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
