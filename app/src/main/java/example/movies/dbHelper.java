package example.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by peter on 24/4/2016.
 */
public class dbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorites.db";
    public static final String TABLE_NAME = "favorite";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_IMAGE = "image";
    public static final String COL_RELEASE_DATE = "release_date";
    public static final String COL_POPULARITY = "popularity";
    public static final String COL_VOTE_AVERAGE = "vote_average";
    public static final String COL_OVERVIEW = "overview";


    public dbHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + COL_ID + " integer primary key," + COL_TITLE + " text," + COL_IMAGE + " text," + COL_RELEASE_DATE + " text,"
                + COL_POPULARITY + " text," + COL_VOTE_AVERAGE + " text," + COL_OVERVIEW + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public void insertMovie(String title,String image,String release_date,String popularity,String vote_average,String overview){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,title);
        cv.put(COL_IMAGE,image);
        cv.put(COL_RELEASE_DATE,release_date);
        cv.put(COL_POPULARITY,popularity);
        cv.put(COL_VOTE_AVERAGE,vote_average);
        cv.put(COL_OVERVIEW, overview);
        db.insert(TABLE_NAME, null, cv);
    }
    public ArrayList<String> getAllOf(String columnName){
        ArrayList<String> al = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME,null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            al.add(c.getString(c.getColumnIndex(columnName)));
            c.moveToNext();
        }
        return al;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_ID+"="+id+"",null);
        return c;
    }
}
