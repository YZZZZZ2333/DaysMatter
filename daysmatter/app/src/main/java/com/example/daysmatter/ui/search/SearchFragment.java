package com.example.daysmatter.ui.search;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.daysmatter.DatabaseHelper;
import com.example.daysmatter.R;
import com.example.daysmatter.detail;

public class SearchFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View cFragment = inflater.inflate(R.layout.fragment_search, container, false);
        cFragment = course(cFragment);
        return cFragment;
    }

    public View course(View cFragment){
        LinearLayout course_linearLayout  = (LinearLayout)cFragment.findViewById(R.id.ll1);
        LinearLayout course_linearLayout2  = (LinearLayout)cFragment.findViewById(R.id.ll2);
        EditText TextSearch = new EditText(course_linearLayout.getContext());
        TextSearch.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextSearch.setTextSize(18);
        TextSearch.setHint("Search...");
        ViewGroup.MarginLayoutParams margin0 = new ViewGroup.MarginLayoutParams(TextSearch.getLayoutParams());
        margin0.setMargins(margin0.leftMargin + 80, 50, margin0.rightMargin + 80, 50);
        LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(margin0);
        TextSearch.setLayoutParams(layoutParams0);
        course_linearLayout.addView(TextSearch);
        Button searchbtn=new Button(course_linearLayout.getContext());
        searchbtn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置文字 内容与颜色
        searchbtn.setId(0);
        searchbtn.setTextSize(18);
        searchbtn.setText("搜索");
        //设置button所属的位置
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(searchbtn.getLayoutParams());
        margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
        searchbtn.setLayoutParams(layoutParams);
        course_linearLayout.addView(searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=TextSearch.getText().toString();
                long now=System.currentTimeMillis()/1000;
                course_linearLayout2.removeAllViewsInLayout();
                TextView t1=new TextView(course_linearLayout2.getContext());
                t1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                t1.setText("日程安排和纪念日：");
                ViewGroup.MarginLayoutParams margin1 = new ViewGroup.MarginLayoutParams(t1.getLayoutParams());
                margin1.setMargins(margin1.leftMargin + 100, 0, margin1.rightMargin + 100, 50);
                LinearLayout.LayoutParams layoutParamst = new LinearLayout.LayoutParams(margin1);
                t1.setLayoutParams(layoutParamst);
                t1.setTextSize(18);
                course_linearLayout2.addView(t1);
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext(),"daysmatter",null,1);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Cursor cursor = db.query("data",new String[]{"rid","title","dsc","star","tim"},"title"+" like '%" + key + "%'",null,null,null,"tim");
                while (cursor.moveToNext()) {
                    Button b = new Button(course_linearLayout2.getContext());
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    int rid=cursor.getInt(cursor.getColumnIndex("rid"));
                    long tim=cursor.getLong(cursor.getColumnIndex("tim"));
                    b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //设置文字 内容与颜色
                    int day=(int)(Math.abs(now-tim)/86400);
                    if (tim>now)
                        b.setText(title+"  "+day+"天后");
                    if (tim<now)
                        b.setText(title+"  "+day+"天前");
                    if (day==0)
                        b.setText(title+"  今天");
                    b.setId(rid);
                    b.setTextColor(Color.parseColor("#535353"));
                    //设置背景色
                    b.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    b.setTextSize(18);
                    //设置button所属的位置
                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
                    margin.setMargins(margin.leftMargin + 100, 0, margin.rightMargin + 100, 50);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                    b.setLayoutParams(layoutParams);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int rid=b.getId();
                            Intent intent = new Intent(getActivity(),detail.class);
                            intent.putExtra("rid",rid);
                            intent.putExtra("share",0);
                            startActivity(intent);
                        }
                    });
                    //添加到线性布局
                    course_linearLayout2.addView(b);
                }
                TextView t2=new TextView(course_linearLayout2.getContext());
                t2.setText("与我共享的：");
                t2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams margin2 = new ViewGroup.MarginLayoutParams(t2.getLayoutParams());
                margin2.setMargins(margin2.leftMargin + 100, 0, margin2.rightMargin + 100, 50);
                LinearLayout.LayoutParams layoutParamst2 = new LinearLayout.LayoutParams(margin2);
                t2.setLayoutParams(layoutParamst2);
                t2.setTextSize(18);
                course_linearLayout2.addView(t2);
                cursor = db.query("share",new String[]{"rid","title","dsc","typ","tim"},"title"+" like '%" + key + "%'",null,null,null,"tim");
                while (cursor.moveToNext()) {
                    Button b = new Button(course_linearLayout2.getContext());
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    int rid=cursor.getInt(cursor.getColumnIndex("rid"));
                    long tim=cursor.getLong(cursor.getColumnIndex("tim"));
                    b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //设置文字 内容与颜色
                    int day=(int)(Math.abs(now-tim)/86400);
                    if (tim>now)
                        b.setText(title+"  "+day+"天后");
                    if (tim<now)
                        b.setText(title+"  "+day+"天前");
                    if (day==0)
                        b.setText(title+"  今天");
                    b.setId(rid);
                    b.setTextColor(Color.parseColor("#535353"));
                    //设置背景色
                    b.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    b.setTextSize(18);
                    //设置button所属的位置
                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(b.getLayoutParams());
                    margin.setMargins(margin.leftMargin + 100, 20, margin.rightMargin + 100, 50);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin);
                    b.setLayoutParams(layoutParams);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int rid=b.getId();
                            Intent intent = new Intent(getActivity(),detail.class);
                            intent.putExtra("rid",rid);
                            intent.putExtra("share",1);
                            startActivity(intent);
                        }
                    });
                    //添加到线性布局
                    course_linearLayout2.addView(b);
                }
            }
        });
        return cFragment;
    }
}

