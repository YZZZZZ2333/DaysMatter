package com.example.form;



import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class post extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

Bundle bundle = this.getIntent().getExtras();
 // 接收信息并显示
        TextView message = (TextView) findViewById(R.id.message);
String text = bundle.getString("text");
// 判断

        message.setText(text);
    }
}