package project.cs442.team9.georeminder;

import android.provider.BaseColumns;

/**
 * Created by akash on 4/17/15.
 */
public class Tabledata {

    Tabledata() {
    }

    public static abstract class table implements BaseColumns
    {
        //Table Info
        public static String LIST_NAME = "list_name";
        public static String LAT="lat";
        public static String LONG="long";
        public static String DATABASE_NAME = "arafaliy9";
        public static String TABLE_NAME = "table2";
        public static String Adr="adr";
        public static String PH="PH";
        public static String SILENT="silent";
        public static String DONE="Done";
        public static String IN="inside";
        public static String MSG="MSG";

    }

}
