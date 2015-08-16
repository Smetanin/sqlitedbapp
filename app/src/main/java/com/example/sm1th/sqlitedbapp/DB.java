package com.example.sm1th.sqlitedbapp;


        import android.content.ContentValues;
        import android.content.Context;
        import android.content.res.AssetManager;
        import android.content.res.Resources;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;

public class DB {



    private static String DB_PATH = "/data/data/com.example.sm1th.sqlitedbapp/databases/";
    //private static String DB_PATH = "/storage/emulated/0/Download/";

    private static String DB_NAME = "db";
    private static final int SCHEMA = 1; // версия базы данных
    final String LOG_TAG = "DB Class";


    static final String TABLE = "test";                         //название таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIELD1 = "Field2";        //название полей по которым будет
    public static final String COLUMN_FIELD2 = "Field3";        //и выводить
    public static final String COLUMN_FIELD3 = "Field4";
    public static final String COLUMN_FIELD4 = "Field5";
    public static final String COLUMN_FIELD5 = "Field6";
    public static final String COLUMN_FIELD6 = "Field7";



    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }
    // копирует файл из папки sqlitedbapp/app/src/main/assets
    // в папку DB_PATH на мобильном устройстве
    public void create_db(){

        Resources resources=mCtx.getResources();
        AssetManager assetManager = resources.getAssets();
        InputStream is = null;
        OutputStream myOutput = null;
        byte[] buffer = null;
        try {
            File file = new File(DB_PATH + DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            is = assetManager.open(DB_NAME);
            buffer = new byte[1024];
            int length;
            myOutput = new FileOutputStream(outFileName);
            while ((length = is.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            myOutput.flush();
            myOutput.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, SCHEMA);
        mDB = mDBHelper.getReadableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(TABLE, null, null, null, null, null, null);
    }


    //поиск элементов
    public Cursor getData(String SELECT_SQL) {
        Cursor c;
        String[] fields = SELECT_SQL.split(" ");
        //создаем строку с sql запросом
        String SqlQuery ="select * from " + DB.TABLE + " where ";
        int i=SqlQuery.length();
        //в эту строку складываем значения всех столбцов, если заполнены не все поля в текст.эдит
        //то мы потом разрежем строку и оставшиеся поля будут идти по порядку
        String buff = "";
        //проверяем существует ли данный столбец
        if(!fields[0].equals("")) {SqlQuery+=DB.COLUMN_FIELD1+"=?"; buff+=fields[0];}
        if(fields.length>1 && !fields[1].equals("")){
            //проверяем первым этот столбец идет или нет, надо или нет ставить and
            if(i != SqlQuery.length()){
                SqlQuery+=" and "+DB.COLUMN_FIELD2+"=?";
                buff+=" "+fields[1];
            }
            else{
                SqlQuery+=DB.COLUMN_FIELD2+"=?";
                buff+=fields[1];
            }
        }
        if(fields.length>2 && !fields[2].equals("")){
            if(i != SqlQuery.length()){
                SqlQuery+=" and "+DB.COLUMN_FIELD3+"=?";
                buff+=" "+fields[2];
            }
            else{
                SqlQuery+=DB.COLUMN_FIELD3+"=?";
                buff+=fields[2];
            }
        }
        if(fields.length>3 && !fields[3].equals("")){
            if(!fields[2].equals("")){
                SqlQuery+=" and "+DB.COLUMN_FIELD4+"=?";
                buff+=" "+fields[3];
            }
            else{
                SqlQuery+=DB.COLUMN_FIELD4+"=?";
                buff+=fields[3];
            }

        }
        if(fields.length>4 && !fields[4].equals("")){
            if(!fields[3].equals("")){
                SqlQuery+=" and "+DB.COLUMN_FIELD5+"=?";
                buff+=" "+fields[4];
            }
            else{
                SqlQuery+=DB.COLUMN_FIELD5+"=?";
                buff+=fields[4];
            }

        }
        Log.d(LOG_TAG, buff+ " buff");
        Log.d(LOG_TAG, SqlQuery+ " SqlQery");

        String[] fields2 = buff.split(" ");  //в массив строк передаем все заданные условия поиска
        SqlQuery+=";";
        Log.d(LOG_TAG, fields2.length + " getData");

        c=mDB.rawQuery(SqlQuery,fields2);
        return c;

    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}