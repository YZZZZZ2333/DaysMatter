package com.example.daysmatter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class share extends AppCompatActivity {
    private EditText inputmail;
    private Button btnaddshare;
    int usrid=-1;
    int rid=0;
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
        setContentView(R.layout.share);
        inputmail=(EditText)findViewById(R.id.inputemail);
        btnaddshare=(Button)findViewById(R.id.btnaddshare);
        Intent intent=getIntent();
        if (intent!=null) {
            rid = intent.getIntExtra("rid",0);
        }
        else {
            finish();
        }
        //TODO 共享列表
        List datalist = new ArrayList<>();
        datalist.add("usrid=" + usrid);
        datalist.add("token=" + token);
        datalist.add("rid="+rid);
        String res="";
        try {
            res = getData.getdata(datalist, "getshare.php");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject json = null;
        int stat = 1;
        String msg = "";
        final LinearLayout course_linearLayout  = (LinearLayout)findViewById(R.id.ll0);
        try {
            json = new JSONObject(res);
            stat = json.getInt("status");//获取json数组中的data项
            msg = json.getString("msg");
            JSONArray paths = json.getJSONArray("data");
            for (int i=0;i<paths.length();i++) {
                String uname = paths.getJSONObject(i).getString("uname");
                String email = paths.getJSONObject(i).getString("email");
                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btn.setId(i);
                btn.setTextColor(Color.parseColor("#535353"));
                //设置背景色
                btn.setBackgroundColor(Color.parseColor("#EEEEEE"));
                btn.setTextSize(18);
                btn.setText(" "+uname+"\n "+email);
                btn.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
                //设置button所属的位置
                ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(btn.getLayoutParams());
                margin.setMargins(margin.leftMargin + 100, 25, margin.rightMargin + 100, 25);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                btn.setLayoutParams(layoutParams);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List datalist = new ArrayList<>();
                        datalist.add("usrid=" + usrid);
                        datalist.add("token=" + token);
                        datalist.add("rid=" + rid);
                        datalist.add("dest=" + email);
                        try {
                            result = getData.getdata(datalist, "shareto.php");
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
                            Intent intent = new Intent(share.this,share.class);
                            intent.putExtra("rid",rid);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                course_linearLayout.addView(btn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnaddshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=inputmail.getText().toString();
                List datalist = new ArrayList<>();
                datalist.add("usrid=" + usrid);
                datalist.add("token=" + token);
                datalist.add("rid=" + rid);
                datalist.add("dest=" + email);
                try {
                    result = getData.getdata(datalist, "shareto.php");
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
                    Intent intent = new Intent(share.this,share.class);
                    intent.putExtra("rid",rid);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
