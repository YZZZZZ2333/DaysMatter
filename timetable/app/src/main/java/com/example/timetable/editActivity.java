package com.example.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.*;

public class editActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    SQLiteDatabase db;
    private Spinner cCL, doWeek, sT, lT;
    private EditText claN, claR, teaN;
    private Button del,cancel,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editlayout);
        cCL = (Spinner) findViewById(R.id.chooseCL);
        doWeek = (Spinner) findViewById(R.id.dayofW);
        sT = (Spinner) findViewById(R.id.startT);
        lT = (Spinner) findViewById(R.id.lastT);
        claN = (EditText) findViewById(R.id.classN);
        claR = (EditText) findViewById(R.id.classR);
        teaN = (EditText) findViewById(R.id.teacherN);
        del = (Button) findViewById(R.id.button2);
        cancel = (Button) findViewById(R.id.button3);
        save = (Button) findViewById(R.id.button);
        helper = new DatabaseHelper(this, "classdata", null, 1);
        db = helper.getWritableDatabase();
        Cursor cursor = db.query("data", new String[]{"subj", "dayow", "sttime", "lstime", "clroom", "teac"}, null, null, null, null, "dayow");
        List subjL = new ArrayList<>();
        List dayowL = new ArrayList<>();
        List sttimeL = new ArrayList<>();
        List lstimeL = new ArrayList<>();
        List clroomL = new ArrayList<>();
        List teacL = new ArrayList<>();
        while (cursor.moveToNext()) {
            String subj = cursor.getString(cursor.getColumnIndex("subj"));
            String dayow = cursor.getString(cursor.getColumnIndex("dayow"));
            String sttime = cursor.getString(cursor.getColumnIndex("sttime"));
            String lstime = cursor.getString(cursor.getColumnIndex("lstime"));
            String clroom = cursor.getString(cursor.getColumnIndex("clroom"));
            String teac = cursor.getString(cursor.getColumnIndex("teac"));
            subjL.add(subj);
            dayowL.add(dayow);
            sttimeL.add(sttime);
            lstimeL.add(lstime);
            clroomL.add(clroom);
            teacL.add(teac);
        }
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String subjo = sharedPreferences.getString("osubj","");
        int flag=-1;
        if (!subjo.equals(""))
            for (int i=0;i<subjL.size();i++)
                if (subjo.equals(subjL.get(i).toString())){
                    subjL.set(i,subjo);
                    dayowL.set(i,sharedPreferences.getString("dayow",""));
                    sttimeL.set(i,sharedPreferences.getString("sttime",""));
                    lstimeL.set(i,sharedPreferences.getString("lstime",""));
                    clroomL.set(i,sharedPreferences.getString("clroom",""));
                    teacL.set(i,sharedPreferences.getString("teac",""));
                    editor.remove("osubj");
                    editor.remove("subj");
                    editor.remove("dayow");
                    editor.remove("sttime");
                    editor.remove("lstime");
                    editor.remove("clroom");
                    editor.remove("teac");
                    editor.commit();
                    flag=i;
                    break;
                }
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjL);
        cCL.setAdapter(adapter0);
        String[] week = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, week);
        doWeek.setAdapter(adapter1);
        String[] tme1 = new String[]{"第1节", "第2节", "第3节", "第4节", "第5节", "第6节", "第7节", "第8节", "第9节", "第10节", "第11节"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tme1);
        sT.setAdapter(adapter2);
        String[] tme2 = new String[]{"1节", "2节", "3节", "4节"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tme2);
        lT.setAdapter(adapter3);
        if (flag!=-1)
            cCL.setSelection(flag);
        cCL.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                claN.setText(String.valueOf(subjL.get(arg2)));//文本说明
                doWeek.setSelection(Integer.parseInt(String.valueOf(dayowL.get(arg2))));
                sT.setSelection(Integer.parseInt(String.valueOf(sttimeL.get(arg2))) - 1);
                lT.setSelection(Integer.parseInt(String.valueOf(lstimeL.get(arg2))) - 1);
                claR.setText(String.valueOf(clroomL.get(arg2)));
                teaN.setText(String.valueOf(teacL.get(arg2)));
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nclaN = claN.getText().toString();
                String ndoWeek = String.valueOf(doWeek.getSelectedItemPosition());
                String nsT = String.valueOf(sT.getSelectedItemPosition() + 1);
                String nlT = String.valueOf(lT.getSelectedItemPosition() + 1);
                String nclaR = claR.getText().toString();
                String nteaN = teaN.getText().toString();
                String sql = "update data set subj=?, dayow=?, sttime=?, lstime=?, clroom=?, teac=? where subj=?";
                db.execSQL(sql,new Object[]{nclaN,ndoWeek,nsT,nlT,nclaR,nteaN,cCL.getSelectedItem().toString()});
                Toast toast=Toast.makeText(editActivity.this,"保存成功",Toast.LENGTH_SHORT);
                toast.show();
                db.close();
                editor.remove("osubj");
                editor.remove("subj");
                editor.remove("dayow");
                editor.remove("sttime");
                editor.remove("lstime");
                editor.remove("clroom");
                editor.remove("teac");
                editor.commit();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nclaN = claN.getText().toString();
                String ndoWeek = String.valueOf(doWeek.getSelectedItemPosition());
                String nsT = String.valueOf(sT.getSelectedItemPosition() + 1);
                String nlT = String.valueOf(lT.getSelectedItemPosition() + 1);
                String nclaR = claR.getText().toString();
                String nteaN = teaN.getText().toString();
                editor.putString("osubj",cCL.getSelectedItem().toString());
                editor.putString("subj",nclaN);
                editor.putString("dayow",ndoWeek);
                editor.putString("sttime",nsT);
                editor.putString("lstime",nlT);
                editor.putString("clroom",nclaR);
                editor.putString("teac",nteaN);
                editor.commit();
                db.close();
                finish();
            }
        });
        del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String sql="delete from data where subj=?";
                db.execSQL(sql,new Object[]{cCL.getSelectedItem().toString()});
                Toast toast=Toast.makeText(editActivity.this,"删除成功",Toast.LENGTH_SHORT);
                toast.show();
                db.close();
                editor.remove("osubj");
                editor.remove("subj");
                editor.remove("dayow");
                editor.remove("sttime");
                editor.remove("lstime");
                editor.remove("clroom");
                editor.remove("teac");
                editor.commit();
                finish();
            }
        });
    }
}

