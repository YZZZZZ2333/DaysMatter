package com.example.timetable;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class addActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    SQLiteDatabase db;
    private Spinner doWeek, sT, lT;
    private EditText claN, claR, teaN;
    private Button del,cancel,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);
        doWeek = (Spinner) findViewById(R.id.dayofW);
        sT = (Spinner) findViewById(R.id.startT);
        lT = (Spinner) findViewById(R.id.lastT);
        claN = (EditText) findViewById(R.id.classN);
        claR = (EditText) findViewById(R.id.classR);
        teaN = (EditText) findViewById(R.id.teacherN);
        cancel = (Button) findViewById(R.id.button2);
        save = (Button) findViewById(R.id.button);
        String[] week = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, week);
        doWeek.setAdapter(adapter1);
        String[] tme1 = new String[]{"第1节", "第2节", "第3节", "第4节", "第5节", "第6节", "第7节", "第8节", "第9节", "第10节", "第11节"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tme1);
        sT.setAdapter(adapter2);
        String[] tme2 = new String[]{"1节", "2节", "3节", "4节"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tme2);
        lT.setAdapter(adapter3);
        helper = new DatabaseHelper(this, "classdata", null, 1);
        db = helper.getWritableDatabase();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nclaN = claN.getText().toString();
                String ndoWeek = String.valueOf(doWeek.getSelectedItemPosition());
                String nsT = String.valueOf(sT.getSelectedItemPosition() + 1);
                String nlT = String.valueOf(lT.getSelectedItemPosition() + 1);
                String nclaR = claR.getText().toString();
                String nteaN = teaN.getText().toString();
                String sql = "insert into data values (?,?,?,?,?,?)";
                db.execSQL(sql,new Object[]{nclaN,ndoWeek,nsT,nlT,nclaR,nteaN});
                Toast toast=Toast.makeText(addActivity.this,"添加成功",Toast.LENGTH_SHORT);
                toast.show();
                db.close();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                finish();
            }
        });
    }
}
