package com.android.slowlifecourier;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.slowlifecourier.database.DatabaseContance;
import com.android.slowlifecourier.database.DatabaseOpenHelper;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    File file;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void onClick(View v) {
        SQLiteDatabase db = new DatabaseOpenHelper(this).getWritableDatabase();
        Cursor res = db.query(DatabaseContance.PROVINCE, null, null, null, null, null, null);
        System.out.println(res.getCount());

//        SQLiteDatabase db = openOrCreateDatabase("area.db", MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE  IF NOT EXISTS province('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' VARCHAR(20) NOT NULL, 'code' INTEGER(6)  NOT NULL, 'status' TINYINT(1)  DEFAULT 0);");
//        db.execSQL("create table if not exists 'test'('id' integer , 'name' varchar(10)) ");
//        db.execSQL("insert into 'test'('id','name') values(1,'a'),(2,'b')");
//        System.out.println(db.getPath());
    }

}
