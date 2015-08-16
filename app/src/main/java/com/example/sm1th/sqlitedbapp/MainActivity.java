package com.example.sm1th.sqlitedbapp;

/**
 * Created by sm1th on 18.07.15.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    EditText txt1;
    EditText txt2;
    EditText txt3;
    EditText txt4;
    EditText txt5;
    final String LOG_TAG = "Main";
    private static String DB_PATH = "/data/data/com.example.sm1th.myapplication/databases/";
    private static String DB_NAME = "db";



    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // открываем подключение к БД
        db = new DB(this);
        db.create_db();
        db.open();
        //находим поля ввода
        txt1 = (EditText) findViewById(R.id.editText1);
        txt2 = (EditText) findViewById(R.id.editText2);
        txt3 = (EditText) findViewById(R.id.editText3);
        txt4 = (EditText) findViewById(R.id.editText4);
        txt5 = (EditText) findViewById(R.id.editText5);


        // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_FIELD1, DB.COLUMN_FIELD2, DB.COLUMN_FIELD3, DB.COLUMN_FIELD4, DB.COLUMN_FIELD5, DB.COLUMN_FIELD6 };
        int[] to = new int[] { R.id.tvFld1, R.id.tvFld2, R.id.tvFld3, R.id.tvFld4, R.id.tvFld5, R.id.tvFld6  };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        // создаем лоадер для чтения данных
        String str1 = txt1.getText().toString();
        String str2 = txt2.getText().toString();
        String str3 = txt3.getText().toString();
        String str4 = txt4.getText().toString();
        String str5 = txt5.getText().toString();
        String SQL =str1+str2+str3+str4+str5;
        Bundle bndl = new Bundle();
        if(!SQL.equals(""))
            bndl.putString("0",str1+" "+str2+" "+str3+" "+str4+" "+str5);
        else
            bndl.putString("0", "null");

        getSupportLoaderManager().initLoader(0, bndl, this);

    }

    // обработка нажатия кнопки
    public void onButtonClick(View view) {

        String str1 = txt1.getText().toString();
        String str2 = txt2.getText().toString();
        String str3 = txt3.getText().toString();
        String str4 = txt4.getText().toString();
        String str5 = txt5.getText().toString();
        String SQL =str1+str2+str3+str4+str5;

        if(!SQL.equals("")){
            Bundle bndl = new Bundle();
            bndl.putString("0",str1+" "+str2+" "+str3+" "+str4+" "+str5);
            getSupportLoaderManager().restartLoader(0, bndl, this);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.find_record);
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db, bndl);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        DB db;
        Bundle args;
        public static String SELECT_SQL = "";
        final String LOG_TAG = "CursorLoader";

        public MyCursorLoader(Context context, DB db, Bundle args) {
            super(context);
            this.db = db;
            this.args = args;
        }

        @Override
        public Cursor loadInBackground() {
            if (args != null)
                SELECT_SQL = args.getString("0");
            Cursor cursor = db.getData(SELECT_SQL);
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return cursor;
        }

    }
}
