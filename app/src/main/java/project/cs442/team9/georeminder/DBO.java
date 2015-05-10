package project.cs442.team9.georeminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by akash on 4/17/15.
 */
public class DBO extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

//    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Tabledata.table.TABLE_NAME + " (" +
                    Tabledata.table.LIST_NAME + " TEXT,"+ Tabledata.table.LAT +" DOUBLE,"+ Tabledata.table.LONG +" DOUBLE,"+ Tabledata.table.PH +" TEXT,"+ Tabledata.table.SILENT +" INTEGER,"+ Tabledata.table.Adr +" TEXT,"+ Tabledata.table.MSG +" TEXT,"+ Tabledata.table.IN +" INTEGER,"+ Tabledata.table.DONE +" INTEGER"+");";


    public DBO(Context context) {
        super(context, Tabledata.table.DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATABSE", "TABLE START");
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d("DATABASE", "TABLE IS CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(DBO dbo, String todo,double lat,double lon,String ph,int sil, String adr ,String msg,int done,int in) {
        SQLiteDatabase db = dbo.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Add data to table
        values.put(Tabledata.table.LIST_NAME, todo);
        values.put(Tabledata.table.LAT, lat);
        values.put(Tabledata.table.LONG, lon);
        values.put(Tabledata.table.PH, ph);
        values.put(Tabledata.table.SILENT, sil);
        values.put(Tabledata.table.Adr, adr);
        values.put(Tabledata.table.MSG,msg);
        values.put(Tabledata.table.DONE,done);
        values.put(Tabledata.table.IN,in);

        long newrow;

        newrow = db.insert(Tabledata.table.TABLE_NAME, null, values);
        Log.d("DATABSE", "INSERT_at" + newrow + "");
    }

    public Cursor getdata(DBO db) {
        SQLiteDatabase sd = db.getReadableDatabase();
        String[] cms = {Tabledata.table.LIST_NAME, Tabledata.table.LAT, Tabledata.table.LONG, Tabledata.table.PH, Tabledata.table.SILENT, Tabledata.table.Adr, Tabledata.table.MSG, Tabledata.table.DONE, Tabledata.table.IN};
         Log.d("retrieve","Start");
         Cursor cr = sd.query(Tabledata.table.TABLE_NAME, cms, null, null, null, null, null);

         Log.d("RRRRR","Success");
        return cr;

    }

    public void updatedone(DBO db,int done,String listname )
    {SQLiteDatabase sd = db.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tabledata.table.DONE, done);

        String selection = Tabledata.table.LIST_NAME + " LIKE ?";
        String[] selectionArgs = { String.valueOf(listname) };

        int count = sd.update(
                Tabledata.table.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public void updatein(DBO db,int in,String listname )
    {SQLiteDatabase sd = db.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tabledata.table.IN, in);

        String selection = Tabledata.table.LIST_NAME + " LIKE ?";
        String[] selectionArgs = { String.valueOf(listname) };

        int count = sd.update(
                Tabledata.table.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
   public void delete(DBO db,String listname)
   {   SQLiteDatabase sd = db.getReadableDatabase();

       String selection = Tabledata.table.LIST_NAME + " LIKE ?";
       String[] selectionArgs = { String.valueOf(listname) };

       int count = sd.delete(
               Tabledata.table.TABLE_NAME,
               selection,
               selectionArgs);


   }

}
