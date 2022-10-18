package com.example.contacts;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class DBActivity extends AppCompatActivity {
    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/kontakti.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/kontakti.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/kontakti.db");
        if (args == null) {
            db.execSQL(SQL);
        } else {
            db.execSQL(SQL, args);
        }
        db.close();
        if (success != null)
            success.OnSuccess();
    }

    protected void InitDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists KONTAKTI( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "Tel text not null, " +
                        "Email text not null, " +
                        "unique(Email), " +
                        "unique(Tel, Name) " +
                        "); ",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "CREATE TABLE SUCCESS",
                        Toast.LENGTH_LONG
                ).show()
        );



    }
    protected void SelectSQL(String SelectQ, String[] args, OnSelectSuccess
            success)
            throws Exception
    {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/kontakti.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/kontakti.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/kontakti.db");
        Cursor cursor = db.rawQuery(SelectQ, args);
        while (cursor.moveToNext()){
            @SuppressLint("Range")
            String ID= cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range")
            String Name= cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range")
            String Tel= cursor.getString(cursor.getColumnIndex("Tel"));
            @SuppressLint("Range")
            String Email= cursor.getString(cursor.getColumnIndex("Email"));
            success.OnElementSelected(ID, Name, Tel, Email);
        }
        db.close();
    }

    protected interface OnQuerySuccess {
        public void OnSuccess();
    }
    protected interface OnSelectSuccess{
        public void
        OnElementSelected(String ID, String Name, String Tel, String Email);

    }

}
