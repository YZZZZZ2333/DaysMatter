package com.example.daysmatter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class detail extends AppCompatActivity {
    private TextView text0,textnum,textday,textVdsc;
    private Button btnshare,btnedit,btndel;
    private Switch switch2;
    int usrid=-1;
    int rid=0,share=0;
    String token="";
    String uname="";
    String result="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        usrid=sharedPreferences.getInt("usrid",-1);
        token=sharedPreferences.getString("token","");
        uname=sharedPreferences.getString("uname","");
        setContentView(R.layout.detail);
        text0=(TextView)findViewById(R.id.text0);
        textnum=(TextView)findViewById(R.id.textnum);
        textday=(TextView)findViewById(R.id.textday);
        textVdsc=(TextView)findViewById(R.id.textVdsc);
        btnshare=(Button)findViewById(R.id.btnshare);
        btnedit=(Button)findViewById(R.id.btnedit);
        btndel=(Button)findViewById(R.id.btndel);
        switch2=(Switch)findViewById(R.id.switch2);
        Intent intent=getIntent(); // 取得从上一个Activity当中传递过来的Intent对象
        if (intent!=null) {
            rid=intent.getIntExtra("rid",0);
            share=intent.getIntExtra("share",0);
        }
        else {
            finish();
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(this,"daysmatter",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String title="",dsc="";
        int typ=0,star=0;
        long tim=0, now=System.currentTimeMillis()/1000;
        if (share==0){          //自己的
            Cursor cursor = db.query("data",new String[]{"title","dsc","typ","star","tim"},"rid=?",new String[]{String.valueOf(rid)},null,null,null);
            while(cursor.moveToNext()) {
                title=cursor.getString(cursor.getColumnIndex("title"));
                typ=Integer.parseInt(cursor.getString(cursor.getColumnIndex("typ")));
                star=Integer.parseInt(cursor.getString(cursor.getColumnIndex("star")));
                switch2.setChecked(star==0?false:true);
                tim=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tim")));
                dsc=cursor.getString(cursor.getColumnIndex("dsc"))+"\n"+timeStamp2Date(tim);
            }
        }
        else {                  //共享的
            btnshare.setText("退出共享");
            btnedit.setClickable(false);
            btnedit.setVisibility(View.INVISIBLE);
            btndel.setClickable(false);
            btndel.setVisibility(View.INVISIBLE);
            switch2.setClickable(false);
            switch2.setVisibility(View.INVISIBLE);
            Cursor cursor = db.query("share",new String[]{"uname","title","dsc","typ","tim"},"rid=?",new String[]{String.valueOf(rid)},null,null,null);
            while(cursor.moveToNext()) {
                tim=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tim")));
                title=cursor.getString(cursor.getColumnIndex("title"));
                dsc=cursor.getString(cursor.getColumnIndex("dsc"))+"\n"+timeStamp2Date(tim)+"\n共享者："+cursor.getString(cursor.getColumnIndex("uname"));
                typ=Integer.parseInt(cursor.getString(cursor.getColumnIndex("typ")));
            }
        }
        textVdsc.setText(dsc);
        int day=(int)(Math.abs(tim-now)/86400);
        textnum.setText(String.valueOf(day));
        if (now>tim){           //以前
            text0.setText(title+" 已经：");
        }
        else {                  //未来
            text0.setText(title+" 还有：");
        }
        if (day==0){
            text0.setText(title+":");
            textnum.setText("今天");
            textday.setText("");
        }
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String star=switch2.isChecked()?"1":"0";
                List datalist = new ArrayList<>();
                datalist.add("usrid=" + usrid);
                datalist.add("token=" + token);
                datalist.add("rid=" + rid);
                datalist.add("star=" + star);
                try {
                    result = getData.getdata(datalist, "setstar.php");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share==0){  //自己的，发起共享
                    Intent intent = new Intent(detail.this,share.class);
                    intent.putExtra("rid",rid);
                    startActivity(intent);
                }
                else {          //共享的，退出共享
                    List datalist = new ArrayList<>();
                    datalist.add("usrid=" + usrid);
                    datalist.add("token=" + token);
                    datalist.add("rid=" + rid);
                    try {
                        result = getData.getdata(datalist, "exitshare.php");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = null;
                    int stat = 1;
                    String msg = "";
                    try {
                        json = new JSONObject(result);
                        stat = json.getInt("status");//获取json数组中的data项
                        msg = json.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (stat == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share==0) {
                    Intent intent = new Intent(detail.this, edit.class);
                    intent.putExtra("rid", rid);
                    startActivity(intent);
                }
            }
        });
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (share==0) {
                    List datalist = new ArrayList<>();
                    datalist.add("usrid=" + usrid);
                    datalist.add("token=" + token);
                    datalist.add("rid=" + rid);
                    try {
                        result = getData.getdata(datalist, "delete.php");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = null;
                    int stat = 1;
                    String msg = "";
                    try {
                        json = new JSONObject(result);
                        stat = json.getInt("status");//获取json数组中的data项
                        msg = json.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (stat == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }
    public static String timeStamp2Date(long seconds) {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds*1000));
    }
}
